package divine;

import divine.lexer.Lexer;
import divine.parser.Parser;
import divine.parser.Stmt;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.out.println("Usage: divine <file.div>");
            return;
        }

        Path file = Path.of(args[0]);

        if (!Files.exists(file)) {
            System.out.println("File not found: " + file);
            return;
        }

        String source = Files.readString(file);

        Lexer lexer = new Lexer(source);

        lexer.tokenize();

        if (!lexer.getErrors().isEmpty()) {
            for (String error : lexer.getErrors()) {
                System.err.println(error);
            }
            System.exit(1);
        }

        Parser parser = new Parser(lexer.tokenize());
        List<Stmt> statements = parser.parse();

        if (!parser.getErrors().isEmpty()) {
            for (String error : parser.getErrors()) {
                System.err.println(error);
            }
            System.exit(1);
        }

        try {
            new Interpreter().interpret(statements);
        } catch (RuntimeException error) {
            System.err.println(error.getMessage());
            System.exit(1);
        }
    }
}