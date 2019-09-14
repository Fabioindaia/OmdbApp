package br.com.smartfit.omdbapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReturnData {

    @SerializedName("Search")
    private List<Item> listaItem;
    @SerializedName("totalResults")
    private int totalRegistro;
    @SerializedName("response")
    private boolean resposta;
    @SerializedName("Error")
    private String erro;

    public List<Item> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<Item> listaItem) {
        this.listaItem = listaItem;
    }

    public int getTotalRegistro() {
        return totalRegistro;
    }

    public void setTotalRegistro(int totalRegistro) {
        this.totalRegistro = totalRegistro;
    }

    public boolean isResposta() {
        return resposta;
    }

    public void setResposta(boolean resposta) {
        this.resposta = resposta;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }
}
