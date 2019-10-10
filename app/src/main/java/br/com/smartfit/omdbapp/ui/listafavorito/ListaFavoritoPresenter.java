package br.com.smartfit.omdbapp.ui.listafavorito;

import br.com.smartfit.omdbapp.realm.ItemDao;

public class ListaFavoritoPresenter implements ListaFavoritoContrato.Presenter {

    private ListaFavoritoContrato.View view;
    private ItemDao itemDao;

    ListaFavoritoPresenter(ListaFavoritoContrato.View view, ItemDao itemDao){
        this.view = view;
        this.itemDao = itemDao;
    }

    /**
     * Faz a chamada para carregar os dados do banco SQLite
     */
    @Override
    public void buscarDados() {
        view.carregarListaFavoritos(itemDao.retornarFavoritos());
    }
}
