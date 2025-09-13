package com.reductio.funcoes.polinomial;

/**
 * Classe para funções quadráticas
 * Representa funções do tipo f(x) = ax² + bx + c
 */
public class Quadrada extends Polinomial {

    private double a; // coeficiente de x²
    private double b; // coeficiente de x
    private double c; // termo constante

    public Quadrada() {
        super();
        this.grau = 2;
    }

    public Quadrada(String expression, String variable) {
        super(expression, variable);
        this.grau = 2;
        extrairCoeficientesQuadraticos(expression, variable);
    }

    public Quadrada(double a, double b, double c, String variable) {
        super(new double[] { c, b, a }, variable);
        this.a = a;
        this.b = b;
        this.c = c;
        this.grau = 2;
    }

    /**
     * Extrai os coeficientes a, b e c da expressão quadrática
     */
    private void extrairCoeficientesQuadraticos(
        String expression,
        String variable
    ) {
        // Remove espaços e normaliza a expressão
        String expr = expression.replaceAll("\\s+", "");

        // Inicializa coeficientes
        this.a = 0;
        this.b = 0;
        this.c = 0;

        // Procura por padrões como "2x²", "x²", "-3x²", etc.
        java.util.regex.Pattern patternX2 = java.util.regex.Pattern.compile(
            "([+-]?\\d*)" + variable + "\\^2"
        );
        java.util.regex.Matcher matcherX2 = patternX2.matcher(expr);

        if (matcherX2.find()) {
            String coefStr = matcherX2.group(1);
            if (coefStr.isEmpty() || coefStr.equals("+")) {
                this.a = 1;
            } else if (coefStr.equals("-")) {
                this.a = -1;
            } else {
                this.a = Double.parseDouble(coefStr);
            }
        }

        // Remove o termo x² para procurar o termo linear
        String semX2 = expr.replaceAll("([+-]?\\d*)" + variable + "\\^2", "");

        // Procura por padrões como "2x", "x", "-3x", etc.
        java.util.regex.Pattern patternX = java.util.regex.Pattern.compile(
            "([+-]?\\d*)" + variable + "(?![\\^\\d])"
        );
        java.util.regex.Matcher matcherX = patternX.matcher(semX2);

        if (matcherX.find()) {
            String coefStr = matcherX.group(1);
            if (coefStr.isEmpty() || coefStr.equals("+")) {
                this.b = 1;
            } else if (coefStr.equals("-")) {
                this.b = -1;
            } else {
                this.b = Double.parseDouble(coefStr);
            }
        }

        // Remove o termo x para encontrar o termo constante
        String semX = semX2.replaceAll(
            "([+-]?\\d*)" + variable + "(?![\\^\\d])",
            ""
        );

        // Procura por termos constantes
        java.util.regex.Pattern patternConst = java.util.regex.Pattern.compile(
            "([+-]?\\d+(?:\\.\\d+)?)"
        );
        java.util.regex.Matcher matcherConst = patternConst.matcher(semX);

        if (matcherConst.find()) {
            this.c = Double.parseDouble(matcherConst.group(1));
        }

        // Atualiza o array de coeficientes
        this.coeficientes = new double[] { c, b, a };
    }

    @Override
    public String getType() {
        return "Função Quadrática";
    }

    @Override
    public String getDomain() {
        return "ℝ (todos os números reais)";
    }

    @Override
    public String getRange() {
        double verticeY = getVerticeY();
        if (a > 0) {
            return "[" + verticeY + ", +∞)";
        } else {
            return "(-∞, " + verticeY + "]";
        }
    }

    @Override
    public double evaluate(double x) {
        return a * x * x + b * x + c;
    }

    /**
     * Calcula o discriminante (Δ = b² - 4ac)
     */
    public double getDiscriminante() {
        return b * b - 4 * a * c;
    }

    /**
     * Encontra as raízes da função quadrática
     */
    public double[] encontrarRaizes() {
        double delta = getDiscriminante();

        if (delta < 0) {
            return new double[0]; // Sem raízes reais
        } else if (delta == 0) {
            // Uma raiz dupla
            return new double[] { -b / (2 * a) };
        } else {
            // Duas raízes distintas
            double sqrtDelta = Math.sqrt(delta);
            double x1 = (-b + sqrtDelta) / (2 * a);
            double x2 = (-b - sqrtDelta) / (2 * a);
            return new double[] { x1, x2 };
        }
    }

    /**
     * Calcula a coordenada x do vértice
     */
    public double getVerticeX() {
        return -b / (2 * a);
    }

