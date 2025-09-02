package com.reductio;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.reductio.funcoes.Function;

class Termo {
    private Double coeficiente;
    private String variavel;
    private Double expoente;

    public Termo(Double coeficiente, String variavel, Double expoente) {
        this.coeficiente = coeficiente;
        this.variavel = variavel;
        this.expoente = expoente;
    }

    @Override
    public String toString() {
        return coeficiente + (variavel == null ? "" : variavel + "^" + expoente);
    }
}

public class Reductio {

    private String expr;

    public Reductio(String expr) {
        this.expr = expr;
    }

    public static void main(String[] args) {
        Reductio reductio = new Reductio("3x^2+4x-5");
        reductio.format();
    }

    public Reductio reduct() {

        return this;

    }

    public Function indendify() {

        return new Function();

    }

    public void format() {
        this.expr = this.expr.replaceAll("\\s+", "");
        String[] termosString = this.expr.split("(?=[+-])");
        for (String termo : termosString) {
            System.out.println(termo);
        }
        ArrayList<Termo> termos = new ArrayList<>();
        for (String termo : termosString) {
            Pattern pattern = Pattern.compile("([+-]?\\d*)?([a-zA-Z]+)?(\\^(\\d+))?");
            Matcher matcher = pattern.matcher(termo);

            if (matcher.matches()) {
                String coefStr = matcher.group(1);
                String variavel = matcher.group(2);
                String expStr = matcher.group(4);

                // Coeficiente
                Double coeficiente;
                if (coefStr == null || coefStr.isEmpty() || coefStr.equals("+") || coefStr.equals("-")) {
                    // Se for só "+" ou "-", assume ±1
                    coeficiente = (coefStr != null && coefStr.equals("-")) ? -1.0 : 1.0;
                } else {
                    coeficiente = Double.parseDouble(coefStr);
                }

                // Expoente
                Double expoente = expStr != null
                        ? Double.parseDouble(expStr)
                        : (variavel != null ? 1.0 : 0.0);

                termos.add(new Termo(coeficiente, variavel, expoente));
            }

        }

    }
}
