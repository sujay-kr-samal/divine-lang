# Divine Lang

Divine Lang is a tiny toy programming language written in Java. It currently supports a single statement: printing a string to the console.

## What it does

The interpreter reads a `.divine` file, tokenizes it, parses the statements, and executes them.

Example program:

```text
print("Hello, world!")
print("Hey!")
```

This produces:

```text
Hello, world!
Hey!
```

## Supported syntax

```text
print("your message here")
```

- `print` is the only keyword currently implemented
- The argument must be a string literal enclosed in double quotes
- Each statement is executed in order

## Project structure

- `src/CodeReader.java` — program entry point and command handling
- `src/CodeToTokens.java` — lexer/tokenizer
- `src/Parser.java` — parser for `print(...)` statements
- `src/Interpreter.java` — runtime executor
- `src/AST.java` and related classes — AST and statement models
- `examples/print.divine` — sample program

## Run it

From the project root:

```bash
javac src/*.java
java -cp src CodeReader run examples/print.divine
```

You should see:

```text
Hello, world!
Hey!
```

## Notes

This is intentionally minimal and educational. It demonstrates the classic pipeline:

```text
source code -> tokens -> AST -> interpreter
```

If you want to extend it, the next natural features would be:

- variables
- numbers and arithmetic
- multiple statement types
- better error handling
- a real `main` class or CLI wrapper

## Example file

The included example is in `examples/print.divine`:

```text
print("Hello!")

print("Hey!")
```

