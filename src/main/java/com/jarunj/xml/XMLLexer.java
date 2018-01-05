package com.jarunj.xml;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

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

    public static boolean isTagEndWithNodeEnd(CharIterator stream) throws ParseException {
        return lookAheadEquals(stream, TagEndWithNodeEnd);
    }

    public static boolean tagEndWithNodeEnd(CharIterator stream) throws ParseException {
        skipCommentAndWhiteSpace(stream);
        return lookAheadEqualsAndSeek(stream, TagEndWithNodeEnd);
    }


    public static String stringExpr(CharIterator stream) throws ParseException {
        StringBuilder sb = new StringBuilder();
        skipCommentAndWhiteSpace(stream);
        if (stream.isNextChar('"')) {
            stream.next();
            while(!stream.isNextChar('"')) {
                if (lookAheadEqualsAndSeek(stream, "\\\"")) {
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
                    throw new ParseException("invalid number", stream.getOffset());
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
                    throw new ParseException("expect character after :", 0);
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


    public static boolean lookAheadEquals(CharIterator stream, String s) {
        return stream.hasNext() && s.equals(stream.lookAhead(s.length()));
    }
    public static boolean lookAheadEqualsAndSeek(CharIterator stream, String s) {
        boolean result = lookAheadEquals(stream, s);
        if (result) {
            for (int i = 0; i < s.length(); i++) stream.next();
        }
        return result;
    }


    public static boolean skipComment(CharIterator stream) throws ParseException {
        if(isCommentBegin(stream)) {
            while(!isCommentEnd(stream)) {
                if (stream.hasNext()) {
                    stream.next();
                } else {
                    throw new ParseException("expect -->", stream.getOffset());
                }
            }
            return true;
        }
        return false;
    }


    public static boolean isWhiteSpace(CharIterator stream) {
        return stream.hasNext() && XMLLexer.WhiteSpaceSet.contains(stream.lookupChar());
    }


    public static void skipWhiteSpace(CharIterator stream) {
        while ( isWhiteSpace(stream)) {
            stream.next();
        }
    }


    private static final Set<Character> WhiteSpaceSet = new HashSet<Character>();
    static {
        WhiteSpaceSet.add(Space);
        WhiteSpaceSet.add(Tab);
        WhiteSpaceSet.add(LF);
        WhiteSpaceSet.add(CR);
    }

    private static boolean digit(CharIterator stream) {
        return stream.hasNext() && Character.isDigit(stream.lookupChar());
    }
    private static boolean alphabet(CharIterator stream) {
        return stream.hasNext() && isAlphabet(stream.lookupChar());
    }
    private static boolean alphaNumeric(CharIterator stream) {
        return stream.hasNext() && isAlphaNumeric(stream.lookupChar());
    }

    private static boolean isCommentBegin(CharIterator stream) {
        return lookAheadEqualsAndSeek(stream, CommentBegin);
    }
    private static boolean isCommentEnd(CharIterator stream) {
        return lookAheadEqualsAndSeek(stream, CommentEnd);
    }

    private static boolean isAlphaNumeric(Character c) {
        return isAlphabet(c) || Character.isDigit(c);
    }

    private static boolean isAlphabet(Character c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }


}