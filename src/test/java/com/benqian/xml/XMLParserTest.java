package com.benqian.xml;

import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by benqian on 12/24/17.
 */
public class XMLParserTest {
    @Test
    public void testParseSimpleNode() throws IOException, ParseException {
        Node expected = new Node("T");
        Assert.assertEquals(expected, XMLParser.parse("<T/>"));
        Assert.assertEquals(expected, XMLParser.parse("<T></T>"));
        Assert.assertEquals(expected, XMLParser.parse("<T>      </T>"));
        Assert.assertEquals(expected, XMLParser.parse("<T><!----></T>"));
        Assert.assertEquals(expected, XMLParser.parse("<T><!--X--></T>"));
    }

    @Test
    public void testParseSimpleNodeWithAttributes() throws IOException, ParseException {
        Node expected = new Node("T")
                .addAttribute("x", "X")
                .addAttribute("y", "10")
                .addAttribute("z", "Z");
        Assert.assertEquals(expected, XMLParser.parse("<T x=\"X\" y=10 z=\"Z\"/>"));
        Assert.assertEquals(expected, XMLParser.parse("<T y=10 x=\"X\" z=\"Z\"/>"));
        Assert.assertEquals(expected, XMLParser.parse("<T z=\"Z\"x=\"X\" y=10/>"));
        Assert.assertEquals(expected, XMLParser.parse("<T z=\"Z\"x=\"X\" y=10></T>"));
    }

    @Test
    public void testParseSimpleNodeWithAttributesAndValue() throws IOException, ParseException {
        Node expected = new Node("T")
                .addAttribute("x", "X")
                .addAttribute("y", "10")
                .addAttribute("z", "Z");
        expected.setValue("wa ha ha");
        Assert.assertEquals(expected, XMLParser.parse("<T x=\"X\" y=10 z=\"Z\">wa ha ha</T>"));
        Assert.assertEquals(expected, XMLParser.parse("<T x=\"X\" y=10 z=\"Z\">\nwa ha ha\n</T>"));
        Assert.assertEquals(expected, XMLParser.parse("<T x=\"X\" y=10 z=\"Z\">\n\twa ha ha\t\n</T>"));
    }

    @Test
    public void testParseSimpleNodeWithAttributesAndChildren() throws IOException, ParseException {
        Node a = new Node("A");
        Node b = new Node("B").addAttribute("ba", "BO").addNode(new Node("C"));
        Node expected = new Node("T")
                .addAttribute("x", "X")
                .addAttribute("y", "10")
                .addAttribute("z", "Z")
                .addNode(a)
                .addNode(a)
                .addNode(b);
        Assert.assertEquals(expected, XMLParser.parse("<T x=\"X\" y=10 z=\"Z\"><A/><A></A><B ba=\"BO\"><C/></B></T>"));
        Assert.assertEquals(expected, XMLParser.parse("<T x=\"X\" y=10 z=\"Z\"><A/><A></A><B ba=\"BO\"><C/></B></T>"));
    }

}
