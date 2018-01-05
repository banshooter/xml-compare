package com.jarunj.xml;

import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by banshooter on 12/24/17.
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
        Node expected = new Node("ns:T")
                .addAttribute("x", "X")
                .addAttribute("i:y", "10")
                .addAttribute("z", "Z");
        Assert.assertEquals(expected, XMLParser.parse("<ns:T x=\"X\" i:y=10 z=\"Z\"/>"));
        Assert.assertEquals(expected, XMLParser.parse("<ns:T i:y=10 x=\"X\" z=\"Z\"/>"));
        Assert.assertEquals(expected, XMLParser.parse("<ns:T z=\"Z\"x=\"X\" i:y=10/>"));
        Assert.assertEquals(expected, XMLParser.parse("<ns:T z=\"Z\"x=\"X\" i:y=10></ns:T>"));
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


    @Test
    public void testParseSimpleNodeWithAChildWithEmptyValueAndWithWhiteSpaceAfterChild() throws IOException, ParseException {
        Node a = new Node("A");
        Node expected = new Node("X").addNode(a);
        Assert.assertEquals(expected, XMLParser.parse("<X>\r\t<A></A>\r</X>"));
    }

    @Test(expected = ParseException.class)
    public void testNoEndStartThrow() throws IOException, ParseException {
        XMLParser.parse("<T><ERROR error=500</T>");
    }

    @Test(expected = ParseException.class)
    public void testNoEndTagThrow() throws IOException, ParseException {
        XMLParser.parse("<T><ERROR error=500></T>");
    }

}