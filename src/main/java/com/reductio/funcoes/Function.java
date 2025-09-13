package com.reductio.funcoes;

import com.reductio.BinaryOp;
import com.reductio.Constant;
import com.reductio.Expr;
import com.reductio.FunctionExpr;
import com.reductio.Variable;
import com.reductio.funcoes.exponencial.Exponencial;
import com.reductio.funcoes.logaritmica.Logaritmica;
import com.reductio.funcoes.polinomial.Cubica;
import com.reductio.funcoes.polinomial.Linear;
import com.reductio.funcoes.polinomial.Polinomial;
import com.reductio.funcoes.polinomial.Quadrada;
import com.reductio.funcoes.trigonometrica.Trigonometrica;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract base class for mathematical functions.
 * Provides automatic identification and instantiation of specific function types.
 *
 * @author Reductio Team
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Function {

    protected final String expression;
    protected final String variable;

    /**
     * Creates a new function with the given expression and variable.
     *
     * @param expression the mathematical expression
     * @param variable the primary variable name
     */
    protected Function(String expression, String variable) {
        this.expression = Objects.requireNonNull(
            expression,
            "Expression cannot be null"
        );
        this.variable = Objects.requireNonNull(
            variable,
            "Variable cannot be null"
        );
    }

    /**
     * Protected constructor for subclasses.
     */
    protected Function() {
        this.expression = "";
        this.variable = "x";
    }

    /**
     * Identifies the type of function from an expression and returns the appropriate instance.
     *
     * @param expr the mathematical expression to analyze
     * @param variable the primary variable name
     * @return a specific Function subclass instance or null if not identified
     * @throws IllegalArgumentException if parameters are invalid
     */
    public static Function identify(Expr expr, String variable) {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
        if (variable == null || variable.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Variable cannot be null or empty"
            );
        }

        String normalizedVariable = variable.trim();

        Function polynomial = identifyPolynomial(expr, normalizedVariable);
        if (polynomial != null) {
            return polynomial;
        }

        Function exponential = identifyExponential(expr, normalizedVariable);
        if (exponential != null) {
            return exponential;
        }

        Function logarithmic = identifyLogarithmic(expr, normalizedVariable);
        if (logarithmic != null) {
            return logarithmic;
        }

        Function trigonometric = identifyTrigonometric(
            expr,
            normalizedVariable
        );
        if (trigonometric != null) {
            return trigonometric;
        }

        return null;
    }

    private static Function identifyPolynomial(Expr expr, String variable) {
        int degree = getPolynomialDegree(expr, variable);

        if (degree < 0) {
            return null;
        }

        String exprStr = expr.show();

        switch (degree) {
            case 1:
                return new Linear(exprStr, variable);
            case 2:
                return new Quadrada(exprStr, variable);
            case 3:
                return new Cubica(exprStr, variable);
            default:
                return new Polinomial(exprStr, variable);
        }
    }

    private static Function identifyExponential(Expr expr, String variable) {
        if (containsExponential(expr, variable)) {
            return new Exponencial(expr.show(), variable);
        }
        return null;
    }

    private static Function identifyLogarithmic(Expr expr, String variable) {
        if (containsLogarithm(expr)) {
            return new Logaritmica(expr.show(), variable);
        }
        return null;
    }

    private static Function identifyTrigonometric(Expr expr, String variable) {
        if (containsTrigonometric(expr)) {
            return new Trigonometrica(expr.show(), variable);
        }
        return null;
    }

    private static int getPolynomialDegree(Expr expr, String variable) {
        return getMaxDegree(expr, variable);
    }

    private static int getMaxDegree(Expr expr, String variable) {
        if (expr instanceof Variable) {
            Variable var = (Variable) expr;
            return var.hasName(variable) ? 1 : 0;
        }

        if (expr instanceof Constant) {
            return 0;
        }

        if (expr instanceof BinaryOp) {
            BinaryOp binOp = (BinaryOp) expr;

            if ("^".equals(binOp.getOperator())) {
                if (
                    binOp.getLeft() instanceof Variable &&
                    ((Variable) binOp.getLeft()).hasName(variable) &&
                    binOp.getRight() instanceof Constant
                ) {
                    return (int) ((Constant) binOp.getRight()).getValue();
                }
            }

            if (
                "+".equals(binOp.getOperator()) ||
                "-".equals(binOp.getOperator())
            ) {
                return Math.max(
                    getMaxDegree(binOp.getLeft(), variable),
                    getMaxDegree(binOp.getRight(), variable)
                );
            }

            if ("*".equals(binOp.getOperator())) {
                return (
                    getMaxDegree(binOp.getLeft(), variable) +
                    getMaxDegree(binOp.getRight(), variable)
                );
            }
        }

        if (expr instanceof FunctionExpr) {
            return -1;
        }

        return 0;
    }

    private static boolean containsExponential(Expr expr, String variable) {
        if (expr instanceof BinaryOp) {
            BinaryOp binOp = (BinaryOp) expr;

            if ("^".equals(binOp.getOperator())) {
                return containsVariable(binOp.getRight(), variable);
            }

            return (
                containsExponential(binOp.getLeft(), variable) ||
                containsExponential(binOp.getRight(), variable)
            );
        }

        return false;
    }

    private static boolean containsLogarithm(Expr expr) {
        if (expr instanceof FunctionExpr) {
            FunctionExpr func = (FunctionExpr) expr;
            return "ln".equals(func.name) || "log".equals(func.name);
        }

        if (expr instanceof BinaryOp) {
            BinaryOp binOp = (BinaryOp) expr;
            return (
                containsLogarithm(binOp.getLeft()) ||
                containsLogarithm(binOp.getRight())
            );
        }

        return false;
    }

    private static boolean containsTrigonometric(Expr expr) {
        if (expr instanceof FunctionExpr) {
            FunctionExpr func = (FunctionExpr) expr;
            return (
                "sin".equals(func.name) ||
                "cos".equals(func.name) ||
                "tan".equals(func.name) ||
                "sec".equals(func.name) ||
                "csc".equals(func.name) ||
                "cot".equals(func.name)
            );
        }

        if (expr instanceof BinaryOp) {
            BinaryOp binOp = (BinaryOp) expr;
            return (
                containsTrigonometric(binOp.getLeft()) ||
                containsTrigonometric(binOp.getRight())
            );
        }

        return false;
    }

    private static boolean containsVariable(Expr expr, String variable) {
        if (expr instanceof Variable) {
            return ((Variable) expr).hasName(variable);
        }

        if (expr instanceof BinaryOp) {
            BinaryOp binOp = (BinaryOp) expr;
            return (
                containsVariable(binOp.getLeft(), variable) ||
                containsVariable(binOp.getRight(), variable)
            );
        }

        if (expr instanceof FunctionExpr) {
            FunctionExpr func = (FunctionExpr) expr;
            return containsVariable(func.arg, variable);
        }

        return false;
    }

    /**
     * Returns the type description of this function.
     *
     * @return function type description
     */
    public abstract String getType();

    /**
     * Returns the domain of this function.
     *
     * @return domain description
     */
    public abstract String getDomain();

    /**
     * Returns the range (codomain) of this function.
     *
     * @return range description
     */
    public abstract String getRange();

    /**
     * Evaluates the function at the given value.
     *
     * @param value the input value
     * @return the function output
     * @throws IllegalArgumentException if value is outside domain
     */
    public abstract double evaluate(double value);

    /**
     * Evaluates the function with multiple variables.
     *
     * @param variables map of variable names to values
     * @return the function output
     * @throws IllegalArgumentException if required variables are missing
     */
    public double evaluate(Map<String, Double> variables) {
        if (variables == null || !variables.containsKey(variable)) {
            throw new IllegalArgumentException(
                "Value for variable '" + variable + "' not provided"
            );
        }
        return evaluate(variables.get(variable));
    }

    /**
     * Gets the mathematical expression as a string.
     *
     * @return the expression string
     */
    public final String getExpression() {
        return expression;
    }

    /**
     * Gets the primary variable name.
     *
     * @return the variable name
     */
    public final String getVariable() {
        return variable;
    }

    /**
     * Checks if this function is defined at the given value.
     *
     * @param value the input value
     * @return true if function is defined at this value
     */
    public boolean isDefinedAt(double value) {
        try {
            evaluate(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns a copy of this function.
     *
     * @return a new instance with the same properties
     */
    public abstract Function copy();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Function)) return false;
        Function function = (Function) obj;
        return (
            Objects.equals(expression, function.expression) &&
            Objects.equals(variable, function.variable)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, variable);
    }

    @Override
    public String toString() {
        return getType() + ": " + expression;
    }
}
