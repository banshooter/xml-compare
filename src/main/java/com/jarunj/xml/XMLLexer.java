package com.jarunj.xml;

import com.jarunj.text.CharIterator;
import com.jarunj.text.Words;

import java.text.ParseException;

public class XMLLexer implements XMLReserved {

    public static boolean tagBegin(CharIterator stream) throws ParseException {
        skipCommentAndWhiteSpace(stream);
        if (isTagBegin(stream)) {
            stream.next();
            return true;
        }
        return false;
    }


    public static boolean tagEnd(CharIterator stream) throws ParseException {
        skipCommentAndWhiteSpace(stream);
        if (isTagEnd(stream)) {
            stream.next();
            return true;
        }
        return false;
    }

    public static boolean isTagBegin(CharIterator stream) {
        return stream.isNextChar(TagBegin);
    }

    public static boolean isTagEnd(CharIterator stream) {
        return stream.isNextChar(TagEnd);
    }

    public static boolean isTagEndWithNodeEnd(CharIterator stream) {
        return stream.lookAheadEquals(TagEndWithNodeEnd);
    }

    public static boolean tagEndWithNodeEnd(CharIterator stream) throws ParseException {
        skipCommentAndWhiteSpace(stream);
        return stream.lookAheadEqualsAndSeek(TagEndWithNodeEnd);
    }


    public static String stringExpr(CharIterator stream) throws ParseException {
        StringBuilder sb = new StringBuilder();
        skipCommentAndWhiteSpace(stream);
        if (stream.isNextChar('"')) {
            stream.next();
            while(!stream.isNextChar('"')) {
                if (stream.lookAheadEqualsAndSeek("\\\"")) {
                    sb.append('"');
                } else {
                    sb.append(stream.next());
                }
            }
            stream.next();
            return sb.toString();
        } else {
            return null;
        }
    }


    public static String numberExpr(CharIterator stream) throws ParseException {
        StringBuilder sb = new StringBuilder();
        skipCommentAndWhiteSpace(stream);
        if (digit(stream)) {
            while(digit(stream)) {
                sb.append(stream.next());
            }
            if (stream.hasNext() && '.' == stream.lookupChar()) {
                sb.append(stream.next());
                while(digit(stream)) {
                    sb.append(stream.next());
                }
            }
            if (stream.hasNext() && ('e' == stream.lookupChar() || 'E' == stream.lookupChar()) ) {
                sb.append(stream.next());
                if (digit(stream)) {
                    sb.append(stream.next());
                    while(digit(stream)) {
                        sb.append(stream.next());
                    }
                } else {
                    throw new ParseException(stream.getStringLocation() + ": invalid number", stream.getOffset());
                }
            }
            return sb.toString();
        } else {
            return null;
        }
    }


    public static String elementNameExpr(CharIterator stream) throws ParseException {
        skipCommentAndWhiteSpace(stream);
        StringBuilder sb = new StringBuilder();
        if (alphabet(stream)) {
            sb.append(stream.next());
            while(alphaNumeric(stream)) {
                sb.append(stream.next());
            }
            if (sb.length() > 0 && stream.isNextChar(':')) {
                sb.append(stream.next());
                String s = elementNameExpr(stream);
                if (s == null) {
                    throw new ParseException(stream.getStringLocation() + ": expect character after :", 0);
                }
                sb.append(s);
            }
            return sb.toString();
        } else {
            return null;
        }
    }


    public static void skipCommentAndWhiteSpace(CharIterator stream) throws ParseException {
        skipWhiteSpace(stream);
        skipComment(stream);
        skipWhiteSpace(stream);
    }


    public static boolean skipComment(CharIterator stream) throws ParseException {
        if(isCommentBegin(stream)) {
            while(!isCommentEnd(stream)) {
                if (stream.hasNext()) {
                    stream.next();
                } else {
                    throw new ParseException(stream.getStringLocation() + ": expect -->", stream.getOffset());
                }
            }
            return true;
        }
        return false;
    }


    public static boolean isWhiteSpace(CharIterator stream) {
        return stream.hasNext() && Words.getDefaultWords().isWhiteSpace(stream.lookupChar());
    }


    public static void skipWhiteSpace(CharIterator stream) {
        while ( isWhiteSpace(stream)) {
            stream.next();
        }
    }

    private static boolean digit(CharIterator stream) {
        return stream.hasNext() && Character.isDigit(stream.lookupChar());
    }
    private static boolean alphabet(CharIterator stream) {
        return stream.hasNext() && Words.isAlphabet(stream.lookupChar());
    }
    private static boolean alphaNumeric(CharIterator stream) {
        return stream.hasNext() && Words.isAlphaNumeric(stream.lookupChar());
    }

    private static boolean isCommentBegin(CharIterator stream) {
        return stream.lookAheadEqualsAndSeek(CommentBegin);
    }
    private static boolean isCommentEnd(CharIterator stream) {
        return stream.lookAheadEqualsAndSeek(CommentEnd);
    }


}