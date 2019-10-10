package br.com.smartfit.omdbapp.ui.detalhesitem

import br.com.smartfit.omdbapp.model.Item

interface DetalhesItemContrato {

    interface View {
        fun inicializar()
        fun mostrarDados(item: Item)
        fun mostrarMensagemErro(mensagem: Int)
        fun esconderPbItem()
    }

    interface Presenter {
        fun buscarDados(id: String?)
    }
}