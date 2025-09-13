# Reductio Usage Examples

This document provides comprehensive examples of how to use the Reductio library for mathematical expression parsing and analysis.

## Installation

First, add the JitPack repository and dependency to your project:

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.sudojed</groupId>
    <artifactId>reductio</artifactId>
    <version>v1.0.0</version>
</dependency>
```

### Gradle

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.sudojed:reductio:v1.0.0'
}
```

## Basic Usage Examples

### 1. Creating and Evaluating Simple Expressions

```java
import com.reductio.*;
import java.util.Map;

public class BasicExample {
    public static void main(String[] args) {
        // Create constants
        Constant five = new Constant(5.0);
        Constant three = new Constant(3.0);
        
        // Create variables
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        
        // Create binary operations
        BinaryOp addition = new BinaryOp("+", five, three);
        BinaryOp multiplication = new BinaryOp("*", x, y);
        
        // Simplify expressions
        Expr simplified = addition.simplify();
        System.out.println("5 + 3 = " + simplified.show()); // Output: "8"
        
        // Evaluate expressions with variables
        Map<String, Double> variables = Map.of("x", 2.0, "y", 4.0);
        double result = multiplication.evaluate(variables);
        System.out.println("x * y = " + result); // Output: 8.0
    }
}
```

### 2. Working with Function Expressions

```java
import com.reductio.*;

public class FunctionExample {
    public static void main(String[] args) {
        Variable x = new Variable("x");
        
        // Create trigonometric function: sin(x)
        FunctionExpr sinX = new FunctionExpr("sin", x);
        
        // Create logarithmic function: ln(x)
        FunctionExpr lnX = new FunctionExpr("ln", x);
        
        // Evaluate functions
        Map<String, Double> variables = Map.of("x", Math.PI/2);
        
        double sinResult = sinX.evaluate(variables);
        System.out.println("sin(π/2) = " + sinResult); // Output: 1.0
        
        variables = Map.of("x", Math.E);
        double lnResult = lnX.evaluate(variables);
        System.out.println("ln(e) = " + lnResult); // Output: 1.0
    }
}
```

## Advanced Function Analysis

### 3. Polynomial Functions

```java
import com.reductio.funcoes.polinomial.*;
import com.reductio.funcoes.Function;

public class PolynomialExample {
    public static void main(String[] args) {
        // Create a quadratic function: x² + 2x + 1
        double[] coefficients = {1.0, 2.0, 1.0}; // [constant, x, x²]
        Polinomial quadratic = new Polinomial(coefficients, "x");
        
        System.out.println("Function: " + quadratic.getExpression());
        System.out.println("Type: " + quadratic.getType());
        System.out.println("Domain: " + quadratic.getDomain());
        System.out.println("Range: " + quadratic.getRange());
        
        // Evaluate at different points
        System.out.println("f(0) = " + quadratic.evaluate(0.0)); // 1.0
        System.out.println("f(1) = " + quadratic.evaluate(1.0)); // 4.0
        System.out.println("f(-1) = " + quadratic.evaluate(-1.0)); // 0.0
        
        // Calculate derivative
        Polinomial derivative = quadratic.derivada();
        System.out.println("f'(x) = " + derivative.getExpression());
        
        // Test copying
        Function copy = quadratic.copy();
        System.out.println("Copy equals original: " + quadratic.equals(copy));
    }
}
```

### 4. Trigonometric Functions

```java
import com.reductio.funcoes.trigonometrica.Trigonometrica;

public class TrigonometricExample {
    public static void main(String[] args) {
        // Create sin(x) function
        Trigonometrica sinFunc = new Trigonometrica(1.0, "sin", 1.0, 0.0, 0.0, "x");
        
        System.out.println("=== Sine Function Analysis ===");
        System.out.println("Expression: " + sinFunc.getExpression());
        System.out.println("Type: " + sinFunc.getType());
        System.out.println("Domain: " + sinFunc.getDomain());
        System.out.println("Range: " + sinFunc.getRange());
        System.out.println("Period: " + sinFunc.getPeriodo());
        System.out.println("Amplitude: " + sinFunc.getAmplitude());
        
        // Evaluate at key points
        System.out.println("\n=== Evaluation ===");
        System.out.println("sin(0) = " + sinFunc.evaluate(0.0));
        System.out.println("sin(π/2) = " + sinFunc.evaluate(Math.PI/2));
        System.out.println("sin(π) = " + sinFunc.evaluate(Math.PI));
        System.out.println("sin(3π/2) = " + sinFunc.evaluate(3*Math.PI/2));
        
        // Create more complex function: 2*sin(3x + π/4)
        Trigonometrica complexSin = new Trigonometrica(2.0, "sin", 3.0, Math.PI/4, 0.0, "x");
        
        System.out.println("\n=== Complex Sine Function ===");
        System.out.println("Expression: " + complexSin.getExpression());
        System.out.println("Amplitude: " + complexSin.getAmplitude());
        System.out.println("Frequency: " + complexSin.getFrequencia());
        System.out.println("Period: " + complexSin.getPeriodo());
        System.out.println("Phase shift: " + complexSin.getDeslocamentoFase());
        
        // Check if function is odd/even
        System.out.println("Is odd function: " + sinFunc.isFuncaoImpar());
        System.out.println("Is even function: " + sinFunc.isFuncaoPar());
        
        // Calculate derivative
        System.out.println("Derivative at x=0: " + sinFunc.calcularDerivada(0.0));
    }
}
```

