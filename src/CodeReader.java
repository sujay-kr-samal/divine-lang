import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CodeReader {

    public static void reader(String filePath) {

        try {

            // 1. Read source code
            String content =
                Files.readString(Path.of(filePath));

            // 2. Lexer
            CodeToTokens converter =
                new CodeToTokens();

            List<Token> tokens =
                converter.convert(content);

            // 3. Parser
            Parser parser =
                new Parser(tokens);

            List<AST> ast =
                parser.parse();

            // 4. Interpreter
            Interpreter interpreter =
                new Interpreter();

            interpreter.execute(ast);

        } catch (IOException e) {

            System.err.println(
                "Error reading the file: "
                + e.getMessage()
            );

        } catch (RuntimeException e) {

            System.err.println(
                e.getMessage()
            );
        }
    }

    public static void main(String[] args) {

        if (args.length != 2) {

            System.err.println(
                "Usage: divine run <file.divine>"
            );

            return;
        }

        String command = args[0];
        String targetFile = args[1];

        if (!command.equals("run")) {

            System.err.println(
                "Unknown command: " + command
            );

            return;
        }

        if (!targetFile.endsWith(".divine")) {

            System.err.println(
                "Error: Expected a '.divine' file."
            );

            return;
        }

        reader(targetFile);
    }
}
