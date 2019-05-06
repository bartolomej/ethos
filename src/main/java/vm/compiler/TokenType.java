package vm.compiler;

public enum TokenType {
    NUMBER, // contains number chars 0-9
    CHAR, // begins and ends with ' and contains chars a-z | 0-9
    IDENTIFIER, // begins with char a-z and contains chars a-z | 0-9
    OPERATOR, // is =, <, >, <=, >=, ==, +, -, /, %, !=,
    BOOL, // begins with t or f
    TYPE, // var followed by space
    SYMB, // {,},[,],
    STRING, // begins with "
    EOF // end of file
}
