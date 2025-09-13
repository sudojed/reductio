# Reductio

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![JitPack](https://jitpack.io/v/sudojed/reductio.svg)](https://jitpack.io/#sudojed/reductio)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()

A powerful Java library for parsing, simplifying, and analyzing mathematical expressions with automatic function type identification.

## 🚀 Features

- **Expression Parsing**: Convert string expressions to abstract syntax trees
- **Automatic Simplification**: Reduce expressions to their simplest form
- **Function Identification**: Automatically detect and classify function types
- **Comprehensive Analysis**: Get detailed mathematical properties for each function type
- **Type-Safe API**: Strongly typed function classes with specific methods
- **Extensible Design**: Easy to add new function types and operations

## 📦 Installation

### GitHub Packages (Recommended)

Add the GitHub Packages repository to your build file:

#### Maven

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/sudojed/reductio</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.sudojed</groupId>
        <artifactId>reductio</artifactId>
        <version>1.1.0</version>
    </dependency>
</dependencies>
```

**Note:** You'll need to authenticate with GitHub Packages. Add to your `~/.m2/settings.xml`:

```xml
<servers>
    <server>
        <id>github</id>
        <username>YOUR_GITHUB_USERNAME</username>
        <password>YOUR_GITHUB_TOKEN</password>
    </server>
</servers>
```

#### Gradle

```gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/sudojed/reductio")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation 'com.github.sudojed:reductio:1.1.0'
}
```

### JitPack (Alternative)

Add the JitPack repository to your build file:

#### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.sudojed</groupId>
        <artifactId>reductio</artifactId>
        <version>v1.1.0</version>
    </dependency>
</dependencies>
```

#### Gradle

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.sudojed:reductio:v1.1.0'
}
```

#### SBT

```scala
resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "com.github.sudojed" % "reductio" % "v1.1.0"
```

#### Leiningen

```clojure
:repositories [["jitpack" "https://jitpack.io"]]
:dependencies [[com.github.sudojed/reductio "v1.1.0"]]
```

### GitHub Token Setup

To use GitHub Packages, you'll need a Personal Access Token:

1. Go to GitHub Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Generate a new token with `read:packages` scope
3. Set environment variables:
   ```bash
   export GITHUB_USERNAME=your-username
   export GITHUB_TOKEN=your-token
   ```
4. Or add to your Maven `settings.xml` or Gradle `gradle.properties`

### Quick Start with GitHub Packages

#### 1. Get a GitHub Token
```bash
# Create a token at: https://github.com/settings/tokens
# Select scope: read:packages
export GITHUB_USERNAME=your-username
export GITHUB_TOKEN=ghp_your_token_here
```

#### 2. Add Repository (Maven)
```xml
<!-- Add to pom.xml -->
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/sudojed/reductio</url>
    </repository>
</repositories>
```

#### 3. Configure Authentication
```xml
<!-- Add to ~/.m2/settings.xml -->
<servers>
    <server>
        <id>github</id>
        <username>${env.GITHUB_USERNAME}</username>
        <password>${env.GITHUB_TOKEN}</password>
    </server>
</servers>
```

#### 4. Add Dependency
```xml
<dependency>
    <groupId>com.github.sudojed</groupId>
    <artifactId>reductio</artifactId>
    <version>1.1.0</version>
</dependency>
```

#### 5. Use in Your Code
```java
import com.reductio.Parser;
import com.reductio.Expr;

// Parse and simplify expressions
Expr expr = Parser.parse("x^2 - 4x + 3");
Expr simplified = expr.simplify();
System.out.println(simplified.show());
```

### Manual Installation

1. Clone the repository:
```bash
git clone https://github.com/sudojed/reductio.git
```

2. Build from source:
```bash
cd reductio
mvn clean install
```

## 🏁 Quick Start

```java
import com.reductio.Parser;
import com.reductio.Expr;
import com.reductio.funcoes.Function;
import com.reductio.funcoes.polinomial.Quadrada;

// Parse a mathematical expression
Expr expr = Parser.parse("x^2 - 4x + 3");

// Simplify the expression
Expr simplified = expr.simplify();

// Automatically identify the function type
Function function = Function.identify(simplified, "x");

