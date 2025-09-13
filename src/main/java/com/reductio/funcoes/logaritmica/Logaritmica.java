package com.reductio.funcoes.logaritmica;

import com.reductio.funcoes.Function;

/**
 * Classe para funções logarítmicas
 * Representa funções do tipo f(x) = a * log_b(cx + d) + e
 * Casos especiais: f(x) = ln(x), f(x) = log(x), etc.
 */
public class Logaritmica extends Function {

    private double a; // coeficiente multiplicativo
    private double base; // base do logaritmo
    private double c; // coeficiente do argumento
    private double d; // termo aditivo no argumento
    private double e; // termo aditivo da função

    public Logaritmica() {
        super();
    }

    public Logaritmica(String expression, String variable) {
        super(expression, variable);
        extrairParametrosLogaritmicos(expression, variable);
    }

    public Logaritmica(
        double a,
        double base,
        double c,
        double d,
        double e,
        String variable
    ) {
        super(construirExpressaoStatic(a, base, c, d, e, variable), variable);
        this.a = a;
        this.base = base;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    /**
     * Construtor simplificado para f(x) = a * log_b(x)
     */
    public Logaritmica(double a, double base, String variable) {
        this(a, base, 1, 0, 0, variable);
    }

    /**
     * Constrói a expressão a partir dos parâmetros (método estático)
     */
    private static String construirExpressaoStatic(
        double a,
        double base,
        double c,
        double d,
        double e,
        String variable
    ) {
        StringBuilder sb = new StringBuilder();

        // Coeficiente multiplicativo
        if (a != 1) {
            if (a == -1) {
                sb.append("-");
            } else {
                sb.append(a).append(" * ");
            }
        }

        // Função logarítmica
        if (Math.abs(base - Math.E) < 1e-10) {
            sb.append("ln");
        } else if (Math.abs(base - 10) < 1e-10) {
            sb.append("log");
        } else {
            sb.append("log_").append(base);
        }

        sb.append("(");

        // Argumento
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

        // Termo aditivo
        if (e > 0) {
            sb.append(" + ").append(e);
        } else if (e < 0) {
            sb.append(" - ").append(Math.abs(e));
        }

        return sb.toString();
    }

    /**
     * Extrai os parâmetros da função logarítmica
     */
    private void extrairParametrosLogaritmicos(
        String expression,
        String variable
    ) {
        // Implementação simplificada - detecta padrões básicos
        String expr = expression.replaceAll("\\s+", "");

        // Valores padrão
        this.a = 1;
        this.base = Math.E; // ln como padrão
        this.c = 1;
        this.d = 0;
        this.e = 0;

        // Detecta padrões como "ln(x)", "log(x)", "2*ln(3x+1)", etc.
        if (expr.contains("ln") || expr.contains("log")) {
            // Coeficiente multiplicativo antes do logaritmo
            java.util.regex.Pattern patternCoef =
                java.util.regex.Pattern.compile(
                    "([+-]?\\d*\\.?\\d*)\\*?(ln|log)"
                );
            java.util.regex.Matcher matcherCoef = patternCoef.matcher(expr);

            if (matcherCoef.find()) {
                String coefStr = matcherCoef.group(1);
                String funcao = matcherCoef.group(2);

                // Coeficiente multiplicativo
                if (
                    !coefStr.isEmpty() &&
                    !coefStr.equals("+") &&
                    !coefStr.equals("-")
                ) {
                    this.a = Double.parseDouble(coefStr);
                } else if (coefStr.equals("-")) {
                    this.a = -1;
                }

                // Base do logaritmo
                if (funcao.equals("ln")) {
                    this.base = Math.E;
                } else {
                    this.base = 10; // log base 10
                }
            }

            // Extrai o argumento do logaritmo
            java.util.regex.Pattern patternArg =
                java.util.regex.Pattern.compile("(ln|log)\\((.*?)\\)");
            java.util.regex.Matcher matcherArg = patternArg.matcher(expr);

            if (matcherArg.find()) {
                String argumento = matcherArg.group(2);
                extrairCoeficientesArgumento(argumento, variable);
            }

            // Termo aditivo após o logaritmo
            String semLog = expr.replaceAll(
                "[+-]?\\d*\\.?\\d*\\*?(ln|log)\\(.*?\\)",
                ""
            );
            if (!semLog.isEmpty()) {
                java.util.regex.Pattern patternE =
                    java.util.regex.Pattern.compile("([+-]?\\d+(?:\\.\\d+)?)");
                java.util.regex.Matcher matcherE = patternE.matcher(semLog);

                if (matcherE.find()) {
                    this.e = Double.parseDouble(matcherE.group(1));
                }
            }
        }
    }

    /**
     * Extrai coeficientes c e d do argumento (cx + d)
     */
    private void extrairCoeficientesArgumento(
        String argumento,
        String variable
    ) {
        // Remove espaços
        String arg = argumento.replaceAll("\\s+", "");

        // Procura por padrão cx
        java.util.regex.Pattern patternX = java.util.regex.Pattern.compile(
            "([+-]?\\d*)" + variable
        );
        java.util.regex.Matcher matcherX = patternX.matcher(arg);

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
        String semX = arg.replaceAll("([+-]?\\d*)" + variable, "");

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
        return construirExpressaoStatic(a, base, c, d, e, getVariable());
    }

    @Override
    public Function copy() {
        return new Logaritmica(a, base, c, d, e, getVariable());
    }

    @Override
    public String getType() {
        return "Função Logarítmica";
    }

    @Override
    public String getDomain() {
        if (c > 0) {
            return "(" + (-d / c) + ", +∞)";
        } else if (c < 0) {
            return "(-∞, " + (-d / c) + ")";
        }
        return "Indefinido";
    }

    @Override
    public String getRange() {
        return "ℝ (todos os números reais)";
    }

    @Override
    public double evaluate(double x) {
        double argumento = c * x + d;
        if (argumento <= 0) {
            throw new IllegalArgumentException(
                "Argumento do logaritmo deve ser positivo"
            );
        }
        return a * (Math.log(argumento) / Math.log(base)) + e;
    }

    /**
     * Verifica se a função é crescente
     */
    public boolean isCrescente() {
        return (a > 0 && c > 0) || (a < 0 && c < 0);
    }

    /**
     * Verifica se a função é decrescente
     */
    public boolean isDecrescente() {
        return (a > 0 && c < 0) || (a < 0 && c > 0);
    }

    /**
     * Calcula o intercepto com o eixo y (quando existe)
     */
    public double getInterceptoY() {
        if (d > 0) {
            return evaluate(0);
        } else {
            throw new IllegalArgumentException(
                "Função não intercepta o eixo y"
            );
        }
    }

    /**
     * Encontra o intercepto com o eixo x (zero da função)
     */
    public double getInterceptoX() {
        // Resolve a * log_b(cx + d) + e = 0
        // log_b(cx + d) = -e/a
        // cx + d = b^(-e/a)
        // x = (b^(-e/a) - d) / c

        if (a == 0) {
            throw new IllegalArgumentException(
                "Coeficiente 'a' não pode ser zero"
            );
        }

        double exponente = -e / a;
        double valor = Math.pow(base, exponente);
        return (valor - d) / c;
    }

    /**
     * Encontra a assíntota vertical
     */
    public double getAssintotaVertical() {
        return -d / c;
    }

    /**
     * Calcula a derivada da função logarítmica
     */
    public double calcularDerivada(double x) {
        double argumento = c * x + d;
        if (argumento <= 0) {
            throw new IllegalArgumentException(
                "Argumento do logaritmo deve ser positivo"
            );
        }
        return (a * c) / (argumento * Math.log(base));
    }

    /**
     * Calcula a função exponencial inversa
     */
    public double calcularExponencial(double y) {
        // Resolve y = a * log_b(cx + d) + e para x
        // log_b(cx + d) = (y - e) / a
        // cx + d = b^((y - e) / a)
        // x = (b^((y - e) / a) - d) / c

        if (a == 0) {
            throw new IllegalArgumentException(
                "Coeficiente 'a' não pode ser zero"
            );
        }

        double exponente = (y - e) / a;
        double valor = Math.pow(base, exponente);
        return (valor - d) / c;
    }

    /**
     * Verifica se é um logaritmo natural
     */
    public boolean isLogaritmoNatural() {
        return Math.abs(base - Math.E) < 1e-10;
    }

    /**
     * Verifica se é um logaritmo decimal
     */
    public boolean isLogaritmoDecimal() {
        return Math.abs(base - 10) < 1e-10;
    }

    /**
     * Retorna informações completas sobre a função
     */
    public String getAnaliseCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("Análise da Função Logarítmica:\n");
        sb.append("Expressão: ").append(getExpression()).append("\n");
        sb.append("Base: ").append(base).append("\n");
        sb.append("Domínio: ").append(getDomain()).append("\n");
        sb.append("Imagem: ").append(getRange()).append("\n");

        try {
            sb.append("Intercepto Y: ").append(getInterceptoY()).append("\n");
        } catch (IllegalArgumentException e) {
            sb.append("Intercepto Y: Não existe\n");
        }

        sb.append("Intercepto X: ").append(getInterceptoX()).append("\n");
        sb
            .append("Assíntota vertical: x = ")
            .append(getAssintotaVertical())
            .append("\n");
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

        if (isLogaritmoNatural()) {
            sb.append("Tipo: Logaritmo natural (base e)\n");
        } else if (isLogaritmoDecimal()) {
            sb.append("Tipo: Logaritmo decimal (base 10)\n");
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
            "Logaritmica[a=%.2f, base=%.2f, c=%.2f, d=%.2f, e=%.2f, expressão=%s]",
            a,
            base,
            c,
            d,
            e,
            getExpression()
        );
    }
}
