package com.jarunj.text;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by banshooter on 12/14/17.
 */
public class CharIterator implements Iterator<Character> {

    private Reader reader = null;
    private CharBuffer buffer = null;
    private int sbIndex = 0;
    private int offset = 0;
    private StringBuilder sb;
    private int line = 0;
    private int column = 0;
    private boolean canRead = true;

    private boolean outOfChar() {
        if (sbIndex == sb.length()) {
            sb.setLength(0);
            sbIndex = 0;
            return true;
        }
        return false;
    }

    private void fetch() throws IOException {
        if (canRead && this.reader.read(buffer) > 0) {
            buffer.flip();
            while(buffer.hasRemaining()) {
                sb.append(buffer.get());
            }
            buffer.clear();
        } else {
            canRead = false;
        }
    }

    private void fetchIgnoreException() {
        try {fetch();} catch(IOException ex) { ex.printStackTrace();}
    }

    public CharIterator(Reader reader, int buffSize) {
        assert(reader != null);
        assert(buffSize > 0);
        this.reader = reader;
        this.buffer = CharBuffer.allocate(buffSize);
        this.sb = new StringBuilder(buffSize);
        fetchIgnoreException();
    }

    public boolean hasNext() {
        if (outOfChar()) {
            fetchIgnoreException();
        }
        return sbIndex < sb.length();
    }

    public Character next() {
        if (outOfChar()) {
            fetchIgnoreException();
        }
        if(outOfChar()) {
            throw new NoSuchElementException();
        }
        char x = sb.charAt(sbIndex++);
        assignLineAndOffset(x);
        return x;
    }

    private void assignLineAndOffset(char x) {
        if (x == '\n') {
            this.line++;
            this.column = 0;
        } else {
            this.offset++;
            this.column++;
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("This iterator is read only.");
       //DO Nothing
    }

    public String lookAhead(int size) {
        if (sbIndex+size <= sb.length()) {
            return sb.substring(sbIndex, Math.min(sbIndex + size, sb.length()));
        }
        while (canRead && sbIndex+size > sb.length()) {
            fetchIgnoreException();
        }
        return sb.substring(sbIndex, Math.min(sbIndex + size, sb.length()));
    }

    public boolean isNextChar(char c) {
        String s = lookAhead(1);
        return s.length() == 1 && s.charAt(0) == c;
    }

    public char lookupChar() {
        String s = lookAhead(1);
        if (s.length() == 1) {
            return s.charAt(0);
        } else {
            return Character.MIN_VALUE;
        }
    }

    public boolean lookAheadEquals(String s) {
        if (s == null) throw new IllegalArgumentException("s is null");
        return this.hasNext() && s.equals(this.lookAhead(s.length()));
    }

    public boolean lookAheadEqualsAndSeek(String s) {
        if (s == null) throw new IllegalArgumentException("s is null");
        boolean result = this.lookAheadEquals(s);
        if (result) {
            this.forward(s.length());
        }
        return result;
    }

    public void forward(int charSize) {
        for(int i = 0; i < charSize; i++) next();
    }


    public int getOffset() {
        return offset;
    }

    public int getLine() { return line; }

    public int getColumn() { return column; }

    public String getStringLocation() { return "{line:"+getLine()+", column:"+getColumn()+"}"; }
}
