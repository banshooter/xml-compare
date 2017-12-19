package com.benqian.xml;

import java.io.StringReader;
import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by benqian on 12/14/17.
 */
public class XMLParser {
    public static Node parse(String xmlString) throws ParseException {
        CharIterator stream = new CharIterator(new StringReader(xmlString), 1024);
        return node(stream);
    }

    private static Node node(CharIterator stream) throws ParseException {
        if (XMLLexer.isTagBegin(stream) ) {
            String tagName = XMLLexer.elementNameExpr(stream);
            if(tagName == null) {
                throw new ParseException("expect tag after <", stream.getOffset());
            }
            Map<String, String> attrs = attributes(stream);
            if (XMLLexer.isTagEndWithNodeEnd(stream)) {
                return new Node(tagName, attrs);
            } else if (XMLLexer.isTagEnd(stream)) {
                //TODO: parse child or value and then </TagEnd>
                return new Node(tagName);
            } else {
                throw new ParseException("Expect > ", stream.getOffset());
            }
        } else {
            throw new ParseException("Expect < ", stream.getOffset());
        }
    }

    private static Map<String, String> attributes(CharIterator stream) throws ParseException {
        Map<String, String> result = new TreeMap<String, String>();
        String attributeName = XMLLexer.elementNameExpr(stream);
        while(null != attributeName) {
            String value = null;
            XMLLexer.skipCommentAndWhiteSpace(stream);
            if (stream.isNextChar('=')) {
                XMLLexer.skipCommentAndWhiteSpace(stream);
                value = XMLLexer.stringExpr(stream);
                if (value == null) {
                    value = XMLLexer.numberExpr(stream);
                }
                if (value == null) {
                    throw new ParseException("expect attribute's value", stream.getOffset());
                }
                result.put(attributeName, value);
            } else {
                throw new ParseException("expect = ", stream.getOffset());
            }
            attributeName = XMLLexer.elementNameExpr(stream);
        }
        return result;
    }



}
