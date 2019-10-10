package br.com.smartfit.omdbapp.ui.listaitem;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

import br.com.smartfit.omdbapp.R;
import br.com.smartfit.omdbapp.model.ReturnData;
import br.com.smartfit.omdbapp.network.RetrofitConfig;
import br.com.smartfit.omdbapp.realm.ItemDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter extends RetrofitConfig implements MainContrato.Presenter {

    private MainContrato.View view;
    private ItemDao itemDao;
    private ItemAdapter itemAdapter;
    private final int N_REGISTRO_PAGINA = 10;
    private int pagina = 1, totalPagina;
    private boolean carregando = true;

    MainPresenter(MainContrato.View view, ItemDao itemDao, ItemAdapter itemAdapter){
        this.view = view;
        this.itemDao = itemDao;
        this.itemAdapter = itemAdapter;
    }

    /**
     * Carrega na toolbar a sugestão de pesquisa cadastrada no banco SQLite
     *
     * @param toolbar_pesquisar toolbar de pesquisa
     */
    @Override
    public void carregarSugestaoPesquisa(MaterialSearchBar toolbar_pesquisar) {
        List<String> listaSugestao = itemDao.retornarSugestaoPesquisa();
        if (listaSugestao.size()>0){
            toolbar_pesquisar.setLastSuggestions(listaSugestao);
        }
    }

    /**
     * Insere a lista de sugestão de pesquisa no banco SQLite
     *
     * @param toolbar_pesquisar toolbar de pesquisa
     */
    @Override
    public void inserirSugestaoPesquisa(MaterialSearchBar toolbar_pesquisar) {
        if (toolbar_pesquisar.getLastSuggestions().size() > 0) {
            itemDao.inserirSugestaoPesquisa(toolbar_pesquisar.getLastSuggestions());
        }
    }

    /**
     * Seta o tipo de acordo com a opção seleciona na spinner
     *
     * @param posicao posição selecionada
     * @return retorna tipo
     */
    @Override
    public String setTipo(int posicao) {
        switch (posicao){
            case 0:
                return "movie";
            case 1:
                return "series";
            default:
                return "episode";
        }
    }

    /**
     * Verifica a quantidade de caracteres do campo de pesquisa
     * Esconde a mensagem principal e mostra a barra de progresso
     * Faz a chamada para iniciar a pesquisa
     *
     * @param titulo título para pesquisa
     * @param tipo tipo para pesquisa
     */
    @Override
    public void pesquisar(String titulo, String tipo) {
        pagina = 1;
        if (titulo.length() < 4){
            view.mostrarMensagemErro(R.string.erro_qtd_caracteres_pesquisa, false, false);
        }else{
            view.esconderMensagemMain();
            view.mostrarPbItem();
            buscarDados(titulo, tipo, pagina);
        }
    }

    /**
     * Faz a chamada para carregar os dados conforme pesquisa
     *
     * @param titulo título para pesquisa
     * @param tipo tipo para pesquisa
     * @param _pagina número página para pesquisa
     */
    @Override
    public void buscarDados(String titulo, String tipo, int _pagina) {
        Call<ReturnData> call = apiCall().listarItem(titulo, tipo, _pagina);
        //noinspection NullableProblems
        call.enqueue(new Callback<ReturnData>() {
            @Override
            public void onResponse(Call<ReturnData> call, Response<ReturnData> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getErro() == null) {
                            itemAdapter.setTotalRegistro(response.body().getTotalRegistro());
                            setTotalPagina(response.body().getTotalRegistro());
                            view.carregarListaItem(response.body().getListaItem());
                        }else{
                            setErro(R.string.erro_ws_registro_nao_encontrado);
                        }
                    }else{
                        setErro(R.string.erro_ws_registro_nao_encontrado);
                    }
                } else {
                    setErro(R.string.erro_ws_sem_conexao);
                }
                carregando = false;
            }

            @Override
            public void onFailure(Call<ReturnData> call, Throwable t) {
                setErro(R.string.erro_ws_sem_conexao);
            }
        });
    }

    /**
     * Faz a chamada para carregar novos personagens quando atingir o final da lista
     *
     * @param linearLayoutManager linearlayoutmanager
     * @param titulo título para pesquisa
     * @param tipo tipo para pesquisa
     */
    @Override
    public void scrolledRvItem(LinearLayoutManager linearLayoutManager, String titulo, String tipo) {
        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

        if (!carregando && pagina < totalPagina) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= N_REGISTRO_PAGINA) {

                carregando = true;
                pagina ++;
                buscarDados(titulo, tipo, pagina);
            }
        }
    }

    /**
     * Seta os dados do erro
     *
     * @param mensagem mensagem de erro
     */
    private void setErro(int mensagem){
        view.mostrarMensagemErro(mensagem, true, carregando);
        if (carregando){
            pagina = pagina - 1;
        }
    }

    /**
     * Seta a quantidade total de itens retornados do webservice
     *
     * @param totalRegistro quantidade total de itens
     */
    private void setTotalPagina(int totalRegistro) {
        double dTotalPagina = Math.ceil((double)totalRegistro / (double)N_REGISTRO_PAGINA);
        totalPagina = (int)dTotalPagina;
    }
}
