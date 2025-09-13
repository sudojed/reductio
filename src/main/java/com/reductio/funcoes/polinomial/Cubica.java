package com.reductio.funcoes.polinomial;

/**
 * Classe para funções cúbicas
 * Representa funções do tipo f(x) = ax³ + bx² + cx + d
 */
public class Cubica extends Polinomial {

    private double a; // coeficiente de x³
    private double b; // coeficiente de x²
    private double c; // coeficiente de x
    private double d; // termo constante

    public Cubica() {
        super();
        this.grau = 3;
    }

    public Cubica(String expression, String variable) {
        super(expression, variable);
        this.grau = 3;
        extrairCoeficientesCubicos(expression, variable);
    }

    public Cubica(double a, double b, double c, double d, String variable) {
        super(new double[] { d, c, b, a }, variable);
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.grau = 3;
    }

    /**
     * Extrai os coeficientes a, b, c e d da expressão cúbica
     */
    private void extrairCoeficientesCubicos(
        String expression,
        String variable
    ) {
        // Remove espaços e normaliza a expressão
        String expr = expression.replaceAll("\\s+", "");

        // Inicializa coeficientes
        this.a = 0;
        this.b = 0;
        this.c = 0;
        this.d = 0;

        // Procura por padrões como "2x³", "x³", "-3x³", etc.
        java.util.regex.Pattern patternX3 = java.util.regex.Pattern.compile(
            "([+-]?\\d*)" + variable + "\\^3"
        );
        java.util.regex.Matcher matcherX3 = patternX3.matcher(expr);

        if (matcherX3.find()) {
            String coefStr = matcherX3.group(1);
            if (coefStr.isEmpty() || coefStr.equals("+")) {
                this.a = 1;
            } else if (coefStr.equals("-")) {
                this.a = -1;
            } else {
                this.a = Double.parseDouble(coefStr);
            }
        }

        // Remove o termo x³ para procurar o termo quadrático
        String semX3 = expr.replaceAll("([+-]?\\d*)" + variable + "\\^3", "");

        // Procura por padrões como "2x²", "x²", "-3x²", etc.
        java.util.regex.Pattern patternX2 = java.util.regex.Pattern.compile(
            "([+-]?\\d*)" + variable + "\\^2"
        );
        java.util.regex.Matcher matcherX2 = patternX2.matcher(semX3);

        if (matcherX2.find()) {
            String coefStr = matcherX2.group(1);
            if (coefStr.isEmpty() || coefStr.equals("+")) {
                this.b = 1;
            } else if (coefStr.equals("-")) {
                this.b = -1;
            } else {
                this.b = Double.parseDouble(coefStr);
            }
        }

        // Remove o termo x² para procurar o termo linear
        String semX2 = semX3.replaceAll("([+-]?\\d*)" + variable + "\\^2", "");

        // Procura por padrões como "2x", "x", "-3x", etc.
        java.util.regex.Pattern patternX = java.util.regex.Pattern.compile(
            "([+-]?\\d*)" + variable + "(?![\\^\\d])"
        );
        java.util.regex.Matcher matcherX = patternX.matcher(semX2);

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
            this.d = Double.parseDouble(matcherConst.group(1));
        }

