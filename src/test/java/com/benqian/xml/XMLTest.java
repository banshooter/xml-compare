package com.benqian.xml;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by benqian on 12/24/17.
 */
public class XMLTest {
    @Test
    public void testEquals() throws IOException, ParseException {
        Assert.assertEquals(new XML("<T></T>"), new XML("<T/>"));
    }

    @Test(expected = AssertionError.class)
    public void testNotEquals() throws IOException, ParseException {
        Assert.assertEquals(new XML("<T>wahaha</T>"), new XML("<T/>"));
    }

}
