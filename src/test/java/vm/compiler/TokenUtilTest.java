package vm.compiler;

import org.junit.Test;
import vm.compiler.TokenUtil;

import java.util.function.ToIntBiFunction;

import static org.junit.Assert.*;

public class TokenUtilTest {

    @Test
    public void testIfDigit() {
        assertTrue(TokenUtil.isDigit('0'));
        assertTrue(TokenUtil.isDigit('3'));
        assertTrue(TokenUtil.isDigit('4'));

        assertFalse(TokenUtil.isDigit('i'));
        assertFalse(TokenUtil.isDigit(' '));
        assertFalse(TokenUtil.isDigit(','));
    }

    @Test
    public void testIfChar() {
        assertTrue(TokenUtil.isChar('\''));
        assertTrue(TokenUtil.isChar('c'));
        assertTrue(TokenUtil.isChar('a'));
        assertTrue(TokenUtil.isChar('q'));

        assertFalse(TokenUtil.isChar('\"'));
    }

    @Test
    public void testIfBool() {
        assertTrue(TokenUtil.isBool("true "));
        assertTrue(TokenUtil.isBool("false "));

        assertFalse(TokenUtil.isBool("truef"));
    }

    @Test
    public void testIfIdentifier() {
        assertTrue(TokenUtil.isType("var "));
        assertTrue(TokenUtil.isType("let "));

        assertFalse(TokenUtil.isType("vara"));
    }

    @Test
    public void testIfOperator() {
        assertTrue(TokenUtil.isOperator('%'));
        assertTrue(TokenUtil.isOperator('%'));
        assertTrue(TokenUtil.isOperator('%'));

        assertFalse(TokenUtil.isOperator('\''));
        assertFalse(TokenUtil.isOperator('\"'));
        assertFalse(TokenUtil.isOperator('d'));
    }

    @Test
    public void testIfType() {
        assertTrue(TokenUtil.isIdentifier('n'));
        assertTrue(TokenUtil.isIdentifier('c'));
        assertTrue(TokenUtil.isIdentifier('b'));
        assertTrue(TokenUtil.isIdentifier('f'));

        assertFalse(TokenUtil.isIdentifier('3'));
        assertFalse(TokenUtil.isIdentifier('\''));
        assertFalse(TokenUtil.isIdentifier('\"'));
    }

    @Test
    public void testIfSymb() {
        assertTrue(TokenUtil.isSymb('['));
        assertTrue(TokenUtil.isSymb(']'));
        assertTrue(TokenUtil.isSymb('{'));
        assertTrue(TokenUtil.isSymb('}'));

        assertFalse(TokenUtil.isSymb('-'));
        assertFalse(TokenUtil.isSymb('d'));
        assertFalse(TokenUtil.isSymb('£'));
    }

    @Test
    public void testIfWhiteSpace() {
        assertTrue(TokenUtil.isWhiteSpace(' '));
        assertTrue(TokenUtil.isWhiteSpace('\n'));

        assertFalse(TokenUtil.isWhiteSpace('\\'));
    }

}