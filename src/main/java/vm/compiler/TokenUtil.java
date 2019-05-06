package vm.compiler;

import java.util.regex.Pattern;

public class TokenUtil {

    public static boolean isNull(char c) {
        return Character.MIN_VALUE == c;
    }

    public static boolean isDigit(char c) {
        return "0123456789".indexOf(c) >= 0;
    }

    public static boolean isBool(String s) {
        return ("true ".equals(s.substring(0, 5)) || "false ".equals(s));
    }

    public static boolean isNextLine(char c) {
        return Pattern.compile("$")
                .matcher(Character.toString(c))
                .find();
    }

    public static boolean isType(String s) {
        return ("var ".equals(s) || "let ".equals(s));
    }

    public static boolean isIdentifier(char c) {
        return "\'\"".indexOf(c) < 0 && !isDigit(c);
    }

    public static boolean isOperator(char c) {
        boolean isOp = Pattern.compile("\\p{Punct}")
                .matcher(Character.toString(c))
                .find();
        return isOp && "\'\"".indexOf(c) < 0;
    }

    public static boolean isChar(char c) {
        boolean isWord = Pattern.compile("\\w")
                .matcher(Character.toString(c))
                .find();
        return isWord || c == '\'';
    }

    public static boolean isWhiteSpace(char c) {
        return Pattern.compile("\\s")
                .matcher(Character.toString(c))
                .find();
    }

    public static boolean isComment(char c) {
        return c == '/';
    }

    public static boolean isString(char c) {
        return "\"".indexOf(c) >= 0;
    }

    public static boolean isSymb(char c) {
        return "{}[]".indexOf(c) >= 0;
    }

    public static boolean isType(char c) {
        return "stringcharnumberboolfunction".indexOf(c) >= 0;
    }
}
