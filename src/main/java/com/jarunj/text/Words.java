package com.jarunj.text;

import java.util.HashSet;
import java.util.Set;

public final class Words implements ICharacters {

    private final Set<Character> whiteSpaceSet = new HashSet<Character>();

    private static Words defaultWords = new Words();

    public static Words getDefaultWords() { return defaultWords; }

    public Words() {
        whiteSpaceSet.add(LF);
        whiteSpaceSet.add(CR);
        whiteSpaceSet.add(Space);
        whiteSpaceSet.add(Tab);
    }

    public Words(Character[] whiteSpaces) {
        for (Character c : whiteSpaces) {
            whiteSpaceSet.add(c);
        }
    }

    public boolean isWhiteSpace(Character c) {
        return whiteSpaceSet.contains(c);
    }

    public static boolean isNumeric(Character c) {
        return Character.isDigit(c);
    }

    public static boolean isLowerCase(Character c) {
        return 'a' <= c && c <= 'z';
    }

    public static boolean isUpperCase(Character c) {
        return 'A' <= c && c <= 'Z';
    }

    public static boolean isAlphabet(Character c) {
        return isLowerCase(c) || isUpperCase(c) || c == Underscore;
    }

    public static boolean isAlphaNumeric(Character c) {
        return isAlphabet(c) || isNumeric(c);
    }

}
