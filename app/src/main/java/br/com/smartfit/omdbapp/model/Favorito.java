package br.com.smartfit.omdbapp.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class Favorito implements Parent<Item> {

    private String tipo;
    private List<Item> listaItem;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Item> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<Item> listaItem) {
        this.listaItem = listaItem;
    }

    @Override
    public List<Item> getChildList() {
        return listaItem;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}