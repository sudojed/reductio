package com.reductio;

import java.util.Map;
import java.util.Objects;

/**
 * Represents binary operations in mathematical expressions (+, -, *, /, ^).
 *
 * @author Reductio Team
 * @version 1.0.0
 * @since 1.0.0
 */
public final class BinaryOp extends Expr {

    private final String operator;
    private final Expr left;
    private final Expr right;

    /**
     * Creates a new binary operation.
     *
     * @param operator the operation (+, -, *, /, ^)
     * @param left the left operand
     * @param right the right operand
     * @throws IllegalArgumentException if operator is invalid or operands are null
     */
    public BinaryOp(String operator, Expr left, Expr right) {
        if (operator == null || operator.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Operator cannot be null or empty"
            );
        }
        if (left == null) {
            throw new IllegalArgumentException("Left operand cannot be null");
        }
        if (right == null) {
            throw new IllegalArgumentException("Right operand cannot be null");
        }
        if (!isValidOperator(operator)) {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }

        this.operator = operator.trim();
        this.left = left;
        this.right = right;
    }

    /**
     * Gets the operator of this binary operation.
     *
     * @return the operator string
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Gets the left operand.
     *
     * @return the left operand
     */
    public Expr getLeft() {
        return left;
    }

    /**
     * Gets the right operand.
     *
     * @return the right operand
     */
    public Expr getRight() {
        return right;
    }

    @Override
    public Expr simplify() {
        Expr leftSimp = left.simplify();
        Expr rightSimp = right.simplify();

        if (leftSimp instanceof Constant && rightSimp instanceof Constant) {
            return evaluateConstants((Constant) leftSimp, (Constant) rightSimp);
        }

        return applySimplificationRules(leftSimp, rightSimp);
    }

    @Override
    public Expr simplify(Map<Integer, String> steps, int[] stepCounter) {
        if (steps != null) {
            steps.put(stepCounter[0]++, this.show());
        }

        Expr leftSimp = left.simplify(steps, stepCounter);
        Expr rightSimp = right.simplify(steps, stepCounter);

        if (leftSimp instanceof Constant && rightSimp instanceof Constant) {
            Expr result = evaluateConstants(
                (Constant) leftSimp,
                (Constant) rightSimp
            );
            if (steps != null) {
                steps.put(stepCounter[0]++, result.show());
            }
            return result;
        }

        Expr result = applySimplificationRules(leftSimp, rightSimp);
        if (steps != null && !result.equals(this)) {
            steps.put(stepCounter[0]++, result.show());
        }
        return result;
    }

    @Override
    public String show() {
        if (needsParentheses(left, operator, true)) {
            if (needsParentheses(right, operator, false)) {
                return (
                    "(" +
                    left.show() +
                    ") " +
                    operator +
                    " (" +
                    right.show() +
                    ")"
                );
            } else {
                return "(" + left.show() + ") " + operator + " " + right.show();
            }
        } else {
            if (needsParentheses(right, operator, false)) {
                return left.show() + " " + operator + " (" + right.show() + ")";
            } else {
                return left.show() + " " + operator + " " + right.show();
            }
        }
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        double leftVal = left.evaluate(variables);
        double rightVal = right.evaluate(variables);

        switch (operator) {
            case "+":
                return leftVal + rightVal;
            case "-":
                return leftVal - rightVal;
            case "*":
                return leftVal * rightVal;
            case "/":
                if (Math.abs(rightVal) < 1e-10) {
                    throw new ArithmeticException("Division by zero");
                }
                return leftVal / rightVal;
            case "^":
                return Math.pow(leftVal, rightVal);
            default:
                throw new IllegalStateException(
                    "Unknown operator: " + operator
                );
        }
    }

    @Override
    public Expr copy() {
        return new BinaryOp(operator, left.copy(), right.copy());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BinaryOp)) return false;
        BinaryOp binaryOp = (BinaryOp) obj;
        return (
            Objects.equals(operator, binaryOp.operator) &&
            Objects.equals(left, binaryOp.left) &&
            Objects.equals(right, binaryOp.right)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, left, right);
    }

    private static boolean isValidOperator(String op) {
        return (
            "+".equals(op) ||
            "-".equals(op) ||
            "*".equals(op) ||
            "/".equals(op) ||
            "^".equals(op)
        );
    }

    private Expr evaluateConstants(Constant left, Constant right) {
        double leftVal = left.getValue();
        double rightVal = right.getValue();

        switch (operator) {
            case "+":
                return new Constant(leftVal + rightVal);
            case "-":
                return new Constant(leftVal - rightVal);
            case "*":
                return new Constant(leftVal * rightVal);
            case "/":
                if (Math.abs(rightVal) < 1e-10) {
                    return this;
                }
                return new Constant(leftVal / rightVal);
            case "^":
                return new Constant(Math.pow(leftVal, rightVal));
            default:
                return this;
        }
    }

    private Expr applySimplificationRules(Expr left, Expr right) {
        if (right instanceof Constant) {
            Constant rightConst = (Constant) right;

            switch (operator) {
                case "*":
                    if (rightConst.isZero()) return new Constant(0);
                    if (rightConst.isOne()) return left;
                    break;
                case "+":
                    if (rightConst.isZero()) return left;
                    break;
                case "-":
                    if (rightConst.isZero()) return left;
                    break;
                case "^":
                    if (rightConst.isZero()) return new Constant(1);
                    if (rightConst.isOne()) return left;
                    break;
                case "/":
                    if (rightConst.isOne()) return left;
                    break;
            }
        }

        if (left instanceof Constant) {
            Constant leftConst = (Constant) left;

            switch (operator) {
                case "*":
                    if (leftConst.isZero()) return new Constant(0);
                    if (leftConst.isOne()) return right;
                    break;
                case "+":
                    if (leftConst.isZero()) return right;
                    break;
                case "^":
                    if (leftConst.isZero()) return new Constant(0);
                    if (leftConst.isOne()) return new Constant(1);
                    break;
            }
        }

        if (left.equals(right)) {
            switch (operator) {
                case "-":
                    return new Constant(0);
                case "/":
                    return new Constant(1);
            }
        }

        return new BinaryOp(operator, left, right);
    }

    private boolean needsParentheses(
        Expr expr,
        String parentOp,
        boolean isLeft
    ) {
        if (!(expr instanceof BinaryOp)) {
            return false;
        }

        BinaryOp childOp = (BinaryOp) expr;
        int parentPrec = getOperatorPrecedence(parentOp);
        int childPrec = getOperatorPrecedence(childOp.operator);

        if (childPrec < parentPrec) {
            return true;
        }

        if (childPrec == parentPrec && !isLeft) {
            return !isLeftAssociative(parentOp);
        }

        return false;
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

    /**
     * Checks if this operation is commutative.
     *
     * @return true if the operation is commutative
     */
    public boolean isCommutative() {
        return "+".equals(operator) || "*".equals(operator);
    }

    /**
     * Checks if this operation is associative.
     *
     * @return true if the operation is associative
     */
    public boolean isAssociative() {
        return "+".equals(operator) || "*".equals(operator);
    }

    /**
     * Returns the precedence level of this operation.
     *
     * @return precedence level (higher number = higher precedence)
     */
    public int getPrecedence() {
        return getOperatorPrecedence(operator);
    }
}
