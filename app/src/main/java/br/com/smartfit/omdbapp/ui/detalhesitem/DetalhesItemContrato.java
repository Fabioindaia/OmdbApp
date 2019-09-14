package br.com.smartfit.omdbapp.ui.detalhesitem;

import br.com.smartfit.omdbapp.model.Item;

public interface DetalhesItemContrato {

    interface View{
        void inicializar();
        void mostrarDados(Item item);
        void mostrarMensagemErro(int mensagem);
        void esconderPbItem();
    }

    interface Presenter{
        void buscarDados(String id);
    }
}
