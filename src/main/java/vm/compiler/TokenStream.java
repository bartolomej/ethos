package vm.compiler;

import java.awt.image.TileObserver;
import java.util.ArrayList;

import static vm.compiler.TokenType.*;

public class TokenStream {

    private String input;
    public ArrayList<Token> tokens;
    private InputStream inputStream;

    public TokenStream(String input) {
        this.input = input;
        this.tokens = new ArrayList<Token>();
        this.inputStream = new InputStream(input);
    }

    public Token next() throws Exception {
        if (TokenUtil.isWhiteSpace(inputStream.peek())) {
            skipWhitespace();
            next();
        }
        if (TokenUtil.isComment(inputStream.peek())) {
            skipComment();
            next();
        }
        if (TokenUtil.isDigit(inputStream.peek())) {
           this.addToken(NUMBER, readDigit());
        }
        if (TokenUtil.isChar(inputStream.peek())) {
            this.addToken(CHAR, readChar());
        }
        if (TokenUtil.isString(inputStream.peek())) {
            this.addToken(STRING, readChar());
        }
        if (TokenUtil.isIdentifier(inputStream.peek())) {
            this.addToken(IDENTIFIER, readIdentifier());
        }
        if (TokenUtil.isSymb(inputStream.peek())) {
            this.addToken(SYMB, readSymb());
        }
        if (TokenUtil.isBool(inputStream.peekNext(6))) {
            this.addToken(BOOL, readBool());
        }
        if (TokenUtil.isType(inputStream.peekNext(4))) {
            this.addToken(TYPE, readType());
        }
        if (TokenUtil.isNull(inputStream.peek())) {
            this.addToken(EOF, inputStream.next());
        }
        throw new Exception("Not a valid token: " + inputStream.top());
    }

    private Token addToken(TokenType type, Object value) {
        Token token = new Token(type, value);
        this.tokens.add(token);
        return token;
    }

    private int readDigit() throws Exception {
        String number = "";
        boolean isDigit = true;
        while (isDigit) {
            char nextChar = inputStream.next();
            isDigit = TokenUtil.isDigit(nextChar);
            number += nextChar;
        }
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            throw new Exception("Not a valid number token: " + number);
        }
    }

    private boolean readBool() throws Exception {
        String bool = "";
        while (!TokenUtil.isWhiteSpace(inputStream.peek())) {
            char nextChar = inputStream.next();
            bool += nextChar;
        }
        if (bool == "true") return true;
        if (bool == "false") return false;
        throw new Exception("Not a valid bool token: " + bool);
    }

    private char readChar() throws Exception {
        char startParenthasis = inputStream.next();
        char character = inputStream.next();
        char endParenthasis = inputStream.next();
        if (
            TokenUtil.isChar(startParenthasis) &&
            TokenUtil.isChar(endParenthasis) &&
            TokenUtil.isChar(character)
        ) return character;
        else throw new Exception("Not a valid char token: " +
                startParenthasis + character + endParenthasis);
    }

    private String readIdentifier() throws Exception {
        String id = "";
        while (!TokenUtil.isWhiteSpace(inputStream.peek())) {
            id += inputStream.next();
        }
        return id;
    }

    private String readType() throws Exception {
        String type = "";
        type += inputStream.next();
        type += inputStream.next();
        type += inputStream.next();
        type += inputStream.next();
        if (type == "var ") return "VAR";
        throw new Exception("Not a valid type token: " + type);
    }

    private char readSymb() {
        return inputStream.next();
    }

    private void skipWhitespace() {
        while (TokenUtil.isWhiteSpace(inputStream.peek()))
            inputStream.next();
    }

    private void skipComment() {
        while (!TokenUtil.isNextLine(inputStream.peek()))
            inputStream.next();
    }

}
