package com.reductio;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Parser for converting mathematical expressions from string to AST.
 * Supports standard mathematical operations and functions.
 *
 * @author Reductio Team
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Parser {

    private static final Set<String> SUPPORTED_FUNCTIONS = Set.of(
        "ln",
        "log",
        "sin",
        "cos",
        "tan",
        "sec",
        "csc",
        "cot",
        "sinh",
        "cosh",
        "tanh",
        "sqrt",
        "abs"
    );

    private static final Pattern NUMBER_PATTERN = Pattern.compile(
        "-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?"
    );

    private static final Pattern VARIABLE_PATTERN = Pattern.compile(
        "[a-zA-Z][a-zA-Z0-9]*"
    );

    private Parser() {
        // Utility class
    }

    /**
     * Parses a mathematical expression string into an expression tree.
     *
     * @param expression the mathematical expression as a string
     * @return the parsed expression tree
     * @throws ParseException if the expression is invalid
     */
    public static Expr parse(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new ParseException("Expression cannot be null or empty");
        }

        try {
            String normalized = normalizeExpression(expression);
            List<Token> tokens = tokenize(normalized);
            return parseTokens(tokens);
        } catch (Exception e) {
            throw new ParseException(
                "Failed to parse expression: " + expression,
                e
            );
        }
    }

    /**
     * Validates if an expression string is syntactically correct.
     *
     * @param expression the expression to validate
     * @return true if the expression is valid
     */
    public static boolean isValid(String expression) {
        try {
            parse(expression);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Gets the set of supported function names.
     *
     * @return immutable set of function names
     */
    public static Set<String> getSupportedFunctions() {
        return SUPPORTED_FUNCTIONS;
    }

    private static String normalizeExpression(String expression) {
        String normalized = expression.replaceAll("\\s+", "");
        normalized = insertImplicitMultiplication(normalized);
        normalized = handleUnaryMinus(normalized);
        return normalized;
    }

    private static String insertImplicitMultiplication(String expression) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);
            result.append(current);

            if (i < expression.length() - 1) {
                char next = expression.charAt(i + 1);

                if (shouldInsertMultiplication(current, next, expression, i)) {
                    result.append('*');
                }
            }
        }

        return result.toString();
    }

    private static boolean shouldInsertMultiplication(
        char current,
        char next,
        String expression,
        int position
    ) {
        // Number followed by letter or opening parenthesis
        if (
            Character.isDigit(current) &&
            (Character.isLetter(next) || next == '(')
        ) {
            return true;
        }

        // Closing parenthesis followed by digit, letter, or opening parenthesis
        if (
            current == ')' &&
            (Character.isDigit(next) || Character.isLetter(next) || next == '(')
        ) {
            return true;
        }

        // Letter followed by digit or opening parenthesis (but not if it's a function)
        if (
            Character.isLetter(current) &&
            (Character.isDigit(next) || next == '(')
        ) {
            String beforeNext = expression.substring(0, position + 1);
            return !endsWithFunction(beforeNext);
        }

        return false;
    }

    private static boolean endsWithFunction(String str) {
        for (String function : SUPPORTED_FUNCTIONS) {
            if (str.endsWith(function)) {
                return true;
            }
        }
        return false;
    }

    private static String handleUnaryMinus(String expression) {
        StringBuilder result = new StringBuilder();
        boolean expectOperand = true;

        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);

            if (current == '-' && expectOperand) {
                result.append("0-");
            } else {
                result.append(current);
            }

            expectOperand = (current == '(' || isOperator(current));
        }

        return result.toString();
    }

    private static List<Token> tokenize(String expression) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                buffer.append(c);
            } else if (Character.isLetter(c)) {
                if (buffer.length() > 0 && isNumber(buffer.toString())) {
                    tokens.add(new Token(TokenType.NUMBER, buffer.toString()));
                    buffer.setLength(0);
                }
                buffer.append(c);
            } else {
                if (buffer.length() > 0) {
                    String value = buffer.toString();
                    if (isNumber(value)) {
                        tokens.add(new Token(TokenType.NUMBER, value));
                    } else if (SUPPORTED_FUNCTIONS.contains(value)) {
                        tokens.add(new Token(TokenType.FUNCTION, value));
                    } else {
                        tokens.add(new Token(TokenType.VARIABLE, value));
                    }
                    buffer.setLength(0);
                }

                TokenType type = getTokenType(c);
                tokens.add(new Token(type, String.valueOf(c)));
            }
        }

        if (buffer.length() > 0) {
            String value = buffer.toString();
            if (isNumber(value)) {
                tokens.add(new Token(TokenType.NUMBER, value));
            } else if (SUPPORTED_FUNCTIONS.contains(value)) {
                tokens.add(new Token(TokenType.FUNCTION, value));
            } else {
                tokens.add(new Token(TokenType.VARIABLE, value));
            }
        }

        return tokens;
    }

    private static Expr parseTokens(List<Token> tokens) {
        if (tokens.isEmpty()) {
            throw new ParseException("Empty token list");
        }

        Stack<Expr> operands = new Stack<>();
        Stack<Token> operators = new Stack<>();

        for (Token token : tokens) {
            switch (token.getType()) {
                case NUMBER:
                    operands.push(
                        new Constant(Double.parseDouble(token.getValue()))
                    );
                    break;
                case VARIABLE:
                    operands.push(new Variable(token.getValue()));
                    break;
                case FUNCTION:
                    operators.push(token);
                    break;
                case LEFT_PAREN:
                    operators.push(token);
                    break;
                case RIGHT_PAREN:
                    while (
                        !operators.isEmpty() &&
                        operators.peek().getType() != TokenType.LEFT_PAREN
                    ) {
                        applyOperator(operands, operators.pop());
                    }
                    if (!operators.isEmpty()) {
                        operators.pop(); // Remove left parenthesis
                    }
                    if (
                        !operators.isEmpty() &&
                        operators.peek().getType() == TokenType.FUNCTION
                    ) {
                        applyOperator(operands, operators.pop());
                    }
                    break;
                case OPERATOR:
                    while (
                        !operators.isEmpty() &&
                        shouldApplyOperator(token, operators.peek())
                    ) {
                        applyOperator(operands, operators.pop());
                    }
                    operators.push(token);
                    break;
            }
        }

        while (!operators.isEmpty()) {
            applyOperator(operands, operators.pop());
        }

        if (operands.size() != 1) {
            throw new ParseException("Invalid expression structure");
        }

        return operands.pop();
    }

    private static void applyOperator(Stack<Expr> operands, Token operator) {
        if (operator.getType() == TokenType.FUNCTION) {
            if (operands.isEmpty()) {
                throw new ParseException("Function requires an argument");
            }
            Expr arg = operands.pop();
            operands.push(new FunctionExpr(operator.getValue(), arg));
        } else if (operator.getType() == TokenType.OPERATOR) {
            if (operands.size() < 2) {
                throw new ParseException(
                    "Binary operator requires two operands"
                );
            }
            Expr right = operands.pop();
            Expr left = operands.pop();
            operands.push(new BinaryOp(operator.getValue(), left, right));
        }
    }

    private static boolean shouldApplyOperator(Token current, Token top) {
        if (top.getType() == TokenType.LEFT_PAREN) {
            return false;
        }

        if (top.getType() == TokenType.FUNCTION) {
            return false;
        }

        if (
            current.getType() != TokenType.OPERATOR ||
            top.getType() != TokenType.OPERATOR
        ) {
            return false;
        }

        int currentPrec = getOperatorPrecedence(current.getValue());
        int topPrec = getOperatorPrecedence(top.getValue());

        return (
            topPrec > currentPrec ||
            (topPrec == currentPrec && isLeftAssociative(current.getValue()))
        );
    }

    private static boolean isNumber(String str) {
        return NUMBER_PATTERN.matcher(str).matches();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static TokenType getTokenType(char c) {
        switch (c) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
                return TokenType.OPERATOR;
            case '(':
                return TokenType.LEFT_PAREN;
            case ')':
                return TokenType.RIGHT_PAREN;
            default:
                throw new ParseException("Unknown character: " + c);
        }
    }

    private static int getOperatorPrecedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }

    private static boolean isLeftAssociative(String op) {
        return !op.equals("^");
    }

    private enum TokenType {
        NUMBER,
        VARIABLE,
        OPERATOR,
        FUNCTION,
        LEFT_PAREN,
        RIGHT_PAREN,
    }

    private static class Token {

        private final TokenType type;
        private final String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        public TokenType getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return type + "(" + value + ")";
        }
    }

    /**
     * Exception thrown when parsing fails.
     */
    public static class ParseException extends RuntimeException {

        public ParseException(String message) {
            super(message);
        }

        public ParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
