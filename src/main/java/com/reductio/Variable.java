package com.reductio;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a variable in mathematical expressions.
 *
 * @author Reductio Team
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Variable extends Expr {

    private final String name;

    /**
     * Creates a new variable with the specified name.
     *
     * @param name the variable name (e.g., "x", "y", "t")
     * @throws IllegalArgumentException if name is null or empty
     */
    public Variable(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Variable name cannot be null or empty"
            );
        }
        this.name = name.trim();
    }

    /**
     * Gets the name of this variable.
     *
     * @return the variable name
     */
    public String getName() {
        return name;
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
        return name;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        if (variables == null || !variables.containsKey(name)) {
            throw new IllegalArgumentException(
                "Value for variable '" + name + "' not provided"
            );
        }
        return variables.get(name);
    }

    @Override
    public Expr copy() {
        return new Variable(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Variable)) return false;
        Variable variable = (Variable) obj;
        return Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Checks if this variable has the same name as another variable.
     *
     * @param other the other variable
     * @return true if names are equal
     */
    public boolean hasSameName(Variable other) {
        return other != null && Objects.equals(name, other.name);
    }

    /**
     * Checks if this variable has the specified name.
     *
     * @param variableName the name to check
     * @return true if names match
     */
    public boolean hasName(String variableName) {
        return Objects.equals(name, variableName);
    }

    /**
     * Creates a new variable with a different name.
     *
     * @param newName the new variable name
     * @return new variable with the specified name
     */
    public Variable withName(String newName) {
        return new Variable(newName);
    }
}
