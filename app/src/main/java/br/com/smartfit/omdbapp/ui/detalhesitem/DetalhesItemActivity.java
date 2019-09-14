package br.com.smartfit.omdbapp.ui.detalhesitem;

import android.os.Bundle;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import br.com.smartfit.omdbapp.R;
import br.com.smartfit.omdbapp.model.Item;
import br.com.smartfit.omdbapp.model.Ratings;
import br.com.smartfit.omdbapp.sqlite.ItemDao;

public class DetalhesItemActivity extends AppCompatActivity implements DetalhesItemContrato.View {

    private SimpleDraweeView imgFoto;
    private RatingBar rBarAvaliacao;
    private TextView txtTitulo, txtAno, txtDiretor, txtEstreia, txtDuracao,
            txtGenero, txtIdioma, txtElenco, txtSinopse;
    private ProgressBar pbDetalhes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_detalhes_item);
        inicializar();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }
    }

    /**
     * Volta para a tela anterior ao clicar no botão
     *
     * @param menuItem menu de itens
     * @return verdadeiro
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    /**
     * Referencia os objetos
     * Configura a toolbar
     * Faz a chamada para carregar os dados de acordo com o item selecionado na tela anterior
     */
    @Override
    public void inicializar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        imgFoto = findViewById(R.id.imgFoto);
        rBarAvaliacao = findViewById(R.id.rBarAvaliacao);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtAno = findViewById(R.id.txtAno);
        txtDiretor = findViewById(R.id.txtDiretor);
        txtEstreia = findViewById(R.id.txtEstreia);
        txtDuracao = findViewById(R.id.txtDuracao);
        txtGenero = findViewById(R.id.txtGenero);
        txtIdioma = findViewById(R.id.txtIdioma);
        txtElenco = findViewById(R.id.txtElenco);
        txtSinopse = findViewById(R.id.txtSinopse);
        pbDetalhes = findViewById(R.id.pbDetalhes);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar);
        }

        DetalhesItemContrato.Presenter presenter = new DetalhesItemPresenter(this,
                new ItemDao(getApplicationContext()));
        presenter.buscarDados(retornarID());
    }

    /**
     * Mostra os dados do item
     *
     * @param item objeto com os dados do item
     */
    @Override
    public void mostrarDados(Item item) {
        runOnUiThread(() -> {
            if (item.getImagem().equals("N/A")){
                imgFoto.setImageResource(R.drawable.img_sem_foto);
            }else {
                imgFoto.setImageURI(item.getImagem());
            }
            txtTitulo.setText(item.getTitulo());
            atualizarAvaliacao(item.getAvaliacao());
            txtAno.setText(item.getAno());
            txtDiretor.setText(item.getDiretor());
            txtEstreia.setText(item.getEstreia());
            txtDuracao.setText(item.getDuracao());
            txtGenero.setText(item.getGenero());
            txtIdioma.setText(item.getIdioma());
            txtElenco.setText(item.getElenco());
            txtSinopse.setText(item.getSinopse());
        });
        esconderPbItem();
    }

    /**
     * Mostra mensagem de erro
     *
     * @param mensagem mensagem
     */
    @Override
    public void mostrarMensagemErro(int mensagem) {
        runOnUiThread(() ->  Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show());
    }

    /**
     * Esconde a barra de progresso
     */
    @Override
    public void esconderPbItem() {
        runOnUiThread(() -> pbDetalhes.setVisibility(View.GONE));
    }

    /**
     * Retornar o ID do item
     */
    private String retornarID(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getString("idItem");
        }else {
            return "";
        }
    }

    /**
     * Atualiza os dados para carregar a avaliação
     *
     * @param listaAvaliacao lista de avaliação
     */
    private void atualizarAvaliacao(List<Ratings> listaAvaliacao){
        for(int i = 0; i < listaAvaliacao.size(); i++){
            if (listaAvaliacao.get(i).getFonte().substring(0, 8).equals("Internet")){
                String strAvaliacao = listaAvaliacao.get(i).getAvaliacao();
                int pos = strAvaliacao.indexOf("/");
                float nota = Float.parseFloat(strAvaliacao.substring(0, pos));
                int notaMax = Integer.parseInt(strAvaliacao.substring(pos + 1));
                rBarAvaliacao.setMax(notaMax);
                rBarAvaliacao.setRating(nota);
            }
        }
    }
}