package com.reductio;

import com.reductio.funcoes.Function;


public class Reductio {
   
    private String expr;
    
    public Reductio(String expr){
        this.expr = expr;
    }
    
    public Reductio reduct(){

        return new Reductio(this.expr);
        
    }

    public Function indendify(){

        return new Function();
    
    }

    public void format(){
        
    }
}
 