        // Atualiza o array de coeficientes
        this.coeficientes = new double[] { d, c, b, a };
    }

    @Override
    public String getType() {
        return "Função Cúbica";
    }

    @Override
    public String getDomain() {
        return "ℝ (todos os números reais)";
    }

    @Override
    public String getRange() {
        return "ℝ (todos os números reais)";
    }

    @Override
    public double evaluate(double x) {
        return a * x * x * x + b * x * x + c * x + d;
    }

    /**
     * Calcula a primeira derivada: f'(x) = 3ax² + 2bx + c
     */
    public Quadrada primeiraDerivada() {
        return new Quadrada(3 * a, 2 * b, c, variable);
    }

    /**
     * Calcula a segunda derivada: f''(x) = 6ax + 2b
     */
    public Linear segundaDerivada() {
        return new Linear(6 * a, 2 * b, variable);
    }

    /**
     * Encontra os pontos críticos (onde f'(x) = 0)
     */
    public double[] encontrarPontosCriticos() {
        Quadrada derivada = primeiraDerivada();
        return derivada.encontrarRaizes();
    }

    /**
     * Encontra pontos de inflexão (onde f''(x) = 0)
     */
    public double encontrarPontoInflexao() {
        Linear segundaDeriv = segundaDerivada();
        try {
            return segundaDeriv.encontrarRaiz();
        } catch (ArithmeticException e) {
            // Se a = 0, não há ponto de inflexão
            return Double.NaN;
        }
    }

    /**
     * Classifica os pontos críticos
     */
    public String[] classificarPontosCriticos() {
        double[] pontosCriticos = encontrarPontosCriticos();
        String[] classificacao = new String[pontosCriticos.length];

        for (int i = 0; i < pontosCriticos.length; i++) {
            double x = pontosCriticos[i];
            double segundaDeriv = segundaDerivada().evaluate(x);

            if (segundaDeriv > 0) {
                classificacao[i] = "Mínimo local em x = " + x;
            } else if (segundaDeriv < 0) {
                classificacao[i] = "Máximo local em x = " + x;
            } else {
                classificacao[i] = "Ponto de sela em x = " + x;
            }
        }

        return classificacao;
    }

    /**
     * Verifica se o gráfico tem formato de "S" (sigmóide)
     */
    public boolean isFormatoS() {
        return a != 0;
    }

    /**
     * Calcula o intercepto com o eixo y
     */
    public double getInterceptoY() {
        return d;
    }

    /**
     * Encontra as raízes usando método de Newton-Raphson (aproximação)
     */
    public double[] encontrarRaizesAproximadas(
        double[] estimativasIniciais,
        double precisao,
        int maxIteracoes
    ) {
        java.util.List<Double> raizes = new java.util.ArrayList<>();

        for (double estimativa : estimativasIniciais) {
            double raiz = newtonRaphson(estimativa, precisao, maxIteracoes);
            if (!Double.isNaN(raiz)) {
                // Verifica se a raiz já foi encontrada
                boolean jaEncontrada = false;
                for (Double r : raizes) {
                    if (Math.abs(r - raiz) < precisao) {
                        jaEncontrada = true;
                        break;
                    }
                }
                if (!jaEncontrada) {
                    raizes.add(raiz);
                }
            }
        }

        return raizes.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * Método de Newton-Raphson para encontrar uma raiz
     */
    private double newtonRaphson(double x0, double precisao, int maxIteracoes) {
        double x = x0;

        for (int i = 0; i < maxIteracoes; i++) {
            double fx = evaluate(x);
            double fpx = primeiraDerivada().evaluate(x);

            if (Math.abs(fpx) < 1e-12) {
                return Double.NaN; // Derivada muito pequena
            }

            double novoX = x - fx / fpx;

            if (Math.abs(novoX - x) < precisao) {
                return novoX;
            }

            x = novoX;
        }

        return Double.NaN; // Não convergiu
    }

    /**
     * Retorna a equação na forma y = ax³ + bx² + cx + d
     */
    public String getEquacaoReduzida() {
        StringBuilder sb = new StringBuilder();
        sb.append("y = ");

        boolean primeiro = true;

        // Termo ax³
        if (a != 0) {
            if (a == 1) {
                sb.append(variable).append("³");
            } else if (a == -1) {
                sb.append("-").append(variable).append("³");
            } else {
                sb.append(a).append(variable).append("³");
            }
            primeiro = false;
        }

        // Termo bx²
        if (b != 0) {
            if (!primeiro && b > 0) {
                sb.append(" + ");
            } else if (b < 0) {
                sb.append(primeiro ? "-" : " - ");
                b = Math.abs(b);
            }

            if (b == 1) {
                sb.append(variable).append("²");
            } else {
                sb.append(b).append(variable).append("²");
            }
            primeiro = false;
        }

        // Termo cx
        if (c != 0) {
            if (!primeiro && c > 0) {
                sb.append(" + ");
            } else if (c < 0) {
                sb.append(primeiro ? "-" : " - ");
                c = Math.abs(c);
            }

            if (c == 1) {
                sb.append(variable);
            } else {
                sb.append(c).append(variable);
            }
            primeiro = false;
        }

        // Termo d
        if (d != 0) {
            if (!primeiro && d > 0) {
                sb.append(" + ").append(d);
            } else if (d < 0) {
                sb.append(primeiro ? "" : " - ").append(Math.abs(d));
            } else if (!primeiro) {
                sb.append(" + ").append(d);
            } else {
                sb.append(d);
            }
            primeiro = false;
        }

        if (primeiro) {
            sb.append("0");
        }

        return sb.toString();
    }

    /**
     * Retorna informações completas sobre a função
     */
    public String getAnaliseCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("Análise da Função Cúbica:\n");
        sb.append("Equação: ").append(getEquacaoReduzida()).append("\n");

        double[] pontosCriticos = encontrarPontosCriticos();
        if (pontosCriticos.length > 0) {
            sb.append("Pontos críticos: ");
            for (int i = 0; i < pontosCriticos.length; i++) {
                sb.append(pontosCriticos[i]);
                if (i < pontosCriticos.length - 1) sb.append(", ");
            }
            sb.append("\n");

            String[] classificacoes = classificarPontosCriticos();
            for (String classificacao : classificacoes) {
                sb.append(classificacao).append("\n");
            }
        } else {
            sb.append("Sem pontos críticos\n");
        }

        double pontoInflexao = encontrarPontoInflexao();
        if (!Double.isNaN(pontoInflexao)) {
            sb
                .append("Ponto de inflexão: x = ")
                .append(pontoInflexao)
                .append("\n");
        }

        sb.append("Intercepto Y: ").append(getInterceptoY()).append("\n");

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

    public double getCoeficienteD() {
        return d;
    }

    @Override
    public String toString() {
        return String.format(
            "Cubica[a=%.2f, b=%.2f, c=%.2f, d=%.2f, expressão=%s]",
            a,
            b,
            c,
            d,
            expression
        );
    }
}
