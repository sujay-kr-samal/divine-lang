package divine.parser;

public class Grouping extends Expr {

    public final Expr expression;

    public Grouping(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitGroupingExpr(this);
    }
}