### 5. Exponential Functions

```java
import com.reductio.funcoes.exponencial.Exponencial;

public class ExponentialExample {
    public static void main(String[] args) {
        // Create e^x function
        Exponencial expFunc = new Exponencial(1.0, Math.E, 1.0, 0.0, 0.0, "x");
        
        System.out.println("=== Exponential Function Analysis ===");
        System.out.println("Expression: " + expFunc.getExpression());
        System.out.println("Type: " + expFunc.getType());
        System.out.println("Domain: " + expFunc.getDomain());
        System.out.println("Range: " + expFunc.getRange());
        System.out.println("Base: " + expFunc.getBase());
        System.out.println("Is natural exponential: " + expFunc.isExponencialNatural());
        
        // Evaluate at key points
        System.out.println("\n=== Evaluation ===");
        System.out.println("e^0 = " + expFunc.evaluate(0.0));
        System.out.println("e^1 = " + expFunc.evaluate(1.0));
        System.out.println("e^2 = " + expFunc.evaluate(2.0));
        System.out.println("e^(-1) = " + expFunc.evaluate(-1.0));
        
        // Create 2^x function
        Exponencial powerOf2 = new Exponencial(1.0, 2.0, 1.0, 0.0, 0.0, "x");
        
        System.out.println("\n=== Powers of 2 ===");
        System.out.println("Expression: " + powerOf2.getExpression());
        System.out.println("2^0 = " + powerOf2.evaluate(0.0));
        System.out.println("2^1 = " + powerOf2.evaluate(1.0));
        System.out.println("2^2 = " + powerOf2.evaluate(2.0));
        System.out.println("2^3 = " + powerOf2.evaluate(3.0));
        
        // Check behavior
        System.out.println("\n=== Function Properties ===");
        System.out.println("Is increasing: " + expFunc.isCrescente());
        System.out.println("Is decreasing: " + expFunc.isDecrescente());
        System.out.println("Y-intercept: " + expFunc.getInterceptoY());
        System.out.println("Horizontal asymptote: " + expFunc.getAssintotaHorizontal());
        
        // Calculate derivative
        System.out.println("Derivative at x=0: " + expFunc.calcularDerivada(0.0));
        System.out.println("Derivative at x=1: " + expFunc.calcularDerivada(1.0));
    }
}
```

### 6. Logarithmic Functions

```java
import com.reductio.funcoes.logaritmica.Logaritmica;

public class LogarithmicExample {
    public static void main(String[] args) {
        // Create natural logarithm ln(x)
        Logaritmica lnFunc = new Logaritmica(1.0, Math.E, 1.0, 0.0, 0.0, "x");
        
        System.out.println("=== Natural Logarithm Analysis ===");
        System.out.println("Expression: " + lnFunc.getExpression());
        System.out.println("Type: " + lnFunc.getType());
        System.out.println("Domain: " + lnFunc.getDomain());
        System.out.println("Range: " + lnFunc.getRange());
        System.out.println("Base: " + lnFunc.getBase());
        System.out.println("Is natural logarithm: " + lnFunc.isLogaritmoNatural());
        
        // Evaluate at key points
        System.out.println("\n=== Evaluation ===");
        System.out.println("ln(1) = " + lnFunc.evaluate(1.0));
        System.out.println("ln(e) = " + lnFunc.evaluate(Math.E));
        System.out.println("ln(e²) = " + lnFunc.evaluate(Math.E * Math.E));
        System.out.println("ln(√e) = " + lnFunc.evaluate(Math.sqrt(Math.E)));
        
        // Create log base 10
        Logaritmica log10 = new Logaritmica(1.0, 10.0, 1.0, 0.0, 0.0, "x");
        
        System.out.println("\n=== Base 10 Logarithm ===");
        System.out.println("Expression: " + log10.getExpression());
        System.out.println("Is decimal logarithm: " + log10.isLogaritmoDecimal());
        System.out.println("log₁₀(1) = " + log10.evaluate(1.0));
        System.out.println("log₁₀(10) = " + log10.evaluate(10.0));
        System.out.println("log₁₀(100) = " + log10.evaluate(100.0));
        System.out.println("log₁₀(1000) = " + log10.evaluate(1000.0));
        
        // Function properties
        System.out.println("\n=== Function Properties ===");
        System.out.println("Is increasing: " + lnFunc.isCrescente());
        System.out.println("Is decreasing: " + lnFunc.isDecrescente());
        System.out.println("X-intercept: " + lnFunc.getInterceptoX());
        System.out.println("Vertical asymptote: x = " + lnFunc.getAssintotaVertical());
        
        // Calculate derivative
        System.out.println("Derivative at x=1: " + lnFunc.calcularDerivada(1.0));
        System.out.println("Derivative at x=e: " + lnFunc.calcularDerivada(Math.E));
        
        // Inverse function (exponential)
        try {
            double invResult = lnFunc.calcularExponencial(2.0);
            System.out.println("Inverse of ln(x) = 2: x = " + invResult);
        } catch (Exception e) {
            System.out.println("Error calculating inverse: " + e.getMessage());
        }
    }
}
```

