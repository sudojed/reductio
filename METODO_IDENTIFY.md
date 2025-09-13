# Método Identify - Identificação Automática de Tipos de Função

## Visão Geral

O método `identify` é uma funcionalidade implementada no sistema Reductio que permite identificar automaticamente o tipo de uma função matemática a partir de sua expressão irredutível e instanciar o objeto correspondente da classe apropriada.

## Funcionamento

### Processo de Identificação

1. **Parse da Expressão**: A expressão matemática em string é convertida em uma árvore sintática abstrata (AST)
2. **Simplificação**: A expressão é simplificada para sua forma irredutível
3. **Análise de Padrões**: O método analisa a estrutura da expressão para identificar padrões característicos
4. **Instanciação**: Cria e retorna um objeto da classe específica do tipo de função identificado

### Assinatura do Método

```java
public static Function identify(Expr expr, String variable)
```

**Parâmetros:**
- `expr`: Expressão matemática já parseada e simplificada
- `variable`: Nome da variável principal (ex: "x", "t", etc.)

**Retorno:**
- Objeto `Function` da subclasse apropriada ou `null` se não identificado

## Tipos de Função Suportados

### 1. Funções Polinomiais

#### Linear (`Linear`)
- **Padrão**: `ax + b`
- **Exemplo**: `2x + 3`
- **Características**:
  - Grau 1
  - Coeficiente angular e linear
  - Gráfico em linha reta

#### Quadrática (`Quadrada`)
- **Padrão**: `ax² + bx + c`
- **Exemplo**: `x^2 - 4x + 3`
- **Características**:
  - Grau 2
  - Vértice, discriminante, raízes
  - Gráfico em parábola

#### Cúbica (`Cubica`)
- **Padrão**: `ax³ + bx² + cx + d`
- **Exemplo**: `x^3 - 2x^2 + x + 1`
- **Características**:
  - Grau 3
  - Pontos críticos, inflexão
  - Até 3 raízes reais

#### Polinomial Geral (`Polinomial`)
- **Padrão**: `aₙxⁿ + aₙ₋₁xⁿ⁻¹ + ... + a₁x + a₀`
- **Exemplo**: `5x^4 - 3x^3 + 2x^2 - x + 7`
- **Características**:
  - Grau n > 3
  - Coeficientes variáveis

### 2. Funções Exponenciais (`Exponencial`)

- **Padrão**: `a * b^(cx + d) + e`
- **Exemplos**: 
  - `2^x`
  - `e^x`
  - `3*2^(2x+1)`
- **Características**:
  - Base exponencial
  - Crescimento/decrescimento exponencial
  - Assíntotas horizontais

### 3. Funções Logarítmicas (`Logaritmica`)

- **Padrão**: `a * log_b(cx + d) + e`
- **Exemplos**:
  - `ln(x)`
  - `log(x)`
  - `2*ln(x+1)`
- **Características**:
  - Base logarítmica
  - Assíntotas verticais
  - Domínio restrito (argumento > 0)

### 4. Funções Trigonométricas (`Trigonometrica`)

- **Padrão**: `a * trig(bx + c) + d`
- **Exemplos**:
  - `sin(x)`
  - `2*cos(x)`
  - `tan(2x+1)`
- **Características**:
  - Periodicidade
  - Amplitude (para sin/cos)
  - Funções: sin, cos, tan, sec, csc, cot

## Exemplo de Uso

```java
// Parse e simplificação
Expr expr = Parser.parse("x^2 + 3x - 2");
Expr exprSimplificada = expr.simplify();

// Identificação do tipo
Function funcao = Function.identify(exprSimplificada, "x");

if (funcao instanceof Quadrada) {
    Quadrada quadratica = (Quadrada) funcao;
    System.out.println("Tipo: " + quadratica.getType());
    System.out.println("Discriminante: " + quadratica.getDiscriminante());
    System.out.println("Vértice: " + Arrays.toString(quadratica.getVertice()));
    System.out.println("f(2) = " + quadratica.evaluate(2));
}
```

