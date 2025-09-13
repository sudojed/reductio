package com.reductio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.reductio.funcoes.Function;
import com.reductio.funcoes.trigonometrica.Trigonometrica;
import com.reductio.funcoes.logaritmica.Logaritmica;
import com.reductio.funcoes.exponencial.Exponencial;
import com.reductio.funcoes.polinomial.Polinomial;

/**
 * Basic test class to ensure the Reductio library works correctly.
 * This test validates core functionality and ensures Maven build passes.
 */
public class ReductioBasicTest {

    @Test
    @DisplayName("Test Basic Expression Creation")
    void testBasicExpressionCreation() {
        // Test Constant creation
        Constant constant = new Constant(5.0);
        assertNotNull(constant);
        assertEquals(5.0, constant.getValue());
        assertEquals("5", constant.show());

        // Test Variable creation
        Variable variable = new Variable("x");
        assertNotNull(variable);
        assertEquals("x", variable.getName());
        assertEquals("x", variable.show());

        // Test BinaryOp creation
        BinaryOp addition = new BinaryOp("+", constant, variable);
        assertNotNull(addition);
        assertEquals("+", addition.getOperator());
        assertEquals("5 + x", addition.show());
    }

    @Test
    @DisplayName("Test Function Expression Creation")
    void testFunctionExpressionCreation() {
        Variable x = new Variable("x");
        FunctionExpr sinExpr = new FunctionExpr("sin", x);

        assertNotNull(sinExpr);
        assertEquals("sin", sinExpr.name);
        assertEquals("sin(x)", sinExpr.show());
    }

    @Test
    @DisplayName("Test Trigonometric Function")
    void testTrigonometricFunction() {
        // Create a simple sin(x) function
        Trigonometrica sinFunc = new Trigonometrica(1.0, "sin", 1.0, 0.0, 0.0, "x");

        assertNotNull(sinFunc);
        assertEquals("Função Trigonométrica (sin)", sinFunc.getType());
        assertEquals("ℝ (todos os números reais)", sinFunc.getDomain());

        // Test evaluation at specific points
        assertEquals(0.0, sinFunc.evaluate(0.0), 1e-10);
        assertEquals(1.0, sinFunc.evaluate(Math.PI / 2), 1e-10);

        // Test copy method
        Function copy = sinFunc.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof Trigonometrica);
        assertTrue(sinFunc.equals(copy));
    }

    @Test
    @DisplayName("Test Logarithmic Function")
    void testLogarithmicFunction() {
        // Create a natural logarithm ln(x)
        Logaritmica lnFunc = new Logaritmica(1.0, Math.E, 1.0, 0.0, 0.0, "x");

        assertNotNull(lnFunc);
        assertEquals("Função Logarítmica", lnFunc.getType());
        assertEquals("ℝ (todos os números reais)", lnFunc.getRange());

        // Test evaluation at specific points
        assertEquals(0.0, lnFunc.evaluate(1.0), 1e-10);
        assertEquals(1.0, lnFunc.evaluate(Math.E), 1e-10);

        // Test copy method
        Function copy = lnFunc.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof Logaritmica);
        assertTrue(lnFunc.equals(copy));
    }

    @Test
    @DisplayName("Test Exponential Function")
    void testExponentialFunction() {
        // Create e^x function
        Exponencial expFunc = new Exponencial(1.0, Math.E, 1.0, 0.0, 0.0, "x");

        assertNotNull(expFunc);
        assertEquals("Função Exponencial", expFunc.getType());
        assertEquals("ℝ (todos os números reais)", expFunc.getDomain());

        // Test evaluation at specific points
        assertEquals(1.0, expFunc.evaluate(0.0), 1e-10);
        assertEquals(Math.E, expFunc.evaluate(1.0), 1e-10);

        // Test copy method
        Function copy = expFunc.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof Exponencial);
        assertTrue(expFunc.equals(copy));
    }

    @Test
    @DisplayName("Test Polynomial Function")
    void testPolynomialFunction() {
        // Create a simple polynomial: x^2 + 2x + 1
        double[] coefficients = {1.0, 2.0, 1.0}; // constant, x, x^2
        Polinomial polyFunc = new Polinomial(coefficients, "x");

        assertNotNull(polyFunc);
        assertTrue(polyFunc.getType().contains("Polinomial"));
        assertEquals("ℝ (todos os números reais)", polyFunc.getDomain());

        // Test evaluation: f(0) = 1, f(1) = 4, f(2) = 9
        assertEquals(1.0, polyFunc.evaluate(0.0), 1e-10);
        assertEquals(4.0, polyFunc.evaluate(1.0), 1e-10);
        assertEquals(9.0, polyFunc.evaluate(2.0), 1e-10);

        // Test copy method
        Function copy = polyFunc.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof Polinomial);
        assertTrue(polyFunc.equals(copy));
    }

    @Test
    @DisplayName("Test Expression Simplification")
    void testExpressionSimplification() {
        // Test simple constant folding
        Constant c1 = new Constant(2.0);
        Constant c2 = new Constant(3.0);
        BinaryOp addition = new BinaryOp("+", c1, c2);

        Expr simplified = addition.simplify();
        assertTrue(simplified instanceof Constant);
        assertEquals(5.0, ((Constant) simplified).getValue(), 1e-10);
    }

    @Test
    @DisplayName("Test Expression Copying")
    void testExpressionCopying() {
        Constant original = new Constant(42.0);
        Expr copy = original.copy();

        assertNotNull(copy);
        assertTrue(copy instanceof Constant);
        assertEquals(original.getValue(), ((Constant) copy).getValue());
        assertNotSame(original, copy); // Different objects
        assertTrue(original.equals(copy)); // But equal content
    }

    @Test
    @DisplayName("Test Expression Evaluation")
    void testExpressionEvaluation() {
        Variable x = new Variable("x");
        Constant c = new Constant(5.0);
        BinaryOp expr = new BinaryOp("*", c, x); // 5 * x

        java.util.Map<String, Double> variables = java.util.Map.of("x", 3.0);
        double result = expr.evaluate(variables);

        assertEquals(15.0, result, 1e-10);
    }

    @Test
    @DisplayName("Test Function String Representation")
    void testFunctionStringRepresentation() {
        Trigonometrica sinFunc = new Trigonometrica(2.0, "sin", 1.0, 0.0, 0.0, "x");
        String representation = sinFunc.toString();

        assertNotNull(representation);
        assertTrue(representation.contains("Trigonometrica"));
        assertTrue(representation.contains("sin"));
    }

    @Test
    @DisplayName("Test Error Handling")
    void testErrorHandling() {
        // Test invalid variable name
        assertThrows(IllegalArgumentException.class, () -> {
            new Variable("");
        });

        // Test null operator
        assertThrows(IllegalArgumentException.class, () -> {
            new BinaryOp(null, new Constant(1), new Constant(2));
        });

        // Test logarithm with negative argument
        Logaritmica lnFunc = new Logaritmica(1.0, Math.E, 1.0, 0.0, 0.0, "x");
        assertThrows(IllegalArgumentException.class, () -> {
            lnFunc.evaluate(-1.0);
        });
    }
}
