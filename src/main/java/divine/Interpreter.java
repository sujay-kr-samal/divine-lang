package divine;

import divine.lexer.Token;
import divine.lexer.TokenType;
import divine.parser.Binary;
import divine.parser.Block;
import divine.parser.Expr;
import divine.parser.Expression;
import divine.parser.Grouping;
import divine.parser.If;
import divine.parser.Literal;
import divine.parser.Print;
import divine.parser.Stmt;
import divine.parser.Unary;
import divine.parser.Var;
import divine.parser.Variable;
import divine.parser.While;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    private final Map<String, Object> variables = new HashMap<>();

    public void interpret(List<Stmt> statements) {
        for (Stmt statement : statements) execute(statement);
    }

    @Override
    public Void visitPrintStmt(Print statement) {
        System.out.println(stringify(evaluate(statement.expression)));
        return null;
    }

    @Override
    public Void visitExpressionStmt(Expression statement) {
        evaluate(statement.expression);
        return null;
    }

    @Override
    public Void visitVarStmt(Var statement) {
        variables.put(statement.name.value, evaluate(statement.initializer));
        return null;
    }

    @Override
    public Void visitBlockStmt(Block statement) {
        for (Stmt nested : statement.statements) execute(nested);
        return null;
    }

    @Override
    public Void visitIfStmt(If statement) {
        if (isTruthy(evaluate(statement.condition))) execute(statement.thenBranch);
        else if (statement.elseBranch != null) execute(statement.elseBranch);
        return null;
    }

    @Override
    public Void visitWhileStmt(While statement) {
        while (isTruthy(evaluate(statement.condition))) execute(statement.body);
        return null;
    }

    @Override
    public Object visitLiteralExpr(Literal expression) { return expression.value; }

    @Override
    public Object visitGroupingExpr(Grouping expression) { return evaluate(expression.expression); }

    @Override
    public Object visitVariableExpr(Variable expression) {
        if (!variables.containsKey(expression.name.value)) {
            throw runtimeError(expression.name, "Undefined variable '" + expression.name.value + "'.");
        }
        return variables.get(expression.name.value);
    }

    @Override
    public Object visitUnaryExpr(Unary expression) {
        Object right = evaluate(expression.right);
        return switch (expression.operator.type) {
            case MINUS -> -number(expression.operator, right);
            case BANG -> !isTruthy(right);
            default -> null;
        };
    }

    @Override
    public Object visitBinaryExpr(Binary expression) {
        Object left = evaluate(expression.left);
        Object right = evaluate(expression.right);

        return switch (expression.operator.type) {
            case PLUS -> {
                if (left instanceof Integer && right instanceof Integer) yield (Integer) left + (Integer) right;
                yield stringify(left) + stringify(right);
            }
            case MINUS -> number(expression.operator, left) - number(expression.operator, right);
            case STAR -> number(expression.operator, left) * number(expression.operator, right);
            case SLASH -> number(expression.operator, left) / number(expression.operator, right);
            case GREATER -> number(expression.operator, left) > number(expression.operator, right);
            case GREATER_EQUAL -> number(expression.operator, left) >= number(expression.operator, right);
            case LESS -> number(expression.operator, left) < number(expression.operator, right);
            case LESS_EQUAL -> number(expression.operator, left) <= number(expression.operator, right);
            case EQUAL_EQUAL -> isEqual(left, right);
            case BANG_EQUAL -> !isEqual(left, right);
            default -> null;
        };
    }

    private void execute(Stmt statement) { statement.accept(this); }
    private Object evaluate(Expr expression) { return expression.accept(this); }
    private boolean isTruthy(Object value) { return value != null && (!(value instanceof Boolean) || (Boolean) value); }
    private boolean isEqual(Object left, Object right) { return left == null ? right == null : left.equals(right); }
    private int number(Token token, Object value) {
        if (value instanceof Integer) return (Integer) value;
        throw runtimeError(token, "Operand must be an integer.");
    }
    private RuntimeException runtimeError(Token token, String message) {
        return new RuntimeException("Line " + token.line + ", column " + token.column + ": " + message);
    }
    private String stringify(Object value) { return value == null ? "nil" : value.toString(); }
}