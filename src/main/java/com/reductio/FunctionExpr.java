package com.reductio;

import java.util.*;
import java.util.Objects;

// Funções (ln, sin, cos, etc.)
public class FunctionExpr extends Expr {

    public String name;
    public Expr arg;

    public FunctionExpr(String name, Expr arg) {
        this.name = name;
        this.arg = arg;
    }

    @Override
    public Expr simplify() {
        Map<Integer, String> dummySteps = new LinkedHashMap<>();
        int[] dummyCounter = { 0 };
        return simplify(dummySteps, dummyCounter);
    }

    @Override
    public Expr simplify(Map<Integer, String> steps, int[] stepCounter) {
        steps.put(stepCounter[0]++, this.show());
        Expr A = arg.simplify(steps, stepCounter);

        // ln(e^x) => x
        if (name.equals("ln") && A instanceof BinaryOp) {
            BinaryOp b = (BinaryOp) A;
            if (
                b.getOperator().equals("^") &&
                b.getLeft() instanceof Variable &&
                ((Variable) b.getLeft()).getName().equals("e")
            ) {
                Expr result = b.getRight().simplify(steps, stepCounter);
                steps.put(stepCounter[0]++, result.show());
                return result;
            }
        }

        // sin(0) = 0, cos(0) = 1, etc.
        if (A instanceof Constant) {
            double value = ((Constant) A).getValue();
            switch (name) {
                case "sin":
                    if (value == 0) return new Constant(0);
                    break;
                case "cos":
                    if (value == 0) return new Constant(1);
                    break;
                case "ln":
                    if (value == 1) return new Constant(0);
                    if (value == Math.E) return new Constant(1);
                    break;
            }
        }

        Expr result = new FunctionExpr(name, A);
        steps.put(stepCounter[0]++, result.show());
        return result;
    }

    @Override
    public String show() {
        return name + "(" + arg.show() + ")";
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        double argValue = arg.evaluate(variables);

        switch (name) {
            case "sin":
                return Math.sin(argValue);
            case "cos":
                return Math.cos(argValue);
            case "tan":
                return Math.tan(argValue);
            case "ln":
                if (argValue <= 0) {
                    throw new IllegalArgumentException(
                        "ln argument must be positive"
                    );
                }
                return Math.log(argValue);
            case "log":
                if (argValue <= 0) {
                    throw new IllegalArgumentException(
                        "log argument must be positive"
                    );
                }
                return Math.log10(argValue);
            case "exp":
                return Math.exp(argValue);
            case "sqrt":
                if (argValue < 0) {
                    throw new IllegalArgumentException(
                        "sqrt argument must be non-negative"
                    );
                }
                return Math.sqrt(argValue);
            case "abs":
                return Math.abs(argValue);
            default:
                throw new UnsupportedOperationException(
                    "Unknown function: " + name
                );
        }
    }

    @Override
    public Expr copy() {
        return new FunctionExpr(name, arg.copy());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FunctionExpr)) return false;
        FunctionExpr other = (FunctionExpr) obj;
        return (
            Objects.equals(name, other.name) && Objects.equals(arg, other.arg)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, arg);
    }
}
