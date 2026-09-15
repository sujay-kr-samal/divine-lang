package divine.parser;

import divine.lexer.Token;

public class Var extends Stmt {

    public final Token name;
    public final Expr initializer;

    public Var(Token name, Expr initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitVarStmt(this);
    }
}