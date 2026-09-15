import java.util.ArrayList;
import java.util.List;

public class CodeToTokens {

    public List<Token> convert(String code) {

        List<Token> tokens = new ArrayList<>();

        int i = 0;

        while (i < code.length()) {

            char current = code.charAt(i);

            
            if (Character.isWhitespace(current)) {
                i++;
                continue;
            }

            if (current == '(') {
                tokens.add(
                    new Token(TokenType.LEFT_PAREN, "(")
                );

                i++;
                continue;
            }

            if (current == ')') {
                tokens.add(
                    new Token(TokenType.RIGHT_PAREN, ")")
                );

                i++;
                continue;
            }

            // String
            if (current == '"') {

                i++;

                StringBuilder value = new StringBuilder();

                while (
                    i < code.length()
                    && code.charAt(i) != '"'
                ) {
                    value.append(code.charAt(i));
                    i++;
                }

                if (i >= code.length()) {
                    throw new RuntimeException(
                        "Unterminated string."
                    );
                }

                i++;

                tokens.add(
                    new Token(
                        TokenType.STRING,
                        value.toString()
                    )
                );

                continue;
            }

            // Words / keywords
            if (Character.isLetter(current)) {

                StringBuilder word = new StringBuilder();

                while (
                    i < code.length()
                    && Character.isLetterOrDigit(
                        code.charAt(i)
                    )
                ) {
                    word.append(code.charAt(i));
                    i++;
                }

                String value = word.toString();

                if (value.equals("print")) {

                    tokens.add(
                        new Token(
                            TokenType.PRINT,
                            value
                        )
                    );

                } else {

                    tokens.add(
                        new Token(
                            TokenType.IDENTIFIER,
                            value
                        )
                    );
                }

                continue;
            }

            throw new RuntimeException(
                "Unexpected character: " + current
            );
        }

        tokens.add(
            new Token(TokenType.EOF, "")
        );

        return tokens;
    }
}