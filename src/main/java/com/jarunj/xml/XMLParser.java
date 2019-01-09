package com.jarunj.xml;

import com.jarunj.text.CharIterator;

import java.io.StringReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by banshooter on 12/14/17.
 */
public class XMLParser {
    public static Node parse(String xmlString) throws ParseException {
        CharIterator stream = new CharIterator(new StringReader(xmlString), 1024);
        return node(stream);
    }

    private static Node node(CharIterator stream) throws ParseException {
        if (XMLLexer.tagBegin(stream) ) {
            String tagName = XMLLexer.elementNameExpr(stream);
            if(tagName == null) {
                throw new ParseException(stream.getStringLocation() + ": expect tag after <", stream.getOffset());
            }
            Map<String, String> attrs = attributes(stream);
            if (XMLLexer.tagEndWithNodeEnd(stream)) {
                return new Node(tagName, attrs);
            } else if (XMLLexer.tagEnd(stream)) {
                XMLLexer.skipCommentAndWhiteSpace(stream);
                if (nodeEnd(stream, tagName)) {
                    return new Node(tagName, attrs);
                } else {
                    String value = nodeValue(stream);
                    Map<Node, Integer> children = new HashMap<Node, Integer>();
                    if (value == null) {
                        while(!nodeEnd(stream, tagName)) {
                            Node node = node(stream);
                            if (children.containsKey(node)) {
                                children.put(node, children.get(node) + 1);
                            } else {
                                children.put(node, 1);
                            }
                            XMLLexer.skipCommentAndWhiteSpace(stream);
                        }
                        return new Node(tagName, attrs, children);
                    } else {
                        if (nodeEnd(stream, tagName)) {
                            return new Node(tagName, attrs, value);
                        } else {
                            throw new ParseException(stream.getStringLocation() + ": expect </" + tagName +">", stream.getOffset());
                        }
                    }
                }
            } else {
                throw new ParseException(stream.getStringLocation() + ": expect > ", stream.getOffset());
            }
        } else {
            throw new ParseException(stream.getStringLocation() + ": expect < ", stream.getOffset());
        }
    }

    private static boolean nodeEnd(CharIterator stream, String tagName) throws ParseException {
        if (stream.hasNext() && stream.lookAheadEqualsAndSeek("</")) {
            XMLLexer.skipCommentAndWhiteSpace(stream);
            if (stream.hasNext() && stream.lookAheadEqualsAndSeek(tagName)) {
                XMLLexer.skipCommentAndWhiteSpace(stream);
                return XMLLexer.tagEnd(stream);
            } else {
                throw new ParseException(stream.getStringLocation() + ": expect " + tagName, stream.getOffset());
            }
        } else {
            return false;
        }
    }

    private static String nodeValue(CharIterator stream) throws ParseException {
        XMLLexer.skipCommentAndWhiteSpace(stream);
        if (!XMLLexer.isTagBegin(stream)) {
            StringBuilder sb = new StringBuilder();
            while(!XMLLexer.isTagBegin(stream)) {
                sb.append(stream.next());
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    private static Map<String, String> attributes(CharIterator stream) throws ParseException {
        Map<String, String> result = new TreeMap<String, String>();
        String attributeName = XMLLexer.elementNameExpr(stream);
        while(null != attributeName) {
            XMLLexer.skipCommentAndWhiteSpace(stream);
            if (stream.isNextChar('=')) {
                stream.next();
                XMLLexer.skipCommentAndWhiteSpace(stream);
                String value = XMLLexer.stringExpr(stream);
                if (value == null) {
                    value = XMLLexer.numberExpr(stream);
                    if (value != null &&
                            !XMLLexer.skipComment(stream) &&
                            !XMLLexer.isWhiteSpace(stream) &&
                            !XMLLexer.isTagEnd(stream) &&
                            !XMLLexer.isTagEndWithNodeEnd(stream)
                            ) {
                        throw new ParseException(stream.getStringLocation() + ": invalid numeric", stream.getOffset());
                    }
                }
                if (value == null) {
                    throw new ParseException(stream.getStringLocation() + ": expect attribute's value", stream.getOffset());
                }
                result.put(attributeName, value);
            } else {
                throw new ParseException(stream.getStringLocation() + ": expect = ", stream.getOffset());
            }
            attributeName = XMLLexer.elementNameExpr(stream);
        }
        return result;
    }



}
