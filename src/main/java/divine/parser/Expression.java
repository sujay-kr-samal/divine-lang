package divine.parser;

public class Expression extends Stmt {

    public final Expr expression;

    public Expression(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitExpressionStmt(this);
    }
}