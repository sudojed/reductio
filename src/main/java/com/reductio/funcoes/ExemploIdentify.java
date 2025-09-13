package com.reductio.funcoes;

import com.reductio.Parser;
import com.reductio.Expr;
import com.reductio.funcoes.polinomial.*;
import com.reductio.funcoes.exponencial.*;
import com.reductio.funcoes.logaritmica.*;
import com.reductio.funcoes.trigonometrica.*;

/**
 * Exemplo simples de uso do método identify
 * Demonstra como identificar tipos de função a partir de expressões irredutíveis
 */
public class ExemploIdentify {

    public static void main(String[] args) {
        System.out.println("=== EXEMPLO DO MÉTODO IDENTIFY ===\n");

        // Exemplos de diferentes tipos de função
        exemploLinear();
        exemploQuadratica();
        exemploExponencial();
        exemploLogaritmica();
        exemploTrigonometrica();
    }

    /**
     * Exemplo com função linear
     */
    private static void exemploLinear() {
        System.out.println("1. FUNÇÃO LINEAR");
        String expressao = "2x + 5";

        // Parse e simplificação da expressão
        Expr expr = Parser.parse(expressao);
        Expr exprSimplificada = expr.simplify();

        // Identificação do tipo de função
        Function funcao = Function.identify(exprSimplificada, "x");

        if (funcao instanceof Linear) {
            Linear linear = (Linear) funcao;
            System.out.println("Expressão: " + expressao);
            System.out.println("Tipo: " + linear.getType());
            System.out.println("Coeficiente angular: " + linear.getCoeficienteAngular());
            System.out.println("Coeficiente linear: " + linear.getCoeficienteLinear());
            System.out.println("Equação: " + linear.getEquacaoReduzida());
            System.out.println("É crescente: " + linear.isCrescente());
            System.out.println("Domínio: " + linear.getDomain());
            System.out.println("f(2) = " + linear.evaluate(2));
        }
        System.out.println();
    }

    /**
     * Exemplo com função quadrática
     */
    private static void exemploQuadratica() {
        System.out.println("2. FUNÇÃO QUADRÁTICA");
        String expressao = "x^2 - 4x + 3";

        Expr expr = Parser.parse(expressao);
        Expr exprSimplificada = expr.simplify();
        Function funcao = Function.identify(exprSimplificada, "x");

        if (funcao instanceof Quadrada) {
            Quadrada quadrada = (Quadrada) funcao;
            System.out.println("Expressão: " + expressao);
            System.out.println("Tipo: " + quadrada.getType());
            System.out.println("Discriminante: " + quadrada.getDiscriminante());

            double[] vertice = quadrada.getVertice();
            System.out.println("Vértice: (" + vertice[0] + ", " + vertice[1] + ")");

            double[] raizes = quadrada.encontrarRaizes();
            if (raizes.length == 2) {
                System.out.println("Raízes: " + raizes[0] + " e " + raizes[1]);
            }

            System.out.println("Concavidade: " +
                (quadrada.isConcavidadeParaCima() ? "Para cima" : "Para baixo"));
            System.out.println("f(0) = " + quadrada.evaluate(0));
        }
        System.out.println();
    }

    /**
     * Exemplo com função exponencial
     */
    private static void exemploExponencial() {
        System.out.println("3. FUNÇÃO EXPONENCIAL");
        String expressao = "2^x";

        Expr expr = Parser.parse(expressao);
        Expr exprSimplificada = expr.simplify();
        Function funcao = Function.identify(exprSimplificada, "x");

        if (funcao instanceof Exponencial) {
            Exponencial exponencial = (Exponencial) funcao;
            System.out.println("Expressão: " + expressao);
            System.out.println("Tipo: " + exponencial.getType());
            System.out.println("Base: " + exponencial.getBase());
            System.out.println("É crescente: " + exponencial.isCrescente());
            System.out.println("Intercepto Y: " + exponencial.getInterceptoY());
            System.out.println("Domínio: " + exponencial.getDomain());
            System.out.println("f(3) = " + exponencial.evaluate(3));
        }
        System.out.println();
    }

    /**
     * Exemplo com função logarítmica
     */
    private static void exemploLogaritmica() {
        System.out.println("4. FUNÇÃO LOGARÍTMICA");
        String expressao = "ln(x)";

        Expr expr = Parser.parse(expressao);
        Expr exprSimplificada = expr.simplify();
        Function funcao = Function.identify(exprSimplificada, "x");

        if (funcao instanceof Logaritmica) {
            Logaritmica logaritmica = (Logaritmica) funcao;
            System.out.println("Expressão: " + expressao);
            System.out.println("Tipo: " + logaritmica.getType());
            System.out.println("Base: " + logaritmica.getBase());
            System.out.println("É logaritmo natural: " + logaritmica.isLogaritmoNatural());
            System.out.println("É crescente: " + logaritmica.isCrescente());
            System.out.println("Domínio: " + logaritmica.getDomain());
            System.out.println("Intercepto X: " + logaritmica.getInterceptoX());
            System.out.println("f(e) = " + logaritmica.evaluate(Math.E));
        }
        System.out.println();
    }

    /**
     * Exemplo com função trigonométrica
     */
    private static void exemploTrigonometrica() {
        System.out.println("5. FUNÇÃO TRIGONOMÉTRICA");
        String expressao = "sin(x)";

        Expr expr = Parser.parse(expressao);
        Expr exprSimplificada = expr.simplify();
        Function funcao = Function.identify(exprSimplificada, "x");

        if (funcao instanceof Trigonometrica) {
            Trigonometrica trigonometrica = (Trigonometrica) funcao;
            System.out.println("Expressão: " + expressao);
            System.out.println("Tipo: " + trigonometrica.getType());
            System.out.println("Função: " + trigonometrica.getFuncaoTrigonometrica());
            System.out.println("Período: " + trigonometrica.getPeriodo());
            System.out.println("Amplitude: " + trigonometrica.getAmplitude());
            System.out.println("É função ímpar: " + trigonometrica.isFuncaoImpar());
            System.out.println("Domínio: " + trigonometrica.getDomain());
            System.out.println("f(π/2) = " + trigonometrica.evaluate(Math.PI/2));
        }
        System.out.println();
    }

    /**
     * Demonstra casos onde o método identify retorna null
     */
    public static void exemplosCasosEspeciais() {
        System.out.println("=== CASOS ESPECIAIS ===\n");

        String[] expressoesMistas = {
            "x^2 + sin(x)",    // Função mista
            "ln(x) * e^x",     // Produto de funções
            "x + y",           // Múltiplas variáveis
            "5"                // Constante
        };

        for (String expr : expressoesMistas) {
            System.out.println("Expressão: " + expr);
            try {
                Expr expressao = Parser.parse(expr);
                Function funcao = Function.identify(expressao, "x");

                if (funcao != null) {
                    System.out.println("Identificado como: " + funcao.getType());
                } else {
                    System.out.println("Não foi possível identificar (função mista ou complexa)");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
            System.out.println();
        }
    }
}