## Algoritmo de Identificação

### 1. Análise de Grau Polinomial
```java
private static int getMaxDegree(Expr expr, String variable)
```
- Percorre a árvore sintática
- Identifica potências da variável
- Retorna o maior grau encontrado

### 2. Detecção de Padrões Específicos
```java
private static boolean containsExponential(Expr expr, String variable)
private static boolean containsLogarithm(Expr expr)
private static boolean containsTrigonometric(Expr expr)
```

### 3. Prioridade de Identificação
1. **Polinomiais** (por grau: linear → quadrática → cúbica → geral)
2. **Exponenciais** (variável no expoente)
3. **Logarítmicas** (funções ln, log)
4. **Trigonométricas** (funções sin, cos, tan, etc.)

## Limitações

### Casos Não Suportados
- **Funções Mistas**: `x^2 + sin(x)`
- **Produtos de Funções**: `ln(x) * e^x`
- **Múltiplas Variáveis**: `x + y`
- **Funções Compostas Complexas**: `sin(ln(x))`

### Comportamento para Casos Especiais
- Retorna `null` quando não consegue identificar
- Funções constantes podem ser identificadas como polinomiais de grau 0
- Expressões muito complexas podem não ser reconhecidas

## Funcionalidades das Classes

### Métodos Comuns
```java
public abstract String getType();           // Tipo da função
public abstract String getDomain();         // Domínio
public abstract String getRange();          // Imagem
public abstract double evaluate(double x);  // Avaliação em um ponto
```

### Métodos Específicos

#### Linear
- `getCoeficienteAngular()`, `getCoeficienteLinear()`
- `isCrescente()`, `isDecrescente()`
- `encontrarRaiz()`, `getEquacaoReduzida()`

#### Quadrada
- `getDiscriminante()`, `getVertice()`, `encontrarRaizes()`
- `isConcavidadeParaCima()`, `getAnaliseCompleta()`

#### Exponencial
- `getBase()`, `isCrescente()`, `calcularDerivada()`
- `isExponencialNatural()`, `getAssintotaHorizontal()`

#### Logaritmica
- `getBase()`, `isLogaritmoNatural()`, `getAssintotaVertical()`
- `calcularExponencial()`, `getInterceptoX()`

#### Trigonometrica
- `getPeriodo()`, `getAmplitude()`, `getFrequencia()`
- `isFuncaoPar()`, `isFuncaoImpar()`, `encontrarZeros()`

## Arquitetura

### Hierarquia de Classes
```
Function (abstract)
├── Polinomial
│   ├── Linear
│   ├── Quadrada
│   └── Cubica
├── Exponencial
├── Logaritmica
└── Trigonometrica
```

### Dependências
- `com.reductio.Expr` - Árvore sintática
- `com.reductio.Parser` - Parser de expressões
- `com.reductio.BinaryOp`, `Constant`, `Variable` - Nós da AST
- `com.reductio.FunctionExpr` - Expressões de função

## Testes e Exemplos

Execute os testes com:
```bash
java -cp src/main/java com.reductio.funcoes.FunctionIdentifierTest
java -cp src/main/java com.reductio.funcoes.ExemploIdentify
```

Os testes demonstram:
- Identificação correta de diferentes tipos
- Avaliação de funções em pontos específicos
- Análise de propriedades matemáticas
- Casos limite e especiais

## Benefícios

1. **Automatização**: Identificação automática sem intervenção manual
2. **Análise Matemática**: Cada classe fornece métodos específicos para análise
3. **Extensibilidade**: Fácil adição de novos tipos de função
4. **Integração**: Funciona com o sistema de simplificação existente
5. **Robustez**: Trata casos especiais e expressões complexas

## Possíveis Extensões

1. **Funções Hiperbólicas**: sinh, cosh, tanh
2. **Funções Racionais**: P(x)/Q(x)
3. **Funções por Partes**: Definidas por intervalos
4. **Funções Implícitas**: F(x,y) = 0
5. **Melhor Reconhecimento**: Padrões mais complexos