package vm.compiler;

import java.io.StringReader;
import java.text.StringCharacterIterator;
import java.util.StringTokenizer;

public class InputStream {

    private int column;
    private int line;
    private int counter;
    private String input;

    public InputStream(String input) {
        this.counter = -1;
        this.input = input;
    }

    public char next() {
        try {
            counter++;
            return input.charAt(counter);
        } catch (StringIndexOutOfBoundsException e) {
            return Character.MIN_VALUE;
        }
    }

    public char top() {
        return input.charAt(counter);
    }

    public char peek() {
        try {
            int nextCol = counter + 1;
            return input.charAt(nextCol);
        } catch (StringIndexOutOfBoundsException e) {
            return Character.MIN_VALUE;
        }
    }

    public String peekNext(int steps) {
        try {
            String value = "";
            for (int i = 1; i <= steps; i++) {
                int nextCol = counter + i;
                value += input.charAt(nextCol);
            }
            return value;
        } catch (StringIndexOutOfBoundsException e) {
            return ""+Character.MIN_VALUE;
        }
    }

    public boolean isEnd() {
        return next() == Character.MIN_VALUE;
    }
}