### 7. Function Identification

```java
import com.reductio.*;
import com.reductio.funcoes.Function;
import com.reductio.funcoes.trigonometrica.Trigonometrica;
import com.reductio.funcoes.exponencial.Exponencial;
import com.reductio.funcoes.logaritmica.Logaritmica;
import com.reductio.funcoes.polinomial.*;

public class FunctionIdentificationExample {
    public static void main(String[] args) {
        System.out.println("=== Automatic Function Identification ===\n");
        
        // Example 1: Identify polynomial functions
        identifyAndAnalyze(createLinearExpression(), "x");
        identifyAndAnalyze(createQuadraticExpression(), "x");
        identifyAndAnalyze(createCubicExpression(), "x");
        
        // Example 2: Identify trigonometric functions
        identifyAndAnalyze(createSinExpression(), "x");
        identifyAndAnalyze(createCosExpression(), "x");
        
        // Example 3: Identify exponential functions
        identifyAndAnalyze(createExponentialExpression(), "x");
        
        // Example 4: Identify logarithmic functions
        identifyAndAnalyze(createLogarithmicExpression(), "x");
    }
    
    private static void identifyAndAnalyze(Expr expression, String variable) {
        System.out.println("Expression: " + expression.show());
        
        Function identified = Function.identify(expression, variable);
        
        if (identified != null) {
            System.out.println("Identified as: " + identified.getType());
            System.out.println("Domain: " + identified.getDomain());
            System.out.println("Range: " + identified.getRange());
            System.out.println("f(1) = " + identified.evaluate(1.0));
            
            // Type-specific analysis
            if (identified instanceof Trigonometrica) {
                Trigonometrica trig = (Trigonometrica) identified;
                System.out.println("Period: " + trig.getPeriodo());
                System.out.println("Amplitude: " + trig.getAmplitude());
            } else if (identified instanceof Exponencial) {
                Exponencial exp = (Exponencial) identified;
                System.out.println("Base: " + exp.getBase());
                System.out.println("Is natural: " + exp.isExponencialNatural());
            } else if (identified instanceof Logaritmica) {
                Logaritmica log = (Logaritmica) identified;
                System.out.println("Base: " + log.getBase());
                System.out.println("Is natural: " + log.isLogaritmoNatural());
            } else if (identified instanceof Polinomial) {
                Polinomial poly = (Polinomial) identified;
                System.out.println("Degree: " + poly.getGrau());
            }
        } else {
            System.out.println("Could not identify function type");
        }
        
        System.out.println("---");
    }
    
    // Helper methods to create different expression types
    private static Expr createLinearExpression() {
        // 2x + 3
        Variable x = new Variable("x");
        Constant two = new Constant(2.0);
        Constant three = new Constant(3.0);
        BinaryOp twoX = new BinaryOp("*", two, x);
        return new BinaryOp("+", twoX, three);
    }
    
    private static Expr createQuadraticExpression() {
        // x² + 2x + 1
        Variable x = new Variable("x");
        Constant one = new Constant(1.0);
        Constant two = new Constant(2.0);
        BinaryOp xSquared = new BinaryOp("^", x, two);
        BinaryOp twoX = new BinaryOp("*", two, x);
        BinaryOp firstTerm = new BinaryOp("+", xSquared, twoX);
        return new BinaryOp("+", firstTerm, one);
    }
    
    private static Expr createCubicExpression() {
        // x³ + x² + x + 1
        Variable x = new Variable("x");
        Constant one = new Constant(1.0);
        Constant two = new Constant(2.0);
        Constant three = new Constant(3.0);
        BinaryOp xCubed = new BinaryOp("^", x, three);
        BinaryOp xSquared = new BinaryOp("^", x, two);
        BinaryOp term1 = new BinaryOp("+", xCubed, xSquared);
        BinaryOp term2 = new BinaryOp("+", term1, x);
        return new BinaryOp("+", term2, one);
    }
    
    private static Expr createSinExpression() {
        Variable x = new Variable("x");
        return new FunctionExpr("sin", x);
    }
    
    private static Expr createCosExpression() {
        Variable x = new Variable("x");
        return new FunctionExpr("cos", x);
    }
    
    private static Expr createExponentialExpression() {
        // e^x
        Variable x = new Variable("x");
        Variable e = new Variable("e");
        return new BinaryOp("^", e, x);
    }
    
    private static Expr createLogarithmicExpression() {
        Variable x = new Variable("x");
        return new FunctionExpr("ln", x);
    }
}
```

