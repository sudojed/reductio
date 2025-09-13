package com.reductio;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a constant numerical value in mathematical expressions.
 *
 * @author Reductio Team
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Constant extends Expr {

    private final double value;

    /**
     * Creates a new constant with the specified value.
     *
     * @param value the numerical value
     */
    public Constant(double value) {
        this.value = value;
    }

    /**
     * Gets the numerical value of this constant.
     *
     * @return the numerical value
     */
    public double getValue() {
        return value;
    }

    @Override
    public Expr simplify() {
        return this;
    }

    @Override
    public Expr simplify(Map<Integer, String> steps, int[] stepCounter) {
        if (steps != null) {
            steps.put(stepCounter[0]++, this.show());
        }
        return this;
    }

    @Override
    public String show() {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return value;
    }

    @Override
    public Expr copy() {
        return new Constant(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Constant)) return false;
        Constant constant = (Constant) obj;
        return Double.compare(constant.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Checks if this constant represents zero.
     *
     * @return true if value is zero
     */
    public boolean isZero() {
        return Math.abs(value) < 1e-10;
    }

    /**
     * Checks if this constant represents one.
     *
     * @return true if value is one
     */
    public boolean isOne() {
        return Math.abs(value - 1.0) < 1e-10;
    }

    /**
     * Checks if this constant represents negative one.
     *
     * @return true if value is negative one
     */
    public boolean isNegativeOne() {
        return Math.abs(value + 1.0) < 1e-10;
    }

    /**
     * Checks if this constant is positive.
     *
     * @return true if value is positive
     */
    public boolean isPositive() {
        return value > 0;
    }

    /**
     * Checks if this constant is negative.
     *
     * @return true if value is negative
     */
    public boolean isNegative() {
        return value < 0;
    }

    /**
     * Returns the absolute value as a new constant.
     *
     * @return new constant with absolute value
     */
    public Constant abs() {
        return new Constant(Math.abs(value));
    }

    /**
     * Returns the negation as a new constant.
     *
     * @return new constant with negated value
     */
    public Constant negate() {
        return new Constant(-value);
    }
}
