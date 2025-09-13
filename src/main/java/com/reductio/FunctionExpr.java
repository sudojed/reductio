package com.reductio;

import java.util.*;

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
                b.op.equals("^") &&
                b.left instanceof Variable &&
                ((Variable) b.left).name.equals("e")
            ) {
                Expr result = b.right.simplify(steps, stepCounter);
                steps.put(stepCounter[0]++, result.show());
                return result;
            }
        }

        // sin(0) = 0, cos(0) = 1, etc.
        if (A instanceof Constant) {
            double value = ((Constant) A).value;
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
}
