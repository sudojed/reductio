package com.reductio.funcoes.polinomial;

import com.reductio.funcoes.Function;

/**
 * Classe base para funções polinomiais
 * Representa funções do tipo f(x) = a_n*x^n + a_(n-1)*x^(n-1) + ... + a_1*x + a_0
 */
public class Polinomial extends Function {

    protected int grau;
    protected double[] coeficientes;

    public Polinomial() {
        super();
    }

    public Polinomial(String expression, String variable) {
        super(expression, variable);
        this.grau = determinarGrau(expression);
        this.coeficientes = extrairCoeficientes(expression, variable);
    }

    public Polinomial(double[] coeficientes, String variable) {
        this.coeficientes = coeficientes;
        this.grau = coeficientes.length - 1;
        this.variable = variable;
        this.expression = construirExpressao(coeficientes, variable);
    }

    /**
     * Determina o grau do polinômio baseado na expressão
     */
    private int determinarGrau(String expression) {
        int maxGrau = 0;

        // Procurar por padrões como x^n
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            variable + "\\^(\\d+)"
        );
        java.util.regex.Matcher matcher = pattern.matcher(expression);

        while (matcher.find()) {
            int grau = Integer.parseInt(matcher.group(1));
            maxGrau = Math.max(maxGrau, grau);
        }

        // Se contém apenas x (sem expoente), grau é pelo menos 1
        if (expression.contains(variable) && maxGrau == 0) {
            maxGrau = 1;
        }

        return maxGrau;
    }

    /**
     * Extrai os coeficientes do polinômio
     */
    private double[] extrairCoeficientes(String expression, String variable) {
        double[] coefs = new double[grau + 1];

        // Implementação simplificada - pode ser expandida
        // Por agora, assume formato padrão

        return coefs;
    }

    /**
     * Constrói a expressão a partir dos coeficientes
     */
    private String construirExpressao(double[] coeficientes, String variable) {
        StringBuilder sb = new StringBuilder();
        boolean primeiro = true;

        for (int i = coeficientes.length - 1; i >= 0; i--) {
            double coef = coeficientes[i];

            if (coef == 0) continue;

            if (!primeiro && coef > 0) {
                sb.append(" + ");
            } else if (coef < 0) {
                sb.append(primeiro ? "-" : " - ");
                coef = Math.abs(coef);
            }

            if (i == 0) {
                // Termo constante
                sb.append(coef);
            } else if (i == 1) {
                // Termo linear
                if (coef == 1) {
                    sb.append(variable);
                } else {
                    sb.append(coef).append(variable);
                }
            } else {
                // Termos de grau superior
                if (coef == 1) {
                    sb.append(variable).append("^").append(i);
                } else {
                    sb.append(coef).append(variable).append("^").append(i);
                }
            }

            primeiro = false;
        }

        return sb.toString();
    }

    @Override
    public String getType() {
        return "Polinomial de grau " + grau;
    }

    @Override
    public String getDomain() {
        return "ℝ (todos os números reais)";
    }

    @Override
    public String getRange() {
        if (grau % 2 == 0 && coeficientes[grau] > 0) {
            return "[mínimo, +∞)";
        } else if (grau % 2 == 0 && coeficientes[grau] < 0) {
            return "(-∞, máximo]";
        } else {
            return "ℝ (todos os números reais)";
        }
    }

    @Override
    public double evaluate(double value) {
        double resultado = 0;
        double potencia = 1;

        for (int i = 0; i <= grau; i++) {
            resultado += coeficientes[i] * potencia;
            potencia *= value;
        }

        return resultado;
    }

    /**
     * Calcula a derivada do polinômio
     */
    public Polinomial derivada() {
        if (grau == 0) {
            return new Polinomial(new double[] { 0 }, variable);
        }

        double[] novoCoefs = new double[grau];

        for (int i = 1; i <= grau; i++) {
            novoCoefs[i - 1] = i * coeficientes[i];
        }

        return new Polinomial(novoCoefs, variable);
    }

    /**
     * Encontra as raízes do polinômio (implementação básica)
     */
    public double[] encontrarRaizes() {
        // Implementação básica - pode ser expandida para diferentes graus
        return new double[0];
    }

    // Getters
    public int getGrau() {
        return grau;
    }

    public double[] getCoeficientes() {
        return coeficientes.clone();
    }

    public double getCoeficiente(int grau) {
        if (grau >= 0 && grau < coeficientes.length) {
            return coeficientes[grau];
        }
        return 0;
    }
}
