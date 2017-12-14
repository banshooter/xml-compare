package com.benqian.xml;

import java.io.StringReader;

/**
 * Created by benqian on 12/14/17.
 */
public class XMLParser {
    private XMLParser() {}
    private static XMLParser instance = new XMLParser();
    public static XMLParser getInstance() {
        return instance;
    }

    public Node parse(String xmlString) {
        CharIterator steam = new CharIterator(new StringReader(xmlString), 1024);
        return null;
    }
}