### 8. Error Handling

```java
import com.reductio.*;
import com.reductio.funcoes.logaritmica.Logaritmica;

public class ErrorHandlingExample {
    public static void main(String[] args) {
        System.out.println("=== Error Handling Examples ===\n");
        
        // Example 1: Invalid variable names
        try {
            Variable invalidVar = new Variable("");
            System.out.println("This shouldn't print");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
        
        // Example 2: Invalid operators
        try {
            Constant a = new Constant(1.0);
            Constant b = new Constant(2.0);
            BinaryOp invalidOp = new BinaryOp("invalid", a, b);
            System.out.println("This shouldn't print");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
        
        // Example 3: Logarithm with negative argument
        try {
            Logaritmica ln = new Logaritmica(1.0, Math.E, 1.0, 0.0, 0.0, "x");
            double result = ln.evaluate(-1.0);
            System.out.println("This shouldn't print: " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
        
        // Example 4: Missing variable in evaluation
        try {
            Variable x = new Variable("x");
            Variable y = new Variable("y");
            BinaryOp expr = new BinaryOp("+", x, y);
            
            Map<String, Double> incompleteVars = Map.of("x", 1.0);
            double result = expr.evaluate(incompleteVars);
            System.out.println("This shouldn't print: " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
        
        // Example 5: Division by zero
        try {
            Constant numerator = new Constant(5.0);
            Constant zero = new Constant(0.0);
            BinaryOp division = new BinaryOp("/", numerator, zero);
            
            double result = division.evaluate(Map.of());
            System.out.println("This shouldn't print: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
        
        System.out.println("\n=== Error Handling Complete ===");
    }
}
```

## Integration Examples

### 9. Complete Mathematical Analysis Tool

