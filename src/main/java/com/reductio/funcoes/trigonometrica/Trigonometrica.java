package com.reductio.funcoes.trigonometrica;

import com.reductio.funcoes.Function;

/**
 * Classe para funções trigonométricas
 * Representa funções do tipo f(x) = a * trig(bx + c) + d
 * Onde trig pode ser sin, cos, tan, sec, csc, cot
 */
public class Trigonometrica extends Function {

    private double a; // amplitude
    private String funcaoTrig; // sin, cos, tan, sec, csc, cot
    private double b; // frequência
    private double c; // deslocamento de fase
    private double d; // deslocamento vertical

    public Trigonometrica() {
        super();
    }

    public Trigonometrica(String expression, String variable) {
        super(expression, variable);
        extrairParametrosTrigonometricos(expression, variable);
    }

    public Trigonometrica(
        double a,
        String funcaoTrig,
        double b,
        double c,
        double d,
        String variable
    ) {
        super(
            construirExpressaoStatic(a, funcaoTrig, b, c, d, variable),
            variable
        );
        this.a = a;
        this.funcaoTrig = funcaoTrig;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Construtor simplificado para f(x) = a * trig(x)
     */
    public Trigonometrica(double a, String funcaoTrig, String variable) {
        this(a, funcaoTrig, 1, 0, 0, variable);
    }

    /**
     * Constrói a expressão a partir dos parâmetros (método estático)
     */
    private static String construirExpressaoStatic(
        double a,
        String funcaoTrig,
        double b,
        double c,
        double d,
        String variable
    ) {
        StringBuilder sb = new StringBuilder();

        // Coeficiente multiplicativo (amplitude)
        if (a != 1) {
            if (a == -1) {
                sb.append("-");
            } else {
                sb.append(a).append(" * ");
            }
        }

        // Função trigonométrica
        sb.append(funcaoTrig).append("(");

        // Argumento
        if (b == 1) {
            sb.append(variable);
        } else if (b == -1) {
            sb.append("-").append(variable);
        } else {
            sb.append(b).append(variable);
        }

        if (c > 0) {
            sb.append(" + ").append(c);
        } else if (c < 0) {
            sb.append(" - ").append(Math.abs(c));
        }

        sb.append(")");

        // Termo aditivo (deslocamento vertical)
        if (d > 0) {
            sb.append(" + ").append(d);
        } else if (d < 0) {
            sb.append(" - ").append(Math.abs(d));
        }

        return sb.toString();
    }

    /**
     * Extrai os parâmetros da função trigonométrica
     */
    private void extrairParametrosTrigonometricos(
        String expression,
        String variable
    ) {
        // Implementação simplificada - detecta padrões básicos
        String expr = expression.replaceAll("\\s+", "");

        // Valores padrão
        this.a = 1;
        this.funcaoTrig = "sin"; // sin como padrão
        this.b = 1;
        this.c = 0;
        this.d = 0;

        // Detecta padrões como "sin(x)", "2*cos(3x+1)", etc.
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            "([+-]?\\d*\\.?\\d*)\\*?(sin|cos|tan|sec|csc|cot)\\((.*?)\\)"
        );
        java.util.regex.Matcher matcher = pattern.matcher(expr);

        if (matcher.find()) {
            String coefStr = matcher.group(1);
            String funcao = matcher.group(2);
            String argumento = matcher.group(3);

            // Coeficiente multiplicativo (amplitude)
            if (
                !coefStr.isEmpty() &&
                !coefStr.equals("+") &&
                !coefStr.equals("-")
            ) {
                this.a = Double.parseDouble(coefStr);
            } else if (coefStr.equals("-")) {
                this.a = -1;
            }

            // Função trigonométrica
            this.funcaoTrig = funcao;

            // Extrai coeficientes do argumento
            extrairCoeficientesArgumento(argumento, variable);
        }

        // Termo aditivo após a função trigonométrica
        String semTrig = expr.replaceAll(
            "[+-]?\\d*\\.?\\d*\\*?(sin|cos|tan|sec|csc|cot)\\(.*?\\)",
            ""
        );
        if (!semTrig.isEmpty()) {
            java.util.regex.Pattern patternD = java.util.regex.Pattern.compile(
                "([+-]?\\d+(?:\\.\\d+)?)"
            );
            java.util.regex.Matcher matcherD = patternD.matcher(semTrig);

            if (matcherD.find()) {
                this.d = Double.parseDouble(matcherD.group(1));
            }
        }
    }