if (function instanceof Quadrada) {
    Quadrada quadratic = (Quadrada) function;
    
    // Get mathematical properties
    System.out.println("Type: " + quadratic.getType());
    System.out.println("Discriminant: " + quadratic.getDiscriminante());
    System.out.println("Vertex: " + Arrays.toString(quadratic.getVertice()));
    System.out.println("Roots: " + Arrays.toString(quadratic.encontrarRaizes()));
    
    // Evaluate at specific points
    System.out.println("f(2) = " + quadratic.evaluate(2.0));
}
```

## 📚 Supported Function Types

### Polynomial Functions

| Type | Pattern | Example | Features |
|------|---------|---------|----------|
| **Linear** | `ax + b` | `2x + 3` | Slope, intercepts, monotonicity |
| **Quadratic** | `ax² + bx + c` | `x^2 - 4x + 3` | Vertex, discriminant, roots, concavity |
| **Cubic** | `ax³ + bx² + cx + d` | `x^3 - 2x^2 + x + 1` | Critical points, inflection points |
| **Polynomial** | `aₙxⁿ + ... + a₁x + a₀` | `5x^4 - 3x^3 + 2x^2` | General polynomial analysis |

### Exponential Functions

| Pattern | Example | Features |
|---------|---------|----------|
| `a * b^(cx + d) + e` | `2^x`, `e^(2x+1)` | Base, growth rate, asymptotes |

### Logarithmic Functions

| Pattern | Example | Features |
|---------|---------|----------|
| `a * log_b(cx + d) + e` | `ln(x)`, `log(2x+1)` | Base, domain restrictions, asymptotes |

### Trigonometric Functions

| Pattern | Example | Features |
|---------|---------|----------|
| `a * trig(bx + c) + d` | `sin(x)`, `2*cos(3x+1)` | Period, amplitude, phase shift |

## 🔧 API Reference

### Core Classes

#### `Parser`
```java
// Parse expression from string
Expr expr = Parser.parse("2x + 3");

// Validate expression
boolean isValid = Parser.isValid("invalid expression");

// Get supported functions
Set<String> functions = Parser.getSupportedFunctions();
```

#### `Expr` (Abstract Base)
```java
// Simplify expression
Expr simplified = expr.simplify();

// Get string representation
String str = expr.show();

// Evaluate with variables
Map<String, Double> vars = Map.of("x", 2.0);
double result = expr.evaluate(vars);
```

#### `Function` (Abstract Base)
```java
// Automatic identification
Function func = Function.identify(expr, "x");

// Common properties
String type = func.getType();
String domain = func.getDomain();
String range = func.getRange();
double value = func.evaluate(2.0);
```

### Specific Function Types

#### Linear Functions
```java
Linear linear = new Linear("2x + 3", "x");

// Properties
double slope = linear.getCoeficienteAngular();
double intercept = linear.getCoeficienteLinear();
boolean increasing = linear.isCrescente();
double root = linear.encontrarRaiz();
```

#### Quadratic Functions
```java
Quadrada quadratic = new Quadrada("x^2 - 4x + 3", "x");

// Properties
double discriminant = quadratic.getDiscriminante();
double[] vertex = quadratic.getVertice();
double[] roots = quadratic.encontrarRaizes();
boolean concaveUp = quadratic.isConcavidadeParaCima();
```

#### Exponential Functions
```java
Exponencial exponential = new Exponencial("2^x", "x");

// Properties
double base = exponential.getBase();
boolean increasing = exponential.isCrescente();
double asymptote = exponential.getAssintotaHorizontal();
```

#### Logarithmic Functions
```java
Logaritmica logarithmic = new Logaritmica("ln(x)", "x");

// Properties
double base = logarithmic.getBase();
boolean isNatural = logarithmic.isLogaritmoNatural();
double asymptote = logarithmic.getAssintotaVertical();
```

#### Trigonometric Functions
```java
Trigonometrica trig = new Trigonometrica("sin(x)", "x");

// Properties
double period = trig.getPeriodo();
double amplitude = trig.getAmplitude();
double frequency = trig.getFrequencia();
boolean isOdd = trig.isFuncaoImpar();
```

## 📖 Examples

### Basic Expression Parsing

```java
// Simple arithmetic
Expr expr1 = Parser.parse("2 + 3 * 4");
System.out.println(expr1.simplify().show()); // "14"

// With variables
Expr expr2 = Parser.parse("2x + 3y - 5x");
System.out.println(expr2.simplify().show()); // "-3x + 3y"

// Functions
Expr expr3 = Parser.parse("sin(x) + cos(x)");
Map<String, Double> vars = Map.of("x", Math.PI/4);
System.out.println(expr3.evaluate(vars)); // ~1.414
```

### Function Analysis

```java
// Quadratic analysis
Function quad = Function.identify(Parser.parse("x^2 - 6x + 8"), "x");
if (quad instanceof Quadrada) {
    Quadrada q = (Quadrada) quad;
    System.out.println("Complete analysis:");
    System.out.println(q.getAnaliseCompleta());
}

