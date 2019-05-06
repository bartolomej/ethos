package vm.compiler;

import static vm.compiler.TokenType.NUMBER;
import static vm.compiler.TokenType.CHAR;
import static vm.compiler.TokenType.SYMB;
import static vm.compiler.TokenType.BOOL;
import static vm.compiler.TokenType.TYPE;

import org.junit.Test;

import java.util.StringTokenizer;

import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testCharRead() {
        TokenStream tokenStream = new TokenStream("'c'");
        try {
            Token nextToken = tokenStream.next();

            assertTrue(nextToken instanceof Token);
            assertEquals(nextToken.type, CHAR);
            assertEquals(nextToken.value, 'c');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleCharRead() {
        TokenStream tokenStream = new TokenStream("'c' 'b'");

        try {
            Token nextToken = tokenStream.next();

            assertTrue(nextToken instanceof Token);
            assertEquals(nextToken.type, CHAR);
            assertEquals(nextToken.value, 'c');
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Token nextToken = tokenStream.next();

            assertTrue(nextToken instanceof Token);
            assertEquals(nextToken.type, CHAR);
            assertEquals(nextToken.value, 'b');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleDiffTokens() {
        String input = "number a = 4;\nchar c = 'c';";
        TokenStream tokenStream = new TokenStream(input);

        try {
            tokenStream.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}