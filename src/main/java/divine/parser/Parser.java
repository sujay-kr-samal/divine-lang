package divine.parser;

import divine.lexer.Token;
import divine.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private final List<String> errors = new ArrayList<>();
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            try {
                statements.add(statement());
            } catch (ParseError error) {
                synchronize();
            }
        }
        return statements;
    }

    public List<String> getErrors() {
        return List.copyOf(errors);
    }

    private Stmt statement() {
        if (match(TokenType.PRINT)) return printStatement();
        if (match(TokenType.LET)) return varStatement();
        if (match(TokenType.IF)) return ifStatement();
        if (match(TokenType.WHILE)) return whileStatement();
        if (match(TokenType.LEFT_BRACE)) return new Block(block());
        return expressionStatement();
    }

    private Stmt printStatement() {
        consume(TokenType.LEFT_PAREN, "Expected '(' after print.");
        Expr value = expression();
        consume(TokenType.RIGHT_PAREN, "Expected ')' after value.");
        match(TokenType.SEMICOLON);
        return new Print(value);
    }

    private Stmt varStatement() {
        Token name = consume(TokenType.IDENTIFIER, "Expected variable name.");
        consume(TokenType.EQUAL, "Expected '=' after variable name.");
        Expr initializer = expression();
        match(TokenType.SEMICOLON);
        return new Var(name, initializer);
    }

    private Stmt ifStatement() {
        consume(TokenType.LEFT_PAREN, "Expected '(' after if.");
        Expr condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expected ')' after condition.");
        Stmt thenBranch = statement();
        Stmt elseBranch = match(TokenType.ELSE) ? statement() : null;
        return new If(condition, thenBranch, elseBranch);
    }

    private Stmt whileStatement() {
        consume(TokenType.LEFT_PAREN, "Expected '(' after while.");
        Expr condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expected ')' after condition.");
        return new While(condition, statement());
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) statements.add(statement());
        consume(TokenType.RIGHT_BRACE, "Expected '}' after block.");
        return statements;
    }

    private Stmt expressionStatement() {
        Expr value = expression();
        match(TokenType.SEMICOLON);
        return new Expression(value);
    }

    private Expr expression() { return equality(); }

    private Expr equality() {
        Expr expr = comparison();
        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            expr = new Binary(expr, operator, comparison());
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            expr = new Binary(expr, operator, term());
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            expr = new Binary(expr, operator, factor());
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            expr = new Binary(expr, operator, unary());
        }
        return expr;
    }

    private Expr unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) return new Unary(previous(), unary());
        return primary();
    }

    private Expr primary() {
        if (match(TokenType.INTEGER)) return new Literal(Integer.parseInt(previous().value));
        if (match(TokenType.STRING)) return new Literal(previous().value);
        if (match(TokenType.IDENTIFIER)) return new Variable(previous());
        if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expected ')' after expression.");
            return new Grouping(expr);
        }
        throw error(peek(), "Expected an expression.");
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        errors.add("Line " + token.line + ", column " + token.column + ": " + message);
        return new ParseError();
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) return;
            if (check(TokenType.IF) || check(TokenType.WHILE) || check(TokenType.LET)
                || check(TokenType.PRINT) || check(TokenType.RIGHT_BRACE)) return;
            advance();
        }
    }

    private boolean check(TokenType type) { return !isAtEnd() && peek().type == type; }
    private Token advance() { if (!isAtEnd()) current++; return previous(); }
    private boolean isAtEnd() { return peek().type == TokenType.EOF; }
    private Token peek() { return tokens.get(current); }
    private Token previous() { return tokens.get(current - 1); }

    private static class ParseError extends RuntimeException { }
}