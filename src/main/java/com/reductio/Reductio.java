package com.reductio;

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

    public Reductio reduct() {

        return new Reductio(this.expr);

    }

    public Function indendify() {

        return new Function();

    }

    public void format() {

    }
}
