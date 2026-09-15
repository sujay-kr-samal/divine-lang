import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<AST> parse() {

        List<AST> statements = new ArrayList<>();

        while (!isAtEnd()) {
            statements.add(statement());
        }

        return statements;
    }

    private AST statement() {

        if (match(TokenType.PRINT)) {
            return printStatement();
        }

        throw error("Expected a statement.");
    }

    private AST printStatement() {

        consume(
            TokenType.LEFT_PAREN,
            "Expected '(' after 'print'."
        );

        Token string = consume(
            TokenType.STRING,
            "Expected a string inside print()."
        );

        consume(
            TokenType.RIGHT_PAREN,
            "Expected ')' after string."
        );

        StringExpression expression =
            new StringExpression(string.value);

        return new PrintStatement(expression);
    }

    private boolean match(TokenType type) {

        if (check(type)) {
            advance();
            return true;
        }

        return false;
    }

    private Token consume(
        TokenType type,
        String message
    ) {

        if (check(type)) {
            return advance();
        }

        throw error(message);
    }

    private boolean check(TokenType type) {

        if (isAtEnd()) {
            return type == TokenType.EOF;
        }

        return peek().type == type;
    }

    private Token advance() {

        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }

    private boolean isAtEnd() {

        return peek().type == TokenType.EOF;
    }

    private Token peek() {

        return tokens.get(current);
    }

    private Token previous() {

        return tokens.get(current - 1);
    }

    private RuntimeException error(String message) {

        return new RuntimeException(
            "Syntax Error: " + message
        );
    }
}