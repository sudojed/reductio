package com.reductio.funcoes.polinomial;

/**
 * Classe para funções lineares
 * Representa funções do tipo f(x) = ax + b
 */
public class Linear extends Polinomial {

    private double a; // coeficiente angular
    private double b; // coeficiente linear

    public Linear() {
        super();
        this.grau = 1;
    }

    public Linear(String expression, String variable) {
        super(expression, variable);
        this.grau = 1;
        extrairCoeficientesLineares(expression, variable);
    }

    public Linear(double a, double b, String variable) {
        super(new double[] { b, a }, variable);
        this.a = a;
        this.b = b;
        this.grau = 1;
    }

    /**
     * Extrai os coeficientes a e b da expressão linear
     */
    private void extrairCoeficientesLineares(
        String expression,
        String variable
    ) {
        // Implementação simplificada
        // Remove espaços e normaliza a expressão
        String expr = expression.replaceAll("\\s+", "");

        // Inicializa coeficientes
        this.a = 0;
        this.b = 0;

        // Procura por padrões como "2x", "x", "-3x", etc.
        java.util.regex.Pattern patternX = java.util.regex.Pattern.compile(
            "([+-]?\\d*)" + variable + "(?![\\^\\d])"
        );
        java.util.regex.Matcher matcherX = patternX.matcher(expr);

        if (matcherX.find()) {
            String coefStr = matcherX.group(1);
            if (coefStr.isEmpty() || coefStr.equals("+")) {
                this.a = 1;
            } else if (coefStr.equals("-")) {
                this.a = -1;
            } else {
                this.a = Double.parseDouble(coefStr);
            }
        }

        // Remove o termo com x para encontrar o termo constante
        String semX = expr.replaceAll(
            "([+-]?\\d*)" + variable + "(?![\\^\\d])",
            ""
        );

        // Procura por termos constantes
        java.util.regex.Pattern patternConst = java.util.regex.Pattern.compile(
            "([+-]?\\d+(?:\\.\\d+)?)"
        );
        java.util.regex.Matcher matcherConst = patternConst.matcher(semX);

        if (matcherConst.find()) {
            this.b = Double.parseDouble(matcherConst.group(1));
        }

        // Atualiza o array de coeficientes
        this.coeficientes = new double[] { b, a };
    }

    @Override
    public String getType() {
        return "Função Linear";
    }

    @Override
    public String getDomain() {
        return "ℝ (todos os números reais)";
    }

    @Override
    public String getRange() {
        if (a != 0) {
            return "ℝ (todos os números reais)";
        } else {
            return "{" + b + "} (função constante)";
        }
    }

    @Override
    public double evaluate(double x) {
        return a * x + b;
    }

    /**
     * Encontra a raiz da função linear (zero da função)
     */
    public double encontrarRaiz() {
        if (a == 0) {
            throw new ArithmeticException(
                "Função constante não possui raiz (a menos que b = 0)"
            );
        }
        return -b / a;
    }

    /**
     * Calcula a taxa de variação (que é constante para funções lineares)
     */
    public double getTaxaVariacao() {
        return a;
    }

    /**
     * Verifica se a função é crescente
     */
    public boolean isCrescente() {
        return a > 0;
    }

    /**
     * Verifica se a função é decrescente
     */
    public boolean isDecrescente() {
        return a < 0;
    }

    /**
     * Verifica se a função é constante
     */
    public boolean isConstante() {
        return a == 0;
    }

    /**
     * Encontra o ponto onde a reta intercepta o eixo y
     */
    public double getInterceptoY() {
        return b;
    }

    /**
     * Encontra o ponto onde a reta intercepta o eixo x
     */
    public double getInterceptoX() {
        return encontrarRaiz();
    }

    /**
     * Calcula a distância entre dois pontos da reta
     */
    public double calcularDistancia(double x1, double x2) {
        double y1 = evaluate(x1);
        double y2 = evaluate(x2);
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Retorna a equação na forma y = ax + b
     */
    public String getEquacaoReduzida() {
        StringBuilder sb = new StringBuilder();
        sb.append("y = ");

        if (a == 1) {
            sb.append(variable);
        } else if (a == -1) {
            sb.append("-").append(variable);
        } else if (a != 0) {
            sb.append(a).append(variable);
        }

        if (b > 0 && a != 0) {
            sb.append(" + ").append(b);
        } else if (b < 0) {
            sb.append(" - ").append(Math.abs(b));
        } else if (b != 0 && a == 0) {
            sb.append(b);
        } else if (a == 0 && b == 0) {
            sb.append("0");
        }

        return sb.toString();
    }

    /**
     * Retorna a equação na forma geral ax - y + b = 0
     */
    public String getEquacaoGeral() {
        StringBuilder sb = new StringBuilder();

        if (a == 1) {
            sb.append(variable);
        } else if (a == -1) {
            sb.append("-").append(variable);
        } else {
            sb.append(a).append(variable);
        }

        sb.append(" - y");

        if (b > 0) {
            sb.append(" + ").append(b);
        } else if (b < 0) {
            sb.append(" - ").append(Math.abs(b));
        }

        sb.append(" = 0");

        return sb.toString();
    }

    // Getters específicos
    public double getCoeficienteAngular() {
        return a;
    }

    public double getCoeficienteLinear() {
        return b;
    }

    @Override
    public String toString() {
        return String.format(
            "Linear[a=%.2f, b=%.2f, expressão=%s]",
            a,
            b,
            expression
        );
    }
}
