package br.com.smartfit.omdbapp.ui.listaitem;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import br.com.smartfit.omdbapp.R;
import br.com.smartfit.omdbapp.model.Item;
import br.com.smartfit.omdbapp.sqlite.ItemDao;
import br.com.smartfit.omdbapp.ui.detalhesitem.DetalhesItemActivity;
import br.com.smartfit.omdbapp.ui.listafavorito.ListaFavoritoActivity;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContrato.View, MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar toolbar_pesquisar;
    private Spinner spnTipo;
    private RecyclerView rvItem;
    private ProgressBar pbItem;
    private ConstraintLayout constraintLayoutMensagem;
    private MainContrato.Presenter presenter;
    private List<Item> listaItem = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SQLiteDatabase bd;
    private String titulo = "", tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        inicializar();

        toolbar_pesquisar.getMenu().setOnMenuItemClickListener((MenuItem item) ->{
            if (item.getItemId() == R.id.mnuFavorito){
                startActivity(new Intent(this, ListaFavoritoActivity.class));
                return true;
            }
            return false;
        });

        spnTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipo = presenter.setTipo(position);
                listaItem.clear();
                itemAdapter.notifyDataSetChanged();
                if (!titulo.isEmpty()){
                   presenter.pesquisar(titulo, tipo);
                }else {
                    mostrarMensagemMain();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Fecha conexão com o banco de dados SQLite
     * Insere a sugestão de pesquisa no SQLite
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.inserirSugestaoPesquisa(toolbar_pesquisar);
        bd.close();
    }

    /**
     * Referencia os objetos
     * Cria o banco de dados SQLite
     * Configura a toolbar
     * Altera o título da toolbar
     * Chama método para setar os dados do spinner
     * Chama método para setar os parâmetros da recyclerview
     * Instância a classe presenter e carrega as sugestões para pesquisa salvas no SQLite
     */
    @Override
    public void inicializar() {
        toolbar_pesquisar = findViewById(R.id.toolbar_pesquisar);
        spnTipo = findViewById(R.id.spnTipo);
        rvItem = findViewById(R.id.rvItem);
        pbItem = findViewById(R.id.pbItem);
        constraintLayoutMensagem = findViewById(R.id.constraintLayoutMensagem);

        ButterKnife.bind(this);
        bd = openOrCreateDatabase("OmbdApp.db", Context.MODE_PRIVATE, null);
        toolbar_pesquisar.setOnSearchActionListener(this);
        toolbar_pesquisar.inflateMenu(R.menu.menu_toolbar_pesquisar);

        setSpinnerTipo();
        setRecyclerViewItem();
        presenter = new MainPresenter(this, new ItemDao(getApplicationContext()), itemAdapter);
        presenter.carregarSugestaoPesquisa(toolbar_pesquisar);
    }

    /**
     * Seta parâmetros da recyclerview
     */
    @Override
    public void setRecyclerViewItem() {
        linearLayoutManager = new LinearLayoutManager(this);
        rvItem.setLayoutManager(linearLayoutManager);
        rvItem.setHasFixedSize(true);
        itemAdapter = new ItemAdapter(listaItem, this);
        rvItem.setAdapter(itemAdapter);
        rvItem.addOnScrollListener(recyclerViewOnScrollListener);
    }

    /**
     * Seta dados da spinner
     */
    @Override
    public void setSpinnerTipo() {
        ArrayAdapter<CharSequence> tipoAdapter;
        tipoAdapter = ArrayAdapter.createFromResource(this, R.array.tipo, R.layout.spinner_item);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipo.setAdapter(tipoAdapter);
    }

    /**
     * Carrega lista de itens
     *
     * @param _listaItem lista de itens
     */
    @Override
    public void carregarListaItem(List<Item> _listaItem) {
        runOnUiThread(() ->{
            listaItem.addAll(_listaItem);
            itemAdapter.notifyDataSetChanged();
            esconderPbItem();
        });
    }

    /**
     * Abre a tela de detalhes do item selecionadi
     *
     * @param idItem id do item
     */
    @Override
    public void abrirDetalhesItemActivity(String idItem) {
        Intent intent = new Intent(this, DetalhesItemActivity.class);
        intent.putExtra("idItem", idItem);

        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);
    }

    /**
     * Mostra mensagem de erro para o usuário
     */
    @Override
    public void mostrarMensagemErro(int mensagem, boolean erroWs, boolean carregando) {
        runOnUiThread(() -> {
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

            if (erroWs) {
                esconderPbItem();
                if (carregando) {
                    listaItem.add(new Item());
                    int position = listaItem.size() - 1;
                    listaItem.remove(position);
                    itemAdapter.notifyItemRemoved(position);
                } else {
                    mostrarMensagemMain();
                }
            }
        });
    }

    /**
     * Mostra a barra de progresso
     */
    @Override
    public void mostrarPbItem() {
        runOnUiThread(() -> pbItem.setVisibility(View.VISIBLE));
    }

    /**
     * Esconde a barra de progresso
     */
    @Override
    public void esconderPbItem() {
        runOnUiThread(() -> pbItem.setVisibility(View.GONE));
    }

    /**
     * Mostra a mensagem principal
     */
    @Override
    public void mostrarMensagemMain() {
        runOnUiThread(() -> {
            rvItem.setVisibility(View.GONE);
            constraintLayoutMensagem.setVisibility(View.VISIBLE);
        });
    }

    /**
     * Esconde a mensagem principal
     */
    @Override
    public void esconderMensagemMain() {
        runOnUiThread(() -> {
            rvItem.setVisibility(View.VISIBLE);
            constraintLayoutMensagem.setVisibility(View.GONE);
        });
    }

    /**
     * Verifica se a toolbar está no estado de pesquisa
     *
     * @param enabled ativa
     */
    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (enabled) {
            toolbar_pesquisar.showSuggestionsList();
        }
    }

    /**
     * Inicia a busca
     *
     * @param text texto para pesquisa
     */
    @Override
    public void onSearchConfirmed(CharSequence text) {        
        listaItem.clear();
        titulo = text.toString();
        presenter.pesquisar(titulo, tipo);
    }

    /**
     * Esconde a lista de sugestão ao clicar no botão de voltar da toolbar
     *
     * @param buttonCode código do botão
     */
    @Override
    public void onButtonClicked(int buttonCode) {
        if (buttonCode == MaterialSearchBar.BUTTON_BACK){
            toolbar_pesquisar.hideSuggestionsList();
        }
    }

    /**
     * Controle do scroll do recyclerview
     */
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            presenter.scrolledRvItem(linearLayoutManager, titulo, tipo);
        }
    };
}
