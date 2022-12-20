package com.appfinance;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Dicas  implements Serializable {

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    private String key;

    private String dicas;

    public Dicas(){}

    public Dicas(String dicas){
        this.dicas = dicas;
    }

    public String getDicas() {
        return dicas;
    }

    public void setDicas(String dicas) {
        this.dicas = dicas;
    }


}