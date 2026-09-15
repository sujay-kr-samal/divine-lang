package divine.parser;

public abstract class Stmt {

    public interface Visitor<R> {

        R visitPrintStmt(Print stmt);
        R visitExpressionStmt(Expression stmt);
        R visitVarStmt(Var stmt);
        R visitBlockStmt(Block stmt);
        R visitIfStmt(If stmt);
        R visitWhileStmt(While stmt);

    }

    public abstract <R> R accept(Visitor<R> visitor);
}