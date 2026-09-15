package divine.parser;

import divine.lexer.Token;

public class Binary extends Expr {

    public final Expr left;
    public final Token operator;
    public final Expr right;

    public Binary(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitBinaryExpr(this);
    }
}