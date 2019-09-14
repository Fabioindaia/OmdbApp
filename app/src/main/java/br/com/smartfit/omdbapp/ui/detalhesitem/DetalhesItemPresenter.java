package br.com.smartfit.omdbapp.ui.detalhesitem;

import br.com.smartfit.omdbapp.R;
import br.com.smartfit.omdbapp.model.Item;
import br.com.smartfit.omdbapp.network.RetrofitConfig;
import br.com.smartfit.omdbapp.sqlite.ItemDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("NullableProblems")
public class DetalhesItemPresenter extends RetrofitConfig implements DetalhesItemContrato.Presenter {

    private DetalhesItemContrato.View view;
    private ItemDao itemDao;

    DetalhesItemPresenter(DetalhesItemContrato.View view, ItemDao itemDao){
        this.view = view;
        this.itemDao = itemDao;
    }

    /**
     * Faz a chamada para carregar os dados conforme o item selecionado
     *
     * @param id id do item selecionado
     */
    @Override
    public void buscarDados(String id) {
        Call<Item> call = apiCall().buscarDadosItem(id);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        view.mostrarDados(response.body());
                        itemDao.inserirFavorito(response.body());
                    }else {
                        view.mostrarMensagemErro(R.string.erro_ws_registro_nao_encontrado);
                    }
                } else {
                    view.mostrarMensagemErro(R.string.erro_ws_sem_conexao);
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                view.mostrarMensagemErro(R.string.erro_ws_sem_conexao);
            }
        });
    }
}
