package com.reductio.funcoes;

import com.reductio.Parser;
import com.reductio.Expr;
import com.reductio.funcoes.polinomial.*;
import com.reductio.funcoes.exponencial.*;
import com.reductio.funcoes.logaritmica.*;
import com.reductio.funcoes.trigonometrica.*;

/**
 * Classe de teste para demonstrar o funcionamento do método identify
 * que identifica tipos de função e instancia objetos correspondentes
 */
public class FunctionIdentifierTest {

    public static void main(String[] args) {
        System.out.println("=== TESTE DO MÉTODO IDENTIFY ===\n");

        // Array de expressões para teste
        String[] expressoes = {
            // Funções polinomiais
            "2x + 3",
            "x^2 + 3x - 2",
            "x^3 - 2x^2 + x + 1",
            "5x^4 - 3x^3 + 2x^2 - x + 7",

            // Funções exponenciais
            "2^x",
            "e^x",
            "3*2^(2x+1)",
            "e^(x-1) + 2",

            // Funções logarítmicas
            "ln(x)",
            "log(x)",
            "2*ln(x+1)",
            "ln(2x-3) + 1",

            // Funções trigonométricas
            "sin(x)",
            "2*cos(x)",
            "tan(2x+1)",
            "3*sin(x) + 2"
        };

        String variavel = "x";

        for (String expressao : expressoes) {
            testarIdentificacao(expressao, variavel);
            System.out.println();
        }

        // Teste adicional com análise detalhada
        System.out.println("\n=== ANÁLISE DETALHADA DE ALGUMAS FUNÇÕES ===\n");

        analisarDetalhadamente("x^2 - 4x + 3", variavel);
        analisarDetalhadamente("2*sin(3x + 1) - 1", variavel);
        analisarDetalhadamente("ln(x + 2) + 3", variavel);
    }

    /**
     * Testa a identificação de uma expressão específica
     */
    private static void testarIdentificacao(String expressao, String variavel) {
        System.out.println("Expressão: " + expressao);

        try {
            // Parse da expressão
            Expr expr = Parser.parse(expressao);
            System.out.println("Parse realizado: " + expr.show());

            // Simplificação
            Expr exprSimplificada = expr.simplify();
            System.out.println("Expressão simplificada: " + exprSimplificada.show());

            // Identificação do tipo de função
            Function funcao = Function.identify(exprSimplificada, variavel);

            if (funcao != null) {
                System.out.println("Tipo identificado: " + funcao.getType());
                System.out.println("Classe: " + funcao.getClass().getSimpleName());
                System.out.println("Domínio: " + funcao.getDomain());
                System.out.println("Imagem: " + funcao.getRange());

                // Teste de avaliação em alguns pontos
                try {
                    double[] testValues = {0, 1, 2, -1};
                    System.out.print("Valores de teste: ");
                    for (double val : testValues) {
                        try {
                            double resultado = funcao.evaluate(val);
                            System.out.printf("f(%.0f)=%.2f ", val, resultado);
                        } catch (Exception e) {
                            System.out.printf("f(%.0f)=indefinido ", val);
                        }
                    }
                    System.out.println();
                } catch (Exception e) {
                    System.out.println("Erro na avaliação: " + e.getMessage());
                }

            } else {
                System.out.println("Tipo não identificado ou função mista");
            }

        } catch (Exception e) {
            System.out.println("Erro no processamento: " + e.getMessage());
        }

        System.out.println("----------------------------------------");
    }

    /**
     * Realiza análise detalhada de uma função específica
     */
    private static void analisarDetalhadamente(String expressao, String variavel) {
        System.out.println("ANÁLISE DETALHADA: " + expressao);

        try {
            Expr expr = Parser.parse(expressao);
            Expr exprSimplificada = expr.simplify();
            Function funcao = Function.identify(exprSimplificada, variavel);

            if (funcao != null) {
                System.out.println("Classe: " + funcao.getClass().getSimpleName());

                // Análise específica por tipo
                if (funcao instanceof Quadrada) {
                    Quadrada quad = (Quadrada) funcao;
                    System.out.println(quad.getAnaliseCompleta());

                } else if (funcao instanceof Linear) {
                    Linear lin = (Linear) funcao;
                    System.out.println("Coeficiente angular: " + lin.getCoeficienteAngular());
                    System.out.println("Coeficiente linear: " + lin.getCoeficienteLinear());
                    System.out.println("Equação reduzida: " + lin.getEquacaoReduzida());
                    System.out.println("É crescente: " + lin.isCrescente());

                } else if (funcao instanceof Cubica) {
                    Cubica cub = (Cubica) funcao;
                    System.out.println(cub.getAnaliseCompleta());

                } else if (funcao instanceof Trigonometrica) {
                    Trigonometrica trig = (Trigonometrica) funcao;
                    System.out.println(trig.getAnaliseCompleta());

                } else if (funcao instanceof Logaritmica) {
                    Logaritmica log = (Logaritmica) funcao;
                    System.out.println(log.getAnaliseCompleta());

                } else if (funcao instanceof Exponencial) {
                    Exponencial exp = (Exponencial) funcao;
                    System.out.println(exp.getAnaliseCompleta());
                }

                // Teste de derivada (quando aplicável)
                try {
                    if (funcao instanceof Quadrada) {
                        Quadrada quad = (Quadrada) funcao;
                        Polinomial derivada = quad.derivada();
                        System.out.println("Derivada: " + derivada.getExpression());
                    } else if (funcao instanceof Cubica) {
                        Cubica cub = (Cubica) funcao;
                        Quadrada derivada = cub.primeiraDerivada();
                        System.out.println("Primeira derivada: " + derivada.getExpression());
                    }
                } catch (Exception e) {
                    System.out.println("Erro no cálculo da derivada: " + e.getMessage());
                }

            } else {
                System.out.println("Função não identificada");
            }

        } catch (Exception e) {
            System.out.println("Erro na análise: " + e.getMessage());
        }

        System.out.println("========================================\n");
    }

    /**
     * Demonstra casos especiais e limitações
     */
    public static void demonstrarCasosEspeciais() {
        System.out.println("\n=== CASOS ESPECIAIS E LIMITAÇÕES ===\n");

        String[] casosEspeciais = {
            "x^2 + sin(x)",     // Função mista
            "e^x * ln(x)",      // Produto de funções
            "sin(x) / cos(x)",  // Quociente trigonométrico
            "x^2 + 2x + 1",     // Quadrado perfeito
            "0",                // Função constante zero
            "5"                 // Função constante
        };

        for (String expressao : casosEspeciais) {
            System.out.println("Caso especial: " + expressao);

            try {
                Expr expr = Parser.parse(expressao);
                Function funcao = Function.identify(expr, "x");

                if (funcao != null) {
                    System.out.println("  Identificado como: " + funcao.getType());
                } else {
                    System.out.println("  Não foi possível identificar (função mista ou complexa)");
                }
            } catch (Exception e) {
                System.out.println("  Erro: " + e.getMessage());
            }

            System.out.println();
        }
    }
}
