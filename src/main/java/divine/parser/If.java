package divine.parser;

public class If extends Stmt {

    public final Expr condition;
    public final Stmt thenBranch;
    public final Stmt elseBranch;

    public If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitIfStmt(this);
    }
}