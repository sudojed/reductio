package com.reductio;

import java.util.*;

public class Reduction {

    public static String simplify(String expr) {
        Expr ast = Parser.parse(expr);
        Expr simplified = ast.simplify();
        return simplified.show();
    }

    public static void main(String[] args) {
        String[] tests = {
            // Soma e subtração de variáveis e constantes
            "x + x",
            "2x + 3x",
            "2x - 5x",
            "x + 2x - 5x + x",
            "x - x",
            "2 + 3 - 5",
            "2x + 3y - 5x + 7 - 2y",
            // Multiplicação e divisão
            "2*x*3",
            "x*0",
            "0*x",
            "x*1",
            "1*x",
            "x/x",
            "x/1",
            "0/x",
            // Potenciação
            "x^0",
            "x^1",
            "1^x",
            "0^x",
            "2^3",
            // Funções
            "ln(e^x)",
            "sin(0)",
            "cos(0)",
            "ln(1)",
            "ln(e)",
            // Casos aninhados e sinais
            "2x-(x+3x)",
            "2x-(x-3x)",
            "2x-(-x)",
            "-x+2x",
            "-x-x",
            "x+(-x)",
            "x-(-x)",
            // Casos mistos
            "ln(e^x)+2x-5x+x-y-y",
            "2x+3y-2x-3y+5-5",
            "2x+3x+4x",
            "2x-3x+4x-5x",
            "2x+3y+4z-2x-3y-4z",
            "2x+3y+4z-2x-3y-4z+5-5",
            "2x+3y+4z-2x-3y-4z+5-5+ln(e^x)",
            "2x+3y+4z-2x-3y-4z+5-5+ln(e^x)-x",
            // Casos com constantes e variáveis misturadas
            "2x+3-2x-3",
            "2x+3-2x-3+5",
            "2x+3-2x-3+5-5",
            // Casos com múltiplos parênteses
            "((x + x) + (2x - 5x))",
            "((2x + 3y) - (5x + 7 - 2y))",
            // Casos de zero
            "0",
            "x+0",
            "0+x",
            "x-0",
            "0-x",
            "0+0",
            "0-0",
            // Casos de um
            "1",
            "x+1",
            "1+x",
            "x-1",
            "1-x",
            "1+1",
            "1-1",
            // Divisão de polinômios e expressões inteiras
            "(x^2 - 1)/(x - 1)",
            "(x^2 + 2x + 1)/(x + 1)",
            "(2x^2 + 4x)/(2x)",
            "(x^2 - x)/(x)",
            "(x^2 - y^2)/(x - y)",
            "(x^3 - x)/(x)",
            "(x^2 - 4)/(x - 2)",
            "(x^2 - 4)/(x + 2)",
            "(x^2 - 2x + 1)/(x - 1)",
            "(x^2 + 2x + 1)/(x - 1)",
            "(x^2 - 2x + 1)/(x + 1)",
            "(x^2 + 2x + 1)/(x + 2)",
            "(x^2 - 1)/(x + 1)",
            "(x^2 - 9)/(x - 3)",
            "(x^2 - 9)/(x + 3)",
            "(x^3 + 3x^2 + 3x + 1)/(x + 1)",
            "(x^3 - 1)/(x - 1)",
            "(x^3 + 1)/(x + 1)",
            "(x^4 - 1)/(x - 1)",
            "(x^4 - 1)/(x + 1)",
            "(x^4 - 1)/(x^2 - 1)",
            "(x^4 - 16)/(x^2 - 4)",
            "(x^4 - 16)/(x^2 + 4)",
            "(x^4 + 4x^2 + 16)/(x^2 + 2x + 4)",
            "(x^2 + 2x + 1)/(x^2 + 2x + 1)",
            "(x^2 - 2x + 1)/(x^2 - 2x + 1)",
            "(x^2 - 1)/(x^2 - 1)",
            "(x^2 - 4)/(x^2 - 4)",
            "(x^2 + 2x + 1)/(x^2 - 2x + 1)",
            "(x^2 - 2x + 1)/(x^2 + 2x + 1)",
        };
        for (String input : tests) {
            System.out.println("Entrada: " + input);
            Map<Integer, String> steps = new LinkedHashMap<>();
            int[] stepCounter = { 0 };
            Expr ast = Parser.parse(input);
            Expr simplified = ast.simplify(steps, stepCounter);
            for (Map.Entry<Integer, String> entry : steps.entrySet()) {
                System.out.println(
                    "Passo " + entry.getKey() + ": " + entry.getValue()
                );
            }
            System.out.println("Simplificado: " + simplified.show());
            System.out.println();
        }
    }
}
