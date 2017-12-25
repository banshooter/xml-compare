package com.jarunj.xml;

import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;

/**
 * Unit test for simple App.
 */
public class CharIteratorTest {
    @Test(expected = AssertionError.class)
    public void testConstructorThrow() throws IOException
    {
        new CharIterator(null, 100);
    }

    @Test(expected = AssertionError.class)
    public void testConstructorThrow2() throws IOException
    {
        new CharIterator(new StringReader("1234"), 0);
    }

    @Test
    public void testConstructorHappy() throws IOException {
        new CharIterator(new StringReader("123"), 10);
    }

    @Test
    public void testHasNextAndNextHappy() throws IOException {
        String s = "1234567890ABCDEF";
        CharIterator testObject = new CharIterator(new StringReader(s), 10);
        int times = 0;
        while(testObject.hasNext()) {
            testObject.next();
            times++;
        }
        Assert.assertEquals(times, s.length());
        Assert.assertFalse(testObject.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testHasNextAndNextThrow() throws IOException {
        String s = "1234567890ABCDEF";
        CharIterator testObject = new CharIterator(new StringReader(s), 10);
        for (int i = 0; i < s.length(); i++) {
            testObject.next();
        }
        testObject.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMustThrow() throws IOException {
        String s = "1234567890ABCDEF";
        CharIterator testObject = new CharIterator(new StringReader(s), 10);
        testObject.remove();
    }

    @Test
    public void testLookAheadHappy() throws IOException {
        String s = "1234567890ABCDEF";
        CharIterator testObject = new CharIterator(new StringReader(s), 10);
        for (int timesOfNext = 0; timesOfNext < 10; timesOfNext++) {
            for ( int i = 0; i < 10; i++) {
                Assert.assertEquals(
                        s.substring(timesOfNext, Math.min(s.length(), timesOfNext+i+1)),
                        testObject.lookAhead(i+1)
                );
            }
            testObject.next();
        }
    }

}