// Trigonometric analysis
Function trig = Function.identify(Parser.parse("2*sin(3x + 1)"), "x");
if (trig instanceof Trigonometrica) {
    Trigonometrica t = (Trigonometrica) trig;
    System.out.println("Period: " + t.getPeriodo());
    System.out.println("Amplitude: " + t.getAmplitude());
    System.out.println("Phase shift: " + t.getDeslocamentoFase());
}
```

### Advanced Usage

```java
// Step-by-step simplification
Map<Integer, String> steps = new LinkedHashMap<>();
int[] counter = {0};
Expr expr = Parser.parse("(x + 2)^2 - (x + 2)");
Expr result = expr.simplify(steps, counter);

steps.forEach((step, expression) -> 
    System.out.println("Step " + step + ": " + expression));
```

## 🏗️ Architecture

### Class Hierarchy

```
Expr (abstract)
├── Constant
├── Variable
├── BinaryOp
└── FunctionExpr

Function (abstract)
├── Polinomial
│   ├── Linear
│   ├── Quadrada
│   └── Cubica
├── Exponencial
├── Logaritmica
└── Trigonometrica
```

### Design Patterns

- **Strategy Pattern**: Different simplification strategies for expression types
- **Factory Pattern**: Automatic function type identification and instantiation
- **Visitor Pattern**: Expression tree traversal and analysis
- **Builder Pattern**: Complex expression construction

## ⚡ Performance

- **Parsing**: O(n) time complexity for expression length
- **Simplification**: O(n log n) for most expressions
- **Evaluation**: O(1) for constants, O(n) for complex expressions
- **Memory**: Minimal overhead with immutable expression trees

## 🧪 Testing

Run the test suite:

```bash
mvn test
```

Example test execution:

```java
// Comprehensive testing
@Test
public void testFunctionIdentification() {
    assertEquals(Linear.class, 
        Function.identify(Parser.parse("2x + 3"), "x").getClass());
    
    assertEquals(Quadrada.class, 
        Function.identify(Parser.parse("x^2 - 1"), "x").getClass());
    
    assertEquals(Exponencial.class, 
        Function.identify(Parser.parse("2^x"), "x").getClass());
}
```

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Development Setup

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes and add tests
4. Ensure all tests pass: `mvn test`
5. Commit your changes: `git commit -m 'Add amazing feature'`
6. Push to the branch: `git push origin feature/amazing-feature`
7. Open a Pull Request

### Code Standards

- Follow Java naming conventions
- Add JavaDoc for public APIs
- Include unit tests for new features
- Maintain backward compatibility

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: [Wiki](https://github.com/sudojed/reductio/wiki)
- **Issues**: [GitHub Issues](https://github.com/sudojed/reductio/issues)
- **Discussions**: [GitHub Discussions](https://github.com/sudojed/reductio/discussions)
- **Email**: abnerjaredejede@gmail.com

## 🗺️ Roadmap

### Version 1.1.0 ✅
- [x] Enhanced JitPack integration and build optimization
- [x] Improved test coverage and code quality
- [x] Better error handling and validation
- [x] Cleaned up version control artifacts

### Version 1.2.0
- [ ] Hyperbolic functions (sinh, cosh, tanh)
- [ ] Rational functions (P(x)/Q(x))
- [ ] Implicit function support
- [ ] Enhanced error messages

### Version 1.3.0
- [ ] Symbolic differentiation
- [ ] Basic integration
- [ ] Function composition
- [ ] Matrix expressions

### Version 2.0.0
- [ ] Multi-variable support
- [ ] 3D function visualization
- [ ] Performance optimizations
- [ ] Plugin architecture

## 🙏 Acknowledgments

- Inspired by mathematical libraries like SymPy and Mathematica
- Built with modern Java practices and design patterns
- Community feedback and contributions

## 📊 Stats

![GitHub stars](https://img.shields.io/github/stars/sudojed/reductio?style=social)
![GitHub forks](https://img.shields.io/github/forks/sudojed/reductio?style=social)
![GitHub issues](https://img.shields.io/github/issues/sudojed/reductio)
![GitHub pull requests](https://img.shields.io/github/issues-pr/sudojed/reductio)

---

<div align="center">
  <sub>Built with ❤️ by Abner Lourenço</sub>
</div>