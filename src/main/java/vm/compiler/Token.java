package vm.compiler;

public class Token {

    public TokenType type;
    public Object value;

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return String.format("TYPE: %s VALUE: %s", type, value);
    }
}
