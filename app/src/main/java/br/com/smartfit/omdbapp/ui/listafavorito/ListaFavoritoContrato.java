package br.com.smartfit.omdbapp.ui.listafavorito;

import java.util.List;

import br.com.smartfit.omdbapp.model.Favorito;

public interface ListaFavoritoContrato {

    interface View{
        void inicializar();
        void setRecyclerViewItem();
        void carregarListaFavoritos(List<Favorito> _listaFavorito);
        void esconderPbItem();
        void abrirDetalhesItemActivity(String idItem);
    }

    interface Presenter{
        void buscarDados();
    }
}
