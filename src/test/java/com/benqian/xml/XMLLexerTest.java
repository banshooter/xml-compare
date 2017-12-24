package com.benqian.xml;

import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

public class XMLLexerTest {
    @Test
    public void testTagBegin() throws IOException, ParseException {
        CharIterator stream = new CharIterator(new StringReader("123456"), 1024);
        Assert.assertFalse(XMLLexer.tagBegin(stream));
        stream = new CharIterator(new StringReader(""), 1024);
        Assert.assertFalse(XMLLexer.tagBegin(stream));
        stream = new CharIterator(new StringReader("<"), 1024);
        Assert.assertTrue(XMLLexer.tagBegin(stream));
        stream = new CharIterator(new StringReader("<!----><"), 1024);
        Assert.assertTrue(XMLLexer.tagBegin(stream));
        stream = new CharIterator(new StringReader("<!--1--><"), 1024);
        Assert.assertTrue(XMLLexer.tagBegin(stream));
        stream = new CharIterator(new StringReader(" <!--1--> <"), 1024);
        Assert.assertTrue(XMLLexer.tagBegin(stream));
    }

    @Test
    public void testTagEnd() throws IOException, ParseException {
        CharIterator stream = new CharIterator(new StringReader("123456"), 1024);
        Assert.assertFalse(XMLLexer.tagEnd(stream));
        stream = new CharIterator(new StringReader(""), 1024);
        Assert.assertFalse(XMLLexer.tagEnd(stream));
        stream = new CharIterator(new StringReader(">"), 1024);
        Assert.assertTrue(XMLLexer.tagEnd(stream));
        stream = new CharIterator(new StringReader("<!---->>"), 1024);
        Assert.assertTrue(XMLLexer.tagEnd(stream));
        stream = new CharIterator(new StringReader("<!--1-->>"), 1024);
        Assert.assertTrue(XMLLexer.tagEnd(stream));
        stream = new CharIterator(new StringReader(" <!--1--> >"), 1024);
        Assert.assertTrue(XMLLexer.tagEnd(stream));
    }

    @Test
    public void testTagEndWithNodeEnd() throws IOException, ParseException {
        CharIterator stream = new CharIterator(new StringReader("123456"), 1024);
        Assert.assertFalse(XMLLexer.tagEndWithNodeEnd(stream));
        stream = new CharIterator(new StringReader(""), 1024);
        Assert.assertFalse(XMLLexer.tagEndWithNodeEnd(stream));
        stream = new CharIterator(new StringReader("/>"), 1024);
        Assert.assertTrue(XMLLexer.tagEndWithNodeEnd(stream));
        stream = new CharIterator(new StringReader("<!---->/>"), 1024);
        Assert.assertTrue(XMLLexer.tagEndWithNodeEnd(stream));
        stream = new CharIterator(new StringReader("<!--1-->/>"), 1024);
        Assert.assertTrue(XMLLexer.tagEndWithNodeEnd(stream));
        stream = new CharIterator(new StringReader(" <!--1--> />"), 1024);
        Assert.assertTrue(XMLLexer.tagEndWithNodeEnd(stream));
    }

    @Test
    public void testStringExpr() throws IOException, ParseException {
        CharIterator stream = new CharIterator(new StringReader("123456"), 1024);
        Assert.assertNull(XMLLexer.stringExpr(stream));
        stream = new CharIterator(new StringReader("\"123456\""), 1024);
        Assert.assertEquals("123456", XMLLexer.stringExpr(stream));
        stream = new CharIterator(new StringReader("\"\\\"ABCDF123\""), 1024);
        Assert.assertEquals("\"ABCDF123", XMLLexer.stringExpr(stream));
        stream = new CharIterator(new StringReader("  \"ABCDF123\\\"\""), 1024);
        Assert.assertEquals("ABCDF123\"", XMLLexer.stringExpr(stream));
        stream = new CharIterator(new StringReader("\"\"ABCDF123\\\"\""), 1024);
        Assert.assertEquals("", XMLLexer.stringExpr(stream));
    }

    @Test
    public void testNumberExpr() throws IOException, ParseException {
        CharIterator stream = new CharIterator(new StringReader("\"123456\""), 1024);
        Assert.assertNull(XMLLexer.numberExpr(stream));
        stream = new CharIterator(new StringReader("ABCD1234"), 1024);
        Assert.assertNull(XMLLexer.numberExpr(stream));
        stream = new CharIterator(new StringReader("1234"), 1024);
        Assert.assertEquals("1234", XMLLexer.numberExpr(stream));
        stream = new CharIterator(new StringReader("112.342"), 1024);
        Assert.assertEquals("112.342", XMLLexer.numberExpr(stream));
        stream = new CharIterator(new StringReader("1.2e10"), 1024);
        Assert.assertEquals("1.2e10", XMLLexer.numberExpr(stream));
        stream = new CharIterator(new StringReader("  1.2e10d"), 1024);
        Assert.assertEquals("1.2e10", XMLLexer.numberExpr(stream));
    }

    @Test
    public void testElementNameExpr() throws IOException, ParseException {
        CharIterator stream = new CharIterator(new StringReader("\"123456\""), 1024);
        Assert.assertNull(XMLLexer.numberExpr(stream));
        stream = new CharIterator(new StringReader("ABCD1234"), 1024);
        Assert.assertNull(XMLLexer.numberExpr(stream));
        stream = new CharIterator(new StringReader("1234"), 1024);
        Assert.assertEquals("1234", XMLLexer.numberExpr(stream));
        stream = new CharIterator(new StringReader("112.342"), 1024);
        Assert.assertEquals("112.342", XMLLexer.numberExpr(stream));
        stream = new CharIterator(new StringReader("1.2e10"), 1024);
        Assert.assertEquals("1.2e10", XMLLexer.numberExpr(stream));
        stream = new CharIterator(new StringReader("  1.2e10d"), 1024);
        Assert.assertEquals("1.2e10", XMLLexer.numberExpr(stream));
    }

}