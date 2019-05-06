package vm.compiler;

import org.junit.Test;
import vm.compiler.InputStream;

import static org.junit.Assert.*;

public class InputStreamTest {

    @Test
    public void testPeekRead() {
        String programInput = "var ab = 5;\nlog(ab);\n ";
        InputStream inputStream = new InputStream(programInput);

        assertEquals(inputStream.peek(), 'v');
        assertEquals(inputStream.next(), 'v');

        assertEquals(inputStream.peek(), 'a');
        assertEquals(inputStream.next(), 'a');
    }

    @Test
    public void testSimpleStream() {
        String programInput = "var ab = 5;\nlog(ab);\n ";
        InputStream inputStream = new InputStream(programInput);

        while (!inputStream.isEnd()) {
            char nextChar = inputStream.next();
            int indexOf = programInput.indexOf(nextChar);
            assertTrue(indexOf >= 0);
        }
    }

    @Test
    public void testStreamInput() {
        String programInput = "'a' 'b'";
        InputStream inputStream = new InputStream(programInput);

        char firstPeek = inputStream.peek();
        char secondPeek = inputStream.peek();

        char firstStartParenthasis = inputStream.next();
        char firstMainChar = inputStream.next();
        char firstEndParenthasis = inputStream.next();
        char whiteSpace = inputStream.next();
        char secondStartParenthasis = inputStream.next();
        char secondMainChar = inputStream.next();
        char secondMainParenthasis = inputStream.next();

        assertEquals(firstPeek, secondPeek);

        assertEquals(firstStartParenthasis, '\'');
        assertEquals(firstMainChar, 'a');
        assertEquals(firstEndParenthasis, '\'');
        assertEquals(whiteSpace, ' ');
        assertEquals(secondStartParenthasis, '\'');
        assertEquals(secondMainChar, 'b');
        assertEquals(secondMainParenthasis, '\'');
    }

    @Test
    public void testPeekNextInput() {
        String programInput = "'a' 'b'";
        InputStream inputStream = new InputStream(programInput);

        assertEquals(inputStream.peekNext(4), "'a' ");

        inputStream.next();
        inputStream.next();
        inputStream.next();
        inputStream.next();

        assertEquals(inputStream.peekNext(3), "'b'");
    }
}