package br.com.smartfit.omdbapp.ui.detalhesitem

import android.os.Bundle
import android.transition.Explode
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.smartfit.omdbapp.R
import br.com.smartfit.omdbapp.model.Item
import br.com.smartfit.omdbapp.model.Ratings
import br.com.smartfit.omdbapp.realm.ItemDao
import com.facebook.drawee.backends.pipeline.Fresco
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detalhes_item.*

class DetalhesItemActivity : AppCompatActivity(), DetalhesItemContrato.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_detalhes_item)
        inicializar()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Explode()
        }
    }

    /**
     * Volta para a tela anterior ao clicar no botão
     *
     * @param menuItem menu de itens
     * @return verdadeiro
     */
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    /**
     * Configura a toolbar
     * Faz a chamada para carregar os dados de acordo com o item selecionado na tela anterior
     */
    override fun inicializar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_voltar)    
        }

        val presenter = DetalhesItemPresenter(this,ItemDao(Realm.getInstance(Realm.getDefaultConfiguration()!!)))
        presenter.buscarDados(retornarID())
    }

    /**
     * Mostra os dados do item
     *
     * @param item objeto com os dados do item
     */
    override fun mostrarDados(item: Item) {
        runOnUiThread {
            if (item.imagem == "N/A") {
                imgFoto.setBackgroundResource(R.drawable.img_sem_foto)
            } else {
                imgFoto.setImageURI(item.imagem)
            }
            
            txtTitulo.text = item.titulo
            item.avaliacao?.let { atualizarAvaliacao(it) }
            txtAno.text = item.ano
            txtDiretor.text = item.diretor
            txtEstreia.text = item.estreia
            txtDuracao.text = item.duracao
            txtGenero.text = item.genero
            txtIdioma.text = item.idioma
            txtElenco.text = item.elenco
            txtSinopse.text = item.sinopse
        }
        esconderPbItem()
    }

    /**
     * Mostra mensagem de erro
     *
     * @param mensagem mensagem
     */
    override fun mostrarMensagemErro(mensagem: Int) {
        runOnUiThread { Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show() }
    }

    /**
     * Esconde a barra de progresso
     */
    override fun esconderPbItem() {
        runOnUiThread { pbDetalhes!!.visibility = View.GONE }
    }

    /**
     * Retornar o ID do item
     */
    private fun retornarID(): String? {
        val bundle = intent.extras
        return if (bundle != null) bundle.getString("idItem") else ""
    }

    /**
     * Atualiza os dados para carregar a avaliação
     *
     * @param listaAvaliacao lista de avaliação
     */
    private fun atualizarAvaliacao(listaAvaliacao: List<Ratings>) {
        for (i in listaAvaliacao.indices) {
            if (listaAvaliacao[i].fonte.substring(0, 8) == "Internet") {
                val strAvaliacao = listaAvaliacao[i].avaliacao
                val pos = strAvaliacao.indexOf("/")
                val nota = java.lang.Float.parseFloat(strAvaliacao.substring(0, pos))
                val notaMax = Integer.parseInt(strAvaliacao.substring(pos + 1))
                rBarAvaliacao!!.max = notaMax
                rBarAvaliacao!!.rating = nota
            }
        }
    }
}