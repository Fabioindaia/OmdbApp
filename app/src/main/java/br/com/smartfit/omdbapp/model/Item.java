package br.com.smartfit.omdbapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {

    @SerializedName("Title")
    private String titulo;
    @SerializedName("Year")
    private String ano;
    @SerializedName("Released")
    private String estreia;
    @SerializedName("Runtime")
    private String duracao;
    @SerializedName("Genre")
    private String genero;
    @SerializedName("Director")
    private String diretor;
    @SerializedName("Actors")
    private String elenco;
    @SerializedName("Plot")
    private String sinopse;
    @SerializedName("Language")
    private String idioma;
    @SerializedName("Poster")
    private String imagem;
    @SerializedName("Ratings")
    private List<Ratings> avaliacao;
    @SerializedName("imdbID")
    private String id;
    @SerializedName("Type")
    private String tipo;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getEstreia() {
        return estreia;
    }

    public void setEstreia(String estreia) {
        this.estreia = estreia;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public String getElenco() {
        return elenco;
    }

    public void setElenco(String elenco) {
        this.elenco = elenco;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public List<Ratings> getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(List<Ratings> avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
