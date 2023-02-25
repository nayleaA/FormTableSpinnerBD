package com.example.formulario;

public class estados {
    private String Estado;

    public estados(){
    }

    public estados(String Estado){
        this.Estado=Estado;
    }

    public String getHorarioDiasLaborales() {
        return Estado;
    }

    public void setHorarioDiasLaborales(String Estado){
        this.Estado=Estado;
    }

    public String toString(){
        return Estado;
    }
}
