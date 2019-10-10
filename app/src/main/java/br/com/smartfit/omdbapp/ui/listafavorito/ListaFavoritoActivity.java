package br.com.smartfit.omdbapp.ui.listafavorito;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.smartfit.omdbapp.R;
import br.com.smartfit.omdbapp.model.Favorito;
import br.com.smartfit.omdbapp.realm.ItemDao;
import br.com.smartfit.omdbapp.ui.detalhesitem.DetalhesItemActivity;
import io.realm.Realm;

public class ListaFavoritoActivity extends AppCompatActivity implements ListaFavoritoContrato.View {

    private RecyclerView rvFavorito;
    private ProgressBar pbFavorito;
    private List<Favorito> listaFavorito = new ArrayList<>();
    private FavoritoAdapter favoritoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_lista_favorito);
        inicializar();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    /**
     * Referencia os objetos
     * Configura a toolbar
     * Altera o título da toolbar
     * Chama método para setar os parâmetros da recyclerview
     * Instância a classe presenter e carrega os itens favoritos salvos no SQLite
     */
    @Override
    public void inicializar() {
        ActionBar actionBar = getSupportActionBar();
        rvFavorito = findViewById(R.id.rvFavorito);
        pbFavorito = findViewById(R.id.pbFavorito);

        if (actionBar != null) {
            actionBar.setTitle(R.string.favoritos);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_voltar);
        }

        setRecyclerViewItem();
        ListaFavoritoPresenter presenter = new ListaFavoritoPresenter(this, new ItemDao(Realm.getInstance(Objects.requireNonNull(Realm.getDefaultConfiguration()))));
        presenter.buscarDados();
    }

    /**
     * Seta parâmetros da recyclerview
     */
    @Override
    public void setRecyclerViewItem() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFavorito.setLayoutManager(linearLayoutManager);
        rvFavorito.setHasFixedSize(true);

        favoritoAdapter = new FavoritoAdapter(listaFavorito, this, new ItemDao(Realm.getInstance(Objects.requireNonNull(Realm.getDefaultConfiguration()))));
        rvFavorito.setAdapter(favoritoAdapter);
    }

    /**
     * Carrega lista de itens favoritos
     *
     * @param _listaFavorito lista de itens favoritos
     */
    @Override
    public void carregarListaFavoritos(List<Favorito> _listaFavorito) {
        runOnUiThread(() ->{
            listaFavorito.clear();
            listaFavorito.addAll(_listaFavorito);
            favoritoAdapter.notifyParentDataSetChanged(false);
            esconderPbItem();
        });
    }

    /**
     * Esconde a barra de progresso
     */
    @Override
    public void esconderPbItem() {
        pbFavorito.setVisibility(View.GONE);
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
}
