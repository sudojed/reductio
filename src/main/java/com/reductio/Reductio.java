package com.reductio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.reductio.funcoes.Function;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

@Getter
public class Reductio {

    private String expr;

    public Reductio(String expr) {
        this.expr = expr;
    }

    public static void main(String[] args) {
        Reductio reductio = new Reductio("3x^2-4x^2-5");
        reductio.polinomialReduct();
    }

    public Reductio reduct() {

        return this;

    }

    public Function indendify() {

        return new Function();

    }

    private void polinomialReduct() {
        this.expr = this.expr.replaceAll("\\s+", "");
        String[] termosString = this.expr.split("(?=[+-])");
        ArrayList<Termo> termos = new ArrayList<>();

        for (String termo : termosString) {
            Pattern pattern = Pattern.compile("([+-]?\\d*)?([a-zA-Z]+)?(\\^(\\d+))?");
            Matcher matcher = pattern.matcher(termo);

            if (matcher.matches()) {
                String coefStr = matcher.group(1);
                String variavel = matcher.group(2);
                String expStr = matcher.group(4);

                Double coeficiente;
                if (coefStr == null || coefStr.isEmpty() || coefStr.equals("+") || coefStr.equals("-")) {
                    coeficiente = (coefStr != null && coefStr.equals("-")) ? -1.0 : 1.0;
                } else {
                    coeficiente = Double.parseDouble(coefStr);
                }

                Double expoente = expStr != null
                        ? Double.parseDouble(expStr)
                        : (variavel != null ? 1.0 : 0.0);

                termos.add(new Termo(coeficiente, variavel, expoente));
            }
        }

        // agora agrupamos por (variável + expoente)
        Map<String, Double> termosMap = new HashMap<>();

        for (Termo termo : termos) {
            String chave = (termo.getVariavel() == null ? "" : termo.getVariavel())
                    + ":" + termo.getExpoente();

            termosMap.merge(
                    chave,
                    termo.getCoeficiente(),
                    Double::sum);
        }

        // reconstruir expressão reduzida
        StringBuilder reduzida = new StringBuilder();
        for (Map.Entry<String, Double> entry : termosMap.entrySet()) {
            double coef = entry.getValue();
            if (coef == 0)
                continue; // ignora termos anulados

            String[] partes = entry.getKey().split(":");
            String variavel = partes[0].isEmpty() ? "" : partes[0];
            double expoente = Double.parseDouble(partes[1]);

            // monta o termo de volta em formato string
            if (reduzida.length() > 0 && coef > 0) {
                reduzida.append("+");
            }

            if (expoente == 0 || variavel.isEmpty()) {
                reduzida.append(coef);
            } else if (expoente == 1) {
                reduzida.append(coef).append(variavel);
            } else {
                reduzida.append(coef).append(variavel).append("^").append((int) expoente);
            }
        }

        this.expr = reduzida.toString();

        System.out.println("Expressão reduzida: " + this.expr);
    }

}
