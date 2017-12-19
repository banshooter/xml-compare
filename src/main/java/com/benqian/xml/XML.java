package com.benqian.xml;

import java.text.ParseException;

/**
 * Created by benqian on 12/14/17.
 */
public class XML {
    private String xmlString = null;
    private Node node = null;

    public XML(String xmlString) throws ParseException {
        this.xmlString = xmlString;
        this.node = XMLParser.parse(xmlString);
    }

    public String toString() {
        return this.xmlString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XML)) return false;

        XML xml = (XML) o;

        return node != null ? node.equals(xml.node) : xml.node == null;
    }

    @Override
    public int hashCode() {
        return node != null ? node.hashCode() : 0;
    }
}
