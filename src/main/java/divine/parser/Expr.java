package divine.parser;

public abstract class Expr {

    public interface Visitor<R> {

        R visitLiteralExpr(Literal expr);
        R visitBinaryExpr(Binary expr);
        R visitGroupingExpr(Grouping expr);
        R visitUnaryExpr(Unary expr);
        R visitVariableExpr(Variable expr);

    }

    public abstract <R> R accept(Visitor<R> visitor);
}