public class PrintStatement implements AST {

    public final StringExpression expression;

    public PrintStatement(
        StringExpression expression
    ) {
        this.expression = expression;
    }
}