    /**
     * Extrai coeficientes b e c do argumento (bx + c)
     */
    private void extrairCoeficientesArgumento(
        String argumento,
        String variable
    ) {
        // Remove espaços
        String arg = argumento.replaceAll("\\s+", "");

        // Procura por padrão bx
        java.util.regex.Pattern patternX = java.util.regex.Pattern.compile(
            "([+-]?\\d*)" + variable
        );
        java.util.regex.Matcher matcherX = patternX.matcher(arg);

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

        // Remove termo bx para encontrar c
        String semX = arg.replaceAll("([+-]?\\d*)" + variable, "");

        // Procura por termo constante
        java.util.regex.Pattern patternConst = java.util.regex.Pattern.compile(
            "([+-]?\\d+(?:\\.\\d+)?)"
        );
        java.util.regex.Matcher matcherConst = patternConst.matcher(semX);

        if (matcherConst.find()) {
            this.c = Double.parseDouble(matcherConst.group(1));
        }
    }

    /**
     * Constrói a expressão a partir dos parâmetros
     */
    private String construirExpressao() {
        return construirExpressaoStatic(a, funcaoTrig, b, c, d, getVariable());
    }

    @Override
    public Function copy() {
        return new Trigonometrica(a, funcaoTrig, b, c, d, getVariable());
    }

    @Override
    public String getType() {
        return "Função Trigonométrica (" + funcaoTrig + ")";
    }

    @Override
    public String getDomain() {
        switch (funcaoTrig) {
            case "sin":
            case "cos":
                return "ℝ (todos os números reais)";
            case "tan":
            case "sec":
                return "ℝ - {x | x = π/2 + nπ, n ∈ ℤ}";
            case "cot":
            case "csc":
                return "ℝ - {x | x = nπ, n ∈ ℤ}";
            default:
                return "ℝ (todos os números reais)";
        }
    }

    @Override
    public String getRange() {
        switch (funcaoTrig) {
            case "sin":
            case "cos":
                return "[" + (d - Math.abs(a)) + ", " + (d + Math.abs(a)) + "]";
            case "tan":
            case "cot":
                return "ℝ (todos os números reais)";
            case "sec":
            case "csc":
                if (a > 0) {
                    return "(-∞, " + (d - a) + "] ∪ [" + (d + a) + ", +∞)";
                } else {
                    return "(-∞, " + (d + a) + "] ∪ [" + (d - a) + ", +∞)";
                }
            default:
                return "ℝ (todos os números reais)";
        }
    }

    @Override
    public double evaluate(double x) {
        double argumento = b * x + c;
        double resultado;

        switch (funcaoTrig) {
            case "sin":
                resultado = a * Math.sin(argumento) + d;
                break;
            case "cos":
                resultado = a * Math.cos(argumento) + d;
                break;
            case "tan":
                resultado = a * Math.tan(argumento) + d;
                break;
            case "sec":
                resultado = a / Math.cos(argumento) + d;
                break;
            case "csc":
                resultado = a / Math.sin(argumento) + d;
                break;
            case "cot":
                resultado = a / Math.tan(argumento) + d;
                break;
            default:
                throw new IllegalArgumentException(
                    "Função trigonométrica desconhecida: " + funcaoTrig
                );
        }

        return resultado;
    }

    /**
     * Calcula o período da função
     */
    public double getPeriodo() {
        switch (funcaoTrig) {
            case "sin":
            case "cos":
            case "sec":
            case "csc":
                return (2 * Math.PI) / Math.abs(b);
            case "tan":
            case "cot":
                return Math.PI / Math.abs(b);
            default:
                return (2 * Math.PI) / Math.abs(b);
        }
    }

    /**
     * Calcula a amplitude da função
     */
    public double getAmplitude() {
        switch (funcaoTrig) {
            case "sin":
            case "cos":
                return Math.abs(a);
            default:
                return Double.NaN; // Funções tan, sec, csc, cot não têm amplitude definida
        }
    }

    /**
     * Calcula a frequência da função
     */
    public double getFrequencia() {
        return Math.abs(b) / (2 * Math.PI);
    }

    /**
     * Calcula o deslocamento de fase
     */
    public double getDeslocamentoFase() {
        return -c / b;
    }

