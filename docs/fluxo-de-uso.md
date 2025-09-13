# 🧮 Reductio - Fluxo de Uso Simples

Uma biblioteca Java poderosa para análise matemática automática que identifica e simplifica expressões de forma inteligente.

## 🚀 Instalação Rápida

### Maven
```xml
<dependency>
    <groupId>com.github.sudojed</groupId>
    <artifactId>reductio</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle
```gradle
implementation 'com.github.sudojed:reductio:1.0.0'
```

---

## 💡 Fluxo de Uso em 3 Passos

### 📝 **Passo 1: Parse da Expressão**
```java
import com.reductio.Parser;
import com.reductio.Expr;

// Converte string em expressão matemática
Expr expressao = Parser.parse("x^2 - 4x + 3");
```

### ⚡ **Passo 2: Simplificação Automática**
```java
// Simplifica automaticamente
Expr simplificada = expressao.simplify();
System.out.println(simplificada.show()); // "x^2 - 4x + 3"
```

### 🎯 **Passo 3: Identificação e Análise**
```java
import com.reductio.funcoes.Function;
import com.reductio.funcoes.polinomial.Quadrada;

// Identifica automaticamente o tipo de função
Function funcao = Function.identify(simplificada, "x");

if (funcao instanceof Quadrada) {
    Quadrada quadratica = (Quadrada) funcao;
    
    // Análise completa automática
    System.out.println("Tipo: " + quadratica.getType());
    System.out.println("Vértice: " + Arrays.toString(quadratica.getVertice()));
    System.out.println("Raízes: " + Arrays.toString(quadratica.encontrarRaizes()));
    System.out.println("f(2) = " + quadratica.evaluate(2.0));
}
```

---

## 🔧 Tipos de Função Suportados

| Tipo | Exemplo | Recursos Automáticos |
|------|---------|---------------------|
| **Linear** | `2x + 3` | Coeficiente angular, raiz, crescimento |
| **Quadrática** | `x² - 4x + 3` | Vértice, discriminante, raízes, concavidade |
| **Cúbica** | `x³ - 2x² + x` | Pontos críticos, inflexão |
| **Exponencial** | `2^x`, `e^x` | Base, taxa de crescimento, assíntotas |
| **Logarítmica** | `ln(x)`, `log(x)` | Base, domínio, assíntotas |
| **Trigonométrica** | `sin(x)`, `cos(x)` | Período, amplitude, fase |

---

## ⭐ Exemplo Completo

```java
public class ExemploReductio {
    public static void main(String[] args) {
        // 1. Parse
        Expr expr = Parser.parse("2*sin(3x + 1)");
        
        // 2. Simplifica
        Expr simplificada = expr.simplify();
        
        // 3. Identifica automaticamente
        Function funcao = Function.identify(simplificada, "x");
        
        if (funcao instanceof Trigonometrica) {
            Trigonometrica trig = (Trigonometrica) funcao;
            
            System.out.println("Função trigonométrica identificada!");
            System.out.println("Período: " + trig.getPeriodo());
            System.out.println("Amplitude: " + trig.getAmplitude());
            System.out.println("Deslocamento: " + trig.getDeslocamentoFase());
        }
    }
}
```

**Saída:**
```
Função trigonométrica identificada!
Período: 2.094395102393195
Amplitude: 2.0
Deslocamento: 1.0
```

---

## 🎨 Principais Vantagens

✅ **Identificação Automática** - Detecta o tipo de função sem intervenção manual  
✅ **API Type-Safe** - Classes específicas para cada tipo de função  
✅ **Análise Completa** - Propriedades matemáticas detalhadas  
✅ **Simplicidade** - Apenas 3 linhas para análise completa  
✅ **Extensível** - Fácil adição de novos tipos de função  

---

## 🌟 Por que usar Reductio?

**Antes:**
```java
// Código manual complexo para análise matemática
double a = extrairCoeficienteA(expressao);
double b = extrairCoeficienteB(expressao);
double discriminante = b*b - 4*a*c;
// ... 50+ linhas de código
```

**Com Reductio:**
```java
// Análise automática em 3 linhas
Function funcao = Function.identify(Parser.parse("x^2-4x+3"), "x");
Quadrada quad = (Quadrada) funcao;
System.out.println(quad.getAnaliseCompleta());
```

---

## 📊 Performance

- **Parse**: O(n) - Linear com tamanho da expressão
- **Simplificação**: O(n log n) - Otimizada para expressões complexas  
- **Avaliação**: O(1) - Constante para valores numéricos
- **Memória**: Mínima - Árvores de expressão imutáveis

---

## 🔗 Links Úteis

- **GitHub**: [sudojed/reductio](https://github.com/sudojed/reductio)
- **Documentação**: [Wiki completa](https://github.com/sudojed/reductio/wiki)
- **JitPack**: [Releases](https://jitpack.io/#sudojed/reductio)

---

<div align="center">

**Transforme análise matemática complexa em código simples** ⚡

*Desenvolvido com ❤️ para a comunidade Java*

[![GitHub stars](https://img.shields.io/github/stars/sudojed/reductio?style=social)](https://github.com/sudojed/reductio)

</div>