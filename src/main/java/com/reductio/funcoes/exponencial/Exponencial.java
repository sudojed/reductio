package com.reductio.funcoes.exponencial;

import com.reductio.funcoes.Function;

/**
 * Classe para funções exponenciais
 * Representa funções do tipo f(x) = a * b^(cx + d) + e
 * Casos especiais: f(x) = a^x, f(x) = e^x, etc.
 */
public class Exponencial extends Function {

    private double a; // coeficiente multiplicativo
    private double base; // base da exponencial
    private double c; // coeficiente do expoente
    private double d; // termo aditivo no expoente
    private double e; // termo aditivo da função

    public Exponencial() {
        super();
    }

    public Exponencial(String expression, String variable) {
        super(expression, variable);
        extrairParametrosExponenciais(expression, variable);
    }

    public Exponencial(
        double a,
        double base,
        double c,
        double d,
        double e,
        String variable
    ) {
        super();
        this.a = a;
        this.base = base;
        this.c = c;
        this.d = d;
        this.e = e;
        this.variable = variable;
        this.expression = construirExpressao();
    }

    /**
     * Construtor simplificado para f(x) = a * base^x
     */
    public Exponencial(double a, double base, String variable) {
        this(a, base, 1, 0, 0, variable);
    }

    /**
     * Extrai os parâmetros da função exponencial
     */
    private void extrairParametrosExponenciais(
        String expression,
        String variable
    ) {
        // Implementação simplificada - detecta padrões básicos
        String expr = expression.replaceAll("\\s+", "");

        // Valores padrão
        this.a = 1;
        this.base = Math.E; // e como base padrão
        this.c = 1;
        this.d = 0;
        this.e = 0;

        // Detecta padrões como "2^x", "e^x", "3*2^x", etc.
        if (expr.contains("^")) {
            // Padrão básico: base^expoente
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "(\\d*\\.?\\d*)\\*?([eE\\d]+)\\^\\(?(.*?)\\)?"
            );
            java.util.regex.Matcher matcher = pattern.matcher(expr);

            if (matcher.find()) {
                String coefStr = matcher.group(1);
                String baseStr = matcher.group(2);
                String expoenteStr = matcher.group(3);

                // Coeficiente multiplicativo
                if (!coefStr.isEmpty()) {
                    this.a = Double.parseDouble(coefStr);
                }

                // Base
                if (baseStr.equals("e") || baseStr.equals("E")) {
                    this.base = Math.E;
                } else {
                    this.base = Double.parseDouble(baseStr);
                }

                // Expoente (assume formato cx + d)
                if (expoenteStr.contains(variable)) {
                    extrairCoeficientesExpoente(expoenteStr, variable);
                }
            }
        }
    }

    /**
     * Extrai coeficientes c e d do expoente (cx + d)
     */
    private void extrairCoeficientesExpoente(String expoente, String variable) {
        // Remove espaços
        String exp = expoente.replaceAll("\\s+", "");

        // Procura por padrão cx
        java.util.regex.Pattern patternX = java.util.regex.Pattern.compile(
            "([+-]?\\d*)" + variable
        );
        java.util.regex.Matcher matcherX = patternX.matcher(exp);

        if (matcherX.find()) {
            String coefStr = matcherX.group(1);
            if (coefStr.isEmpty() || coefStr.equals("+")) {
                this.c = 1;
            } else if (coefStr.equals("-")) {
                this.c = -1;
            } else {
                this.c = Double.parseDouble(coefStr);
            }
        }

        // Remove termo cx para encontrar d
        String semX = exp.replaceAll("([+-]?\\d*)" + variable, "");

        // Procura por termo constante
        java.util.regex.Pattern patternConst = java.util.regex.Pattern.compile(
            "([+-]?\\d+(?:\\.\\d+)?)"
        );
        java.util.regex.Matcher matcherConst = patternConst.matcher(semX);

        if (matcherConst.find()) {
            this.d = Double.parseDouble(matcherConst.group(1));
        }
    }

    /**
     * Constrói a expressão a partir dos parâmetros
     */
    private String construirExpressao() {
        StringBuilder sb = new StringBuilder();

        // Coeficiente multiplicativo
        if (a != 1) {
            sb.append(a).append(" * ");
        }

        // Base
        if (base == Math.E) {
            sb.append("e");
        } else {
            sb.append(base);
        }

        sb.append("^");

        // Expoente
        if (c != 1 || d != 0) {
            sb.append("(");

            if (c == 1) {
                sb.append(variable);
            } else if (c == -1) {
                sb.append("-").append(variable);
            } else {
                sb.append(c).append(variable);
            }

            if (d > 0) {
                sb.append(" + ").append(d);
            } else if (d < 0) {
                sb.append(" - ").append(Math.abs(d));
            }

            sb.append(")");
        } else {
            sb.append(variable);
        }

        // Termo aditivo
        if (e > 0) {
            sb.append(" + ").append(e);
        } else if (e < 0) {
            sb.append(" - ").append(Math.abs(e));
        }

        return sb.toString();
    }

    @Override
    public String getType() {
        return "Função Exponencial";
    }

    @Override
    public String getDomain() {
        return "ℝ (todos os números reais)";
    }

    @Override
    public String getRange() {
        if (c > 0) {
            if (a > 0) {
                return "(" + e + ", +∞)";
            } else {
                return "(-∞, " + e + ")";
            }
        } else {
            if (a > 0) {
                return "(" + e + ", +∞)";
            } else {
                return "(-∞, " + e + ")";
            }
        }
    }

    @Override
    public double evaluate(double x) {
        double expoente = c * x + d;
        return a * Math.pow(base, expoente) + e;
    }

    /**
     * Verifica se a função é crescente
     */
    public boolean isCrescente() {
        if (base > 1) {
            return (a > 0 && c > 0) || (a < 0 && c < 0);
        } else if (base > 0 && base < 1) {
            return (a > 0 && c < 0) || (a < 0 && c > 0);
        }
        return false;
    }

    /**
     * Verifica se a função é decrescente
     */
    public boolean isDecrescente() {
        if (base > 1) {
            return (a > 0 && c < 0) || (a < 0 && c > 0);
        } else if (base > 0 && base < 1) {
            return (a > 0 && c > 0) || (a < 0 && c < 0);
        }
        return false;
    }

    /**
     * Calcula o intercepto com o eixo y
     */
    public double getInterceptoY() {
        return evaluate(0);
    }

    /**
     * Encontra a assíntota horizontal
     */
    public double getAssintotaHorizontal() {
        if (c > 0) {
            return e; // quando x → -∞
        } else if (c < 0) {
            return e; // quando x → +∞
        }
        return Double.NaN;
    }

    /**
     * Calcula a taxa de crescimento instantânea (derivada)
     */
    public double calcularDerivada(double x) {
        double expoente = c * x + d;
        return a * c * Math.log(base) * Math.pow(base, expoente);
    }

    /**
     * Calcula o logaritmo da função (função inversa quando possível)
     */
    public double calcularLogaritmo(double y) {
        if (a == 0 || base <= 0 || base == 1) {
            throw new IllegalArgumentException(
                "Parâmetros inválidos para calcular logaritmo"
            );
        }

        double argumento = (y - e) / a;
        if (argumento <= 0) {
            throw new IllegalArgumentException(
                "Argumento do logaritmo deve ser positivo"
            );
        }

        return (Math.log(argumento) / Math.log(base) - d) / c;
    }

    /**
     * Verifica se é uma função exponencial natural (base e)
     */
    public boolean isExponencialNatural() {
        return Math.abs(base - Math.E) < 1e-10;
    }

    /**
     * Retorna informações completas sobre a função
     */
    public String getAnaliseCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("Análise da Função Exponencial:\n");
        sb.append("Expressão: ").append(expression).append("\n");
        sb.append("Base: ").append(base).append("\n");
        sb.append("Domínio: ").append(getDomain()).append("\n");
        sb.append("Imagem: ").append(getRange()).append("\n");
        sb.append("Intercepto Y: ").append(getInterceptoY()).append("\n");
        sb
            .append("Comportamento: ")
            .append(
                isCrescente()
                    ? "Crescente"
                    : isDecrescente()
                        ? "Decrescente"
                        : "Constante"
            )
            .append("\n");

        double assintota = getAssintotaHorizontal();
        if (!Double.isNaN(assintota)) {
            sb
                .append("Assíntota horizontal: y = ")
                .append(assintota)
                .append("\n");
        }

        if (isExponencialNatural()) {
            sb.append("Tipo: Exponencial natural (base e)\n");
        }

        return sb.toString();
    }

    // Getters específicos
    public double getCoeficienteA() {
        return a;
    }

    public double getBase() {
        return base;
    }

    public double getCoeficienteC() {
        return c;
    }

    public double getCoeficienteD() {
        return d;
    }

    public double getCoeficienteE() {
        return e;
    }

    @Override
    public String toString() {
        return String.format(
            "Exponencial[a=%.2f, base=%.2f, c=%.2f, d=%.2f, e=%.2f, expressão=%s]",
            a,
            base,
            c,
            d,
            e,
            expression
        );
    }
}