    /**
     * Calcula o deslocamento vertical
     */
    public double getDeslocamentoVertical() {
        return d;
    }

    /**
     * Encontra os zeros da função no intervalo [0, período]
     */
    public double[] encontrarZeros() {
        java.util.List<Double> zeros = new java.util.ArrayList<>();
        double periodo = getPeriodo();
        double step = periodo / 1000; // Precisão

        for (double x = 0; x < periodo; x += step) {
            double y1 = evaluate(x);
            double y2 = evaluate(x + step);

            // Verifica mudança de sinal
            if (y1 * y2 < 0) {
                // Usa método da bissecção para refinar
                double zero = bisseccao(x, x + step, 1e-10);
                if (!Double.isNaN(zero)) {
                    zeros.add(zero);
                }
            }
        }

        return zeros.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * Método da bissecção para encontrar zeros
     */
    private double bisseccao(double a, double b, double precisao) {
        double meio;
        int maxIteracoes = 100;
        int iteracao = 0;

        while (Math.abs(b - a) > precisao && iteracao < maxIteracoes) {
            meio = (a + b) / 2;
            double fMeio = evaluate(meio);

            if (Math.abs(fMeio) < precisao) {
                return meio;
            }

            if (evaluate(a) * fMeio < 0) {
                b = meio;
            } else {
                a = meio;
            }

            iteracao++;
        }

        return (a + b) / 2;
    }

    /**
     * Calcula a derivada da função trigonométrica
     */
    public double calcularDerivada(double x) {
        double argumento = b * x + c;

        switch (funcaoTrig) {
            case "sin":
                return a * b * Math.cos(argumento);
            case "cos":
                return -a * b * Math.sin(argumento);
            case "tan":
                double sec = 1 / Math.cos(argumento);
                return a * b * sec * sec;
            case "sec":
                return a * b * (1 / Math.cos(argumento)) * Math.tan(argumento);
            case "csc":
                return (
                    -a *
                    b *
                    (1 / Math.sin(argumento)) *
                    (1 / Math.tan(argumento))
                );
            case "cot":
                double csc = 1 / Math.sin(argumento);
                return -a * b * csc * csc;
            default:
                throw new IllegalArgumentException(
                    "Função trigonométrica desconhecida: " + funcaoTrig
                );
        }
    }

    /**
     * Verifica se a função é par
     */
    public boolean isFuncaoPar() {
        return funcaoTrig.equals("cos") || funcaoTrig.equals("sec");
    }

    /**
     * Verifica se a função é ímpar
     */
    public boolean isFuncaoImpar() {
        return (
            funcaoTrig.equals("sin") ||
            funcaoTrig.equals("tan") ||
            funcaoTrig.equals("csc") ||
            funcaoTrig.equals("cot")
        );
    }

    /**
     * Retorna informações completas sobre a função
     */
    public String getAnaliseCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("Análise da Função Trigonométrica:\n");
        sb.append("Expressão: ").append(getExpression()).append("\n");
        sb.append("Função: ").append(funcaoTrig).append("\n");
        sb.append("Domínio: ").append(getDomain()).append("\n");
        sb.append("Imagem: ").append(getRange()).append("\n");
        sb.append("Período: ").append(getPeriodo()).append("\n");

        if (!Double.isNaN(getAmplitude())) {
            sb.append("Amplitude: ").append(getAmplitude()).append("\n");
        }

        sb.append("Frequência: ").append(getFrequencia()).append("\n");
        sb
            .append("Deslocamento de fase: ")
            .append(getDeslocamentoFase())
            .append("\n");
        sb
            .append("Deslocamento vertical: ")
            .append(getDeslocamentoVertical())
            .append("\n");

        if (isFuncaoPar()) {
            sb.append("Paridade: Função par\n");
        } else if (isFuncaoImpar()) {
            sb.append("Paridade: Função ímpar\n");
        } else {
            sb.append("Paridade: Nem par nem ímpar\n");
        }

        return sb.toString();
    }

    // Getters específicos
    public double getCoeficienteA() {
        return a;
    }

    public String getFuncaoTrigonometrica() {
        return funcaoTrig;
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
            "Trigonometrica[a=%.2f, funcao=%s, b=%.2f, c=%.2f, d=%.2f, expressão=%s]",
            a,
            funcaoTrig,
            b,
            c,
            d,
            getExpression()
        );
    }
}
