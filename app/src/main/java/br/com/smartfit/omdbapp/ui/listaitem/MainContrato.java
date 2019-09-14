package br.com.smartfit.omdbapp.ui.listaitem;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

import br.com.smartfit.omdbapp.model.Item;

public interface MainContrato {

    interface View{
        void inicializar();
        void setRecyclerViewItem();
        void setSpinnerTipo();
        void carregarListaItem(List<Item> _listaItem);
        void abrirDetalhesItemActivity(String idItem);
        void mostrarMensagemErro(int mensagem, boolean erroWs, boolean carregando);
        void mostrarPbItem();
        void esconderPbItem();
        void mostrarMensagemMain();
        void esconderMensagemMain();
    }

    interface Presenter{
        void carregarSugestaoPesquisa(MaterialSearchBar toolbar_pesquisar);
        void inserirSugestaoPesquisa(MaterialSearchBar toolbar_pesquisar);
        String setTipo(int posicao);
        void pesquisar(String titulo, String tipo);
        void buscarDados(String titulo, String tipo, int _pagina);
        void scrolledRvItem(LinearLayoutManager linearLayoutManager, String titulo, String tipo);
    }

}
