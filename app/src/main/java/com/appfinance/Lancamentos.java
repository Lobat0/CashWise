package com.appfinance;

import java.util.Date;

public class Lancamentos {

    private String email;
    private Boolean receita;
    private Boolean despesa;
    private String valor;
    private String data;
    private String categoria;

    public Lancamentos(){}

    public Lancamentos(String email,Boolean receita,Boolean despesa,String valor,String data,String categoria){
        this.email = email;
        this.receita = receita;
        this.despesa = despesa;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getReceita() {
        return receita;
    }

    public void setReceita(Boolean receita) {
        this.receita = receita;
    }

    public Boolean getDespesa() {
        return despesa;
    }

    public void setDespesa(Boolean despesa) {
        this.despesa = despesa;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
