package br.com.smartfit.omdbapp.model;

import com.google.gson.annotations.SerializedName;

public class Ratings {

    @SerializedName("Source")
    private String fonte;
    @SerializedName("Value")
    private String avaliacao;

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }
}
