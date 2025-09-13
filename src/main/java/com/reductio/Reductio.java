package com.reductio;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reductio {

    private final String originalExpr;

    public Reductio(String expr) {
        this.originalExpr = expr.replace(" ", ""); // remove espaços
        runReductions();
    }

    private void runReductions() {
        System.out.println("Expressão original: " + originalExpr);

        String reduced = originalExpr;

        // aplicar reduções em ordem lógica
        reduced = reduceLogs(reduced);
        System.out.println("Após log: " + reduced);

        reduced = reduceExponentials(reduced);
        System.out.println("Após exponenciais: " + reduced);

        reduced = reducePowers(reduced);
        System.out.println("Após potenciação: " + reduced);

        reduced = reduceRoots(reduced);
        System.out.println("Após radiciação: " + reduced);

        reduced = reducePolynomials(reduced);
        System.out.println("Após polinômios: " + reduced);

        System.out.println("Final: " + reduced);
    }

    // ================= LOGS ==================
    private String reduceLogs(String expr) {
        Pattern logPattern = Pattern.compile("log(\\d+)\\(([^)]+)\\)");
        Matcher matcher = logPattern.matcher(expr);

        List<String> bases = new ArrayList<>();
        List<String> valores = new ArrayList<>();
        List<Boolean> isNegative = new ArrayList<>();

        int lastEnd = 0;
        while (matcher.find()) {
            bases.add(matcher.group(1));
            valores.add(matcher.group(2));

            String antes = expr.substring(lastEnd, matcher.start());
            isNegative.add(antes.endsWith("-"));

            lastEnd = matcher.end();
        }

        if (bases.isEmpty())
            return expr;
        String base = bases.get(0);

        List<String> numeradores = new ArrayList<>();
        List<String> denominadores = new ArrayList<>();

        for (int i = 0; i < valores.size(); i++) {
            if (isNegative.get(i))
                denominadores.add(valores.get(i));
            else
                numeradores.add(valores.get(i));
        }

        String numerador = numeradores.isEmpty() ? "1" : String.join("*", numeradores);
        String denominador = denominadores.isEmpty() ? "1" : String.join("*", denominadores);

        String reduzido = "log" + base + "(" + numerador + "/" + denominador + ")";
        return expr.replaceAll("log\\d+\\([^)]*\\)([+\\-]log\\d+\\([^)]*\\))*", reduzido);
    }

    // ================= EXPONENCIAIS ==================
    private String reduceExponentials(String expr) {
        // simplificar exp(a)*exp(b) -> exp(a+b)
        expr = expr.replaceAll("exp\\(([^)]+)\\)\\*exp\\(([^)]+)\\)", "exp($1+$2)");

        // simplificar exp(a)/exp(b) -> exp(a-b)
        expr = expr.replaceAll("exp\\(([^)]+)\\)/exp\\(([^)]+)\\)", "exp($1-$2)");

        // exp(ln(x)) = x
        expr = expr.replaceAll("exp\\(ln\\(([^)]+)\\)\\)", "$1");

        return expr;
    }

    // ================= POTENCIAÇÃO ==================
    private String reducePowers(String expr) {
        // (x^a)^b -> x^(a*b)
        // Only remove parentheses if they are around a single variable or a single number
        expr = expr.replaceAll("\\(([a-zA-Z])\\)\\^(\\d+)", "$1^$2");
        expr = expr.replaceAll("\\((\\d+)\\)\\^(\\d+)", "$1^$2");

        // x^0 -> 1
        expr = expr.replaceAll("([a-zA-Z]+)\\^0", "1");

        // x^1 -> x
        expr = expr.replaceAll("([a-zA-Z]+)\\^1", "$1");

        return expr;
    }

    // ================= RADICIAÇÃO ==================
    private String reduceRoots(String expr) {
        // sqrt(x) -> x^(1/2)
        expr = expr.replaceAll("sqrt\\(([^)]+)\\)", "($1)^(1/2)");

        // nrt(x) -> x^(1/n)
        expr = expr.replaceAll("(\\d+)rt\\(([^)]+)\\)", "($2)^(1/$1)");

        return expr;
    }

    // ================= POLINÔMIOS ==================
    private String reducePolynomials(String expr) {
        Pattern termoPattern = Pattern.compile("([+-]?\\d*)x\\^(\\d+)");
        Matcher matcher = termoPattern.matcher(expr);

        Map<Integer, Integer> termos = new HashMap<>();

        while (matcher.find()) {
            String coefStr = matcher.group(1);
            int coef = coefStr.equals("") || coefStr.equals("+") ? 1
                    : coefStr.equals("-") ? -1 : Integer.parseInt(coefStr);
            int exp = Integer.parseInt(matcher.group(2));
            termos.put(exp, termos.getOrDefault(exp, 0) + coef);
        }

        // Reconstruct the reduced polynomial expression
        List<Integer> exps = new ArrayList<>(termos.keySet());
        Collections.sort(exps, Collections.reverseOrder());
        StringBuilder polyBuilder = new StringBuilder();
        for (int exp : exps) {
            int coef = termos.get(exp);
            if (coef == 0) continue;
            if (polyBuilder.length() > 0 && coef > 0) {
                polyBuilder.append("+");
            }
            if (coef == 1) {
                polyBuilder.append("x^").append(exp);
            } else if (coef == -1) {
                polyBuilder.append("-x^").append(exp);
            } else {
                polyBuilder.append(coef).append("x^").append(exp);
            }
        }
        // Remove all polynomial terms from the original expression
        String nonPoly = expr.replaceAll("([+-]?\\d*)x\\^\\d+", "");
        // Clean up leading/trailing pluses
        String reduzido = polyBuilder.toString();
        if (!nonPoly.isEmpty()) {
            if (!reduzido.isEmpty() && !nonPoly.startsWith("+") && !nonPoly.startsWith("-")) {
                reduzido += "+" + nonPoly;
            } else {
                reduzido += nonPoly;
            }
        }
        // Clean up double plus/minus
        reduzido = reduzido.replaceAll("\\+\\+", "+")
                .replaceAll("\\+-", "-")
                .replaceAll("-\\+", "-");
        return reduzido;
    }

    public static void main(String[] args) {
        // alguns testes
        new Reductio("3x^2-4x^2+log2(8)-log2(16)-log2(100)+10^2");
      
    }
}
