import java.util.List;

public class Interpreter {

    public void execute(List<AST> statements) {

        for (AST statement : statements) {

            executeStatement(statement);

        }
    }

    private void executeStatement(AST statement) {

        if (statement instanceof PrintStatement print) {

            executePrint(print);

            return;
        }

        throw new RuntimeException(
            "Unknown statement."
        );
    }

    private void executePrint(
        PrintStatement statement
    ) {

        System.out.println(
            statement.expression.value
        );
    }
}