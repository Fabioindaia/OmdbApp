package br.com.smartfit.omdbapp.ui.detalhesitem

import br.com.smartfit.omdbapp.R
import br.com.smartfit.omdbapp.model.Item
import br.com.smartfit.omdbapp.network.RetrofitConfig
import br.com.smartfit.omdbapp.realm.ItemDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalhesItemPresenter internal constructor(private val view: DetalhesItemContrato.View, private val itemDao: ItemDao) : RetrofitConfig(), DetalhesItemContrato.Presenter {

    /**
     * Faz a chamada para carregar os dados conforme o item selecionado
     *
     * @param id id do item selecionado
     */
    override fun buscarDados(id: String?) {
        val call = apiCall().buscarDadosItem(id)
        call.enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        view.mostrarDados(response.body()!!)
                        itemDao.inserirFavorito(response.body()!!)
                    } else {
                        view.mostrarMensagemErro(R.string.erro_ws_registro_nao_encontrado)
                    }
                } else {
                    view.mostrarMensagemErro(R.string.erro_ws_sem_conexao)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                view.mostrarMensagemErro(R.string.erro_ws_sem_conexao)
            }
        })
    }
}