```java
import com.reductio.*;
import com.reductio.funcoes.Function;
import com.reductio.funcoes.trigonometrica.Trigonometrica;
import com.reductio.funcoes.exponencial.Exponencial;
import com.reductio.funcoes.logaritmica.Logaritmica;
import com.reductio.funcoes.polinomial.Polinomial;

public class MathematicalAnalysisTool {
    public static void main(String[] args) {
        System.out.println("=== Complete Mathematical Analysis Tool ===\n");
        
        // Analyze different types of functions
        analyzeTrigonometric();
        analyzeExponential();
        analyzeLogarithmic();
        analyzePolynomial();
    }
    
    private static void analyzeTrigonometric() {
        System.out.println("--- TRIGONOMETRIC FUNCTION ANALYSIS ---");
        
        // Create 2*sin(3x + π/4) + 1
        Trigonometrica func = new Trigonometrica(2.0, "sin", 3.0, Math.PI/4, 1.0, "x");
        
        System.out.println("Function: " + func.getExpression());
        System.out.println("Complete Analysis:");
        System.out.println(func.getAnaliseCompleta());
        
        // Find zeros in one period
        double[] zeros = func.encontrarZeros();
        System.out.println("Zeros in [0, period]: " + java.util.Arrays.toString(zeros));
        
        System.out.println();
    }
    
    private static void analyzeExponential() {
        System.out.println("--- EXPONENTIAL FUNCTION ANALYSIS ---");
        
        // Create 3*e^(2x) - 1
        Exponencial func = new Exponencial(3.0, Math.E, 2.0, 0.0, -1.0, "x");
        
        System.out.println("Function: " + func.getExpression());
        System.out.println("Complete Analysis:");
        System.out.println(func.getAnaliseCompleta());
        
        // Calculate inverse for a specific value
        try {
            double inverse = func.calcularLogaritmo(5.0);
            System.out.println("Inverse: if f(x) = 5, then x = " + inverse);
        } catch (Exception e) {
            System.out.println("Cannot calculate inverse: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void analyzeLogarithmic() {
        System.out.println("--- LOGARITHMIC FUNCTION ANALYSIS ---");
        
        // Create 2*ln(x + 1) + 3
        Logaritmica func = new Logaritmica(2.0, Math.E, 1.0, 1.0, 3.0, "x");
        
        System.out.println("Function: " + func.getExpression());
        System.out.println("Complete Analysis:");
        System.out.println(func.getAnaliseCompleta());
        
        System.out.println();
    }
    
    private static void analyzePolynomial() {
        System.out.println("--- POLYNOMIAL FUNCTION ANALYSIS ---");
        
        // Create x³ - 3x² + 2x + 1
        double[] coefficients = {1.0, 2.0, -3.0, 1.0}; // [constant, x, x², x³]
        Polinomial func = new Polinomial(coefficients, "x");
        
        System.out.println("Function: " + func.getExpression());
        System.out.println("Type: " + func.getType());
        System.out.println("Domain: " + func.getDomain());
        System.out.println("Range: " + func.getRange());
        
        // Calculate derivative
        Polinomial derivative = func.derivada();
        System.out.println("Derivative: " + derivative.getExpression());
        
        // Evaluate at multiple points
        System.out.println("\nEvaluation table:");
        for (int x = -2; x <= 2; x++) {
            double y = func.evaluate(x);
            double dy = derivative.evaluate(x);
            System.out.printf("f(%d) = %.2f, f'(%d) = %.2f%n", x, y, x, dy);
        }
        
        System.out.println();
    }
}
```

### 10. Performance Testing

```java
import com.reductio.*;
import com.reductio.funcoes.Function;
import com.reductio.funcoes.trigonometrica.Trigonometrica;

public class PerformanceTest {
    public static void main(String[] args) {
        System.out.println("=== Performance Testing ===\n");
        
        // Test expression creation performance
        testExpressionCreation();
        
        // Test evaluation performance
        testEvaluationPerformance();
        
        // Test copying performance
        testCopyingPerformance();
    }
    
    private static void testExpressionCreation() {
        System.out.println("--- Expression Creation Performance ---");
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10000; i++) {
            Variable x = new Variable("x");
            Constant c = new Constant(i);
            BinaryOp expr = new BinaryOp("+", x, c);
        }
        
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
        
        System.out.println("Created 10,000 expressions in " + duration + " ms");
        System.out.println("Average: " + (duration / 10000) + " ms per expression\n");
    }
    
    private static void testEvaluationPerformance() {
        System.out.println("--- Evaluation Performance ---");
        
        Trigonometrica sinFunc = new Trigonometrica(1.0, "sin", 1.0, 0.0, 0.0, "x");
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 100000; i++) {
            double x = i * 0.001; // Small increments
            double result = sinFunc.evaluate(x);
        }
        
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;
        
        System.out.println("Evaluated function 100,000 times in " + duration + " ms");
        System.out.println("Average: " + (duration / 100000) + " ms per evaluation\n");
    }
    
    private static void testCopyingPerformance() {
        System.out.println("--- Copying Performance ---");
        
        Trigonometrica originalFunc = new Trigonometrica(2.0, "cos", 3.0, Math.PI/4, 1.0, "x");
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10000; i++) {
            Function copy = originalFunc.copy();
        }
        
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;
        
        System.out.println("Copied function 10,000 times in " + duration + " ms");
        System.out.println("Average: " + (duration / 10000) + " ms per copy\n");
    }
}
```

## Best Practices

### 1. Error Handling
- Always catch `IllegalArgumentException` when creating expressions with user input
- Check domain restrictions before evaluating logarithmic and trigonometric functions
- Validate variable names and operator types

### 2. Performance
- Reuse `Variable` and `Constant` instances when possible
- Cache evaluation results for frequently used expressions
- Use appropriate data types for coefficients

### 3. Memory Management
- Use the `copy()` method when you need independent function instances
- Be aware that expressions create tree structures that reference other expressions

### 4. Type Safety
- Use `instanceof` checks when working with identified functions
- Cast to specific function types to access specialized methods
- Leverage the type system to catch errors at compile time

This completes the comprehensive usage examples for the Reductio library. Each example demonstrates different aspects of the library's functionality, from basic expression creation to advanced mathematical analysis.