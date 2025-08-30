package com.reductio;

import com.reductio.funcoes.Function;


public class Reductio {
   
    private String expr;
    
    public Reductio(String expr){
        this.expr = expr;
    }
    
    public Function reduct(){
        return new Function();
    }
}
