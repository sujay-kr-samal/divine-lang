package divine.lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lexer {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();

    private int current = 0;
    private int start = 0;
    private int line = 1;
    private int column = 1;
    private int startLine = 1;
    private int startColumn = 1;

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> tokenize() {

        tokens.clear();
        errors.clear();
        current = 0;
        line = 1;
        column = 1;

        while (!isAtEnd()) {

            start = current;
            startLine = line;
            startColumn = column;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", line, column));

        return Collections.unmodifiableList(new ArrayList<>(tokens));
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(new ArrayList<>(errors));
    }

    private void scanToken() {

        char c = advance();

        switch (c) {

            case '(' -> addToken(TokenType.LEFT_PAREN);
            case ')' -> addToken(TokenType.RIGHT_PAREN);
            case '{' -> addToken(TokenType.LEFT_BRACE);
            case '}' -> addToken(TokenType.RIGHT_BRACE);
            case ';' -> addToken(TokenType.SEMICOLON);

            case '+' -> addToken(TokenType.PLUS);
            case '-' -> addToken(TokenType.MINUS);
            case '*' -> addToken(TokenType.STAR);
            case '/' -> {
                if (match('/')) {
                    while (!isAtEnd() && peek() != '\n') {
                        advance();
                    }
                } else {
                    addToken(TokenType.SLASH);
                }
            }

            case '=' -> addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
            case '!' -> addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
            case '<' -> addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
            case '>' -> addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);

            case ' ', '\t', '\r', '\n' -> {
                // Ignore whitespace
            }

            case '"' -> string();

            default -> {

                if (isDigit(c)) {
                    number();
                }
                else if (isAlpha(c)) {
                    identifier();
                }
                else {
                    error("Unexpected character: " + c);
                }
            }
        }
    }

    private void string() {

        while (!isAtEnd() && peek() != '"') {
            advance();
        }

        if (isAtEnd()) {
            error("Unterminated string");
            return;
        }

        advance();

        String value = source.substring(
            start + 1,
            current - 1
        );

        tokens.add(
            new Token(TokenType.STRING, value, startLine, startColumn)
        );
    }

    private void number() {

        while (!isAtEnd() && isDigit(peek())) {
            advance();
        }

        String value = source.substring(
            start,
            current
        );

        tokens.add(
            new Token(TokenType.INTEGER, value, startLine, startColumn)
        );
    }

    private void identifier() {

        while (!isAtEnd() && isAlphaNumeric(peek())) {
            advance();
        }

        String value = source.substring(
            start,
            current
        );

        TokenType type = keyword(value);

        tokens.add(
            new Token(type, value, startLine, startColumn)
        );
    }

    private TokenType keyword(String text) {

        return switch (text) {

            case "let" -> TokenType.LET;
            case "print" -> TokenType.PRINT;
            case "if" -> TokenType.IF;
            case "else" -> TokenType.ELSE;
            case "while" -> TokenType.WHILE;

            default -> TokenType.IDENTIFIER;
        };
    }

    private char advance() {

        char character = source.charAt(current++);

        if (character == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        return character;
    }

    private char peek() {

        if (isAtEnd()) {
            return '\0';
        }

        return source.charAt(current);
    }

    private boolean isAtEnd() {

        return current >= source.length();
    }

    private boolean isDigit(char c) {

        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {

        return (c >= 'a' && c <= 'z')
            || (c >= 'A' && c <= 'Z')
            || c == '_';
    }

    private boolean isAlphaNumeric(char c) {

        return isAlpha(c) || isDigit(c);
    }

    private void addToken(TokenType type) {

        String text = source.substring(
            start,
            current
        );

        tokens.add(
            new Token(type, text, startLine, startColumn)
        );
    }

    private boolean match(char expected) {

        if (isAtEnd() || source.charAt(current) != expected) {
            return false;
        }

        advance();
        return true;
    }

    private void error(String message) {

        errors.add("Line " + startLine + ", column " + startColumn + ": " + message);
    }
}