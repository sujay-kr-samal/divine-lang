package divine.lexer;

public enum TokenType {
    // Keywords
    LET,
    PRINT,
    IF,
    ELSE,
    WHILE,

    // Values
    IDENTIFIER,
    INTEGER,
    STRING,

    // Operators
    PLUS,
    MINUS,
    STAR,
    SLASH,
    EQUAL,
    EQUAL_EQUAL,
    BANG,
    BANG_EQUAL,
    LESS,
    LESS_EQUAL,
    GREATER,
    GREATER_EQUAL,

    // Symbols
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    SEMICOLON,

    // Special
    EOF
}