    /**
     * Calcula a coordenada y do vértice
     */
    public double getVerticeY() {
        double xv = getVerticeX();
        return evaluate(xv);
    }

    /**
     * Retorna as coordenadas do vértice
     */
    public double[] getVertice() {
        return new double[] { getVerticeX(), getVerticeY() };
    }

    /**
     * Verifica se a parábola tem concavidade para cima
     */
    public boolean isConcavidadeParaCima() {
        return a > 0;
    }

    /**
     * Verifica se a parábola tem concavidade para baixo
     */
    public boolean isConcavidadeParaBaixo() {
        return a < 0;
    }

    /**
     * Calcula o eixo de simetria
     */
    public double getEixoSimetria() {
        return getVerticeX();
    }

    /**
     * Encontra o valor mínimo ou máximo da função
     */
    public double getValorExtremo() {
        return getVerticeY();
    }

    /**
     * Verifica se o vértice é um ponto de mínimo
     */
    public boolean isMinimo() {
        return a > 0;
    }

    /**
     * Verifica se o vértice é um ponto de máximo
     */
    public boolean isMaximo() {
        return a < 0;
    }

    /**
     * Calcula o intercepto com o eixo y
     */
    public double getInterceptoY() {
        return c;
    }

    /**
     * Encontra os interceptos com o eixo x (raízes)
     */
    public double[] getInterceptosX() {
        return encontrarRaizes();
    }

    /**
     * Retorna a equação na forma y = ax² + bx + c
     */
    public String getEquacaoReduzida() {
        StringBuilder sb = new StringBuilder();
        sb.append("y = ");

        // Termo ax²
        if (a == 1) {
            sb.append(variable).append("²");
        } else if (a == -1) {
            sb.append("-").append(variable).append("²");
        } else if (a != 0) {
            sb.append(a).append(variable).append("²");
        }

        // Termo bx
        if (b > 0 && a != 0) {
            sb.append(" + ");
            if (b == 1) {
                sb.append(variable);
            } else {
                sb.append(b).append(variable);
            }
        } else if (b < 0) {
            sb.append(" - ");
            if (b == -1) {
                sb.append(variable);
            } else {
                sb.append(Math.abs(b)).append(variable);
            }
        } else if (b != 0 && a == 0) {
            if (b == 1) {
                sb.append(variable);
            } else {
                sb.append(b).append(variable);
            }
        }

        // Termo c
        if (c > 0 && (a != 0 || b != 0)) {
            sb.append(" + ").append(c);
        } else if (c < 0) {
            sb.append(" - ").append(Math.abs(c));
        } else if (c != 0 && a == 0 && b == 0) {
            sb.append(c);
        } else if (a == 0 && b == 0 && c == 0) {
            sb.append("0");
        }

        return sb.toString();
    }

    /**
     * Retorna informações completas sobre a função
     */
    public String getAnaliseCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("Análise da Função Quadrática:\n");
        sb.append("Equação: ").append(getEquacaoReduzida()).append("\n");
        sb.append("Discriminante: ").append(getDiscriminante()).append("\n");
        sb
            .append("Vértice: (")
            .append(getVerticeX())
            .append(", ")
            .append(getVerticeY())
            .append(")\n");
        sb
            .append("Concavidade: ")
            .append(isConcavidadeParaCima() ? "Para cima" : "Para baixo")
            .append("\n");
        sb
            .append("Valor extremo: ")
            .append(getValorExtremo())
            .append(" (")
            .append(isMinimo() ? "mínimo" : "máximo")
            .append(")\n");
        sb.append("Intercepto Y: ").append(getInterceptoY()).append("\n");

        double[] raizes = encontrarRaizes();
        if (raizes.length == 0) {
            sb.append("Raízes: Não possui raízes reais\n");
        } else if (raizes.length == 1) {
            sb.append("Raiz: ").append(raizes[0]).append(" (raiz dupla)\n");
        } else {
            sb
                .append("Raízes: ")
                .append(raizes[0])
                .append(" e ")
                .append(raizes[1])
                .append("\n");
        }

        return sb.toString();
    }

    // Getters específicos
    public double getCoeficienteA() {
        return a;
    }

    public double getCoeficienteB() {
        return b;
    }

    public double getCoeficienteC() {
        return c;
    }

    @Override
    public String toString() {
        return String.format(
            "Quadrada[a=%.2f, b=%.2f, c=%.2f, expressão=%s]",
            a,
            b,
            c,
            expression
        );
    }
}
