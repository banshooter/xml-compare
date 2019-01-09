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
    private String s = null;
    private int line = 0;
    private int column = 0;

    private void fetch() throws IOException {
        sb.setLength(0);
        sbIndex = 0;
        if (this.reader.read(buffer) > 0) {
            buffer.flip();
            while(buffer.hasRemaining()) {
                sb.append(buffer.get());
            }
            buffer.clear();
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
        if (this.s != null) {
            return true;
        } else {
            if (sbIndex == sb.length()) {
                fetchIgnoreException();
            }
            return sbIndex != sb.length();
        }
    }

    public Character next() {
        if (this.s != null) {
            char x = this.s.charAt(0);
            if (this.s.length() == 1) {
                this.s = null;
            } else {
                this.s = this.s.substring(1);
            }
            assignLineAndOffset(x);
            return x;
        } else {
            if (sbIndex == sb.length()) {
                fetchIgnoreException();
            }
            if(sbIndex == sb.length()) {
                throw new NoSuchElementException();
            }
            char x = sb.charAt(sbIndex++);
            assignLineAndOffset(x);
            return x;
        }
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
        assert(size <= sb.capacity());
        if ( s != null) {
            String more = "";
            if (size > this.s.length()) {
                int moreSize = size - this.s.length();
                more = sb.substring(0, Math.min(sb.length(), moreSize));
            }
            return this.s.substring(0, Math.min(size,s.length())) + more;
        } else {
            if (sbIndex+size > sb.length()) {
                int more = sbIndex+size - sb.length();
                this.s = sb.substring(sbIndex);
                fetchIgnoreException();
                return this.s + sb.substring(sbIndex, Math.min(sbIndex+more, sb.length()));
            } else {
                return sb.substring(sbIndex, Math.min(sbIndex+size,sb.length()));
            }
        }
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
