package com.reductio;

import java.util.Map;

/**
 * Abstract base class representing mathematical expressions.
 *
 * @author Reductio Team
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Expr {

    /**
     * Simplifies the expression to its most reduced form.
     *
     * @return the simplified expression
     */
    public abstract Expr simplify();

    /**
     * Simplifies the expression with step tracking.
     *
     * @param steps map to store simplification steps
     * @param stepCounter array containing the current step number
     * @return the simplified expression
     */
    public abstract Expr simplify(
        Map<Integer, String> steps,
        int[] stepCounter
    );

    /**
     * Returns the string representation of the expression.
     *
     * @return string representation
     */
    public abstract String show();

    /**
     * Evaluates the expression for given variable values.
     *
     * @param variables map of variable names to values
     * @return the evaluated result
     * @throws IllegalArgumentException if required variables are missing
     */
    public abstract double evaluate(Map<String, Double> variables);

    /**
     * Returns a copy of this expression.
     *
     * @return a deep copy of the expression
     */
    public abstract Expr copy();

    /**
     * Checks if this expression equals another expression.
     *
     * @param other the other expression
     * @return true if expressions are equivalent
     */
    @Override
    public abstract boolean equals(Object other);

    /**
     * Returns the hash code for this expression.
     *
     * @return hash code
     */
    @Override
    public abstract int hashCode();

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return show();
    }
}
