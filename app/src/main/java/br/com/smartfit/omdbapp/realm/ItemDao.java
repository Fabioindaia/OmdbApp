package br.com.smartfit.omdbapp.realm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.smartfit.omdbapp.model.Favorito;
import br.com.smartfit.omdbapp.model.Item;
import br.com.smartfit.omdbapp.realm.model.FavoritoRealm;
import br.com.smartfit.omdbapp.realm.model.SugestaoPesquisaRealm;
import io.realm.Realm;
import io.realm.RealmResults;

public class ItemDao {

    private Realm realm;

    public ItemDao(Realm realm){
        this.realm = realm;
    }

    /**
     * Insere a lista de sugestões de pesquisa no banco de dados local
     *
     * @param listaPesquisa lista de sugestões de pesquisa
     */
    public void inserirSugestaoPesquisa(List listaPesquisa) {
        removerSugestaoPesquisa();
        for (int i = 0; i < listaPesquisa.size(); i++){
            SugestaoPesquisaRealm sugestaoPesquisaRealm = new SugestaoPesquisaRealm();
            sugestaoPesquisaRealm.setIdSugestao(i + 1);
            sugestaoPesquisaRealm.setTexto(listaPesquisa.get(i).toString());
            realm.beginTransaction();
            realm.copyToRealm(sugestaoPesquisaRealm);
            realm.commitTransaction();
        }
    }

    /**
     * Insere item selecionado no banco de dados local
     *
     * @param item objeto com os dados do item
     */
    public void inserirFavorito(Item item) {
        if (verificarNaoExisteFavorito(item.getId())) {
            FavoritoRealm favoritoRealm = new FavoritoRealm();
            favoritoRealm.setIdItem(Objects.requireNonNull(item.getId()));
            favoritoRealm.setTipo(Objects.requireNonNull(item.getTipo()));
            favoritoRealm.setTitulo(item.getTitulo() != null ? item.getTitulo() : "");
            favoritoRealm.setImagem(item.getImagem() != null ? item.getImagem() : "");
            realm.beginTransaction();
            realm.copyToRealm(favoritoRealm);
            realm.commitTransaction();
        }
    }

    /**
     * Remove todas sugestões de pesquisa do banco de dados local
     */
    private void removerSugestaoPesquisa() {
        RealmResults<SugestaoPesquisaRealm> listaSugestaoPesquisa = realm.where(SugestaoPesquisaRealm.class).findAll();
        realm.beginTransaction();
        listaSugestaoPesquisa.deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * Remove item selecionado do banco de dados local
     *
     * @param idItem id do item selecionado
     */
    public void removerFavorito(String idItem) {
        RealmResults<FavoritoRealm> listaFavoritoRealm = realm.where(FavoritoRealm.class).equalTo("idItem", idItem).findAll();
        realm.beginTransaction();
        listaFavoritoRealm.deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * @return a lista de sugestão de pesquisa cadastrada no banco de dados local
     */
    public List<String> retornarSugestaoPesquisa(){
        List<String> listaSugestao = new ArrayList<>();
        RealmResults<SugestaoPesquisaRealm> sugestaoPesquisaRealm = realm.where(SugestaoPesquisaRealm.class).findAll();
        for(SugestaoPesquisaRealm s : sugestaoPesquisaRealm){
            listaSugestao.add(s.getTexto());
        }
        return listaSugestao;
    }

    /**
     * Verifica se já existe o item cadastrado no banco de dados local
     *
     * @param idItem id do item
     * @return se não existe cadastro
     */
    private boolean verificarNaoExisteFavorito(String idItem){
        return realm.where(FavoritoRealm.class).equalTo("idItem", idItem).findAll().isEmpty();
    }

    /**
     * Retorna lista dos itens favoritos cadastrados no SQLite
     *
     * @return lista de favoritos
     */
    public List<Favorito> retornarFavoritos(){
        int cont = 0;
        String tipoWs, tipo;

        List<Favorito> listaFavorito = new ArrayList<>();
        while (cont <= 2){
            switch (cont){
                case 0:
                    tipoWs = "movie";
                    tipo = "Filmes";
                    break;
                case 1:
                    tipoWs = "series";
                    tipo = "Séries";
                    break;
                default:
                    tipoWs = "episode";
                    tipo = "Episódios";
            }
            Favorito favorito = new Favorito();
            List<Item> listaItem = new ArrayList<>();

            RealmResults<FavoritoRealm> favoritoRealm = realm.where(FavoritoRealm.class).equalTo("tipo", tipoWs) .findAll();
            for(FavoritoRealm f : favoritoRealm){
                Item item = new Item();
                item.setId(f.getIdItem());
                item.setImagem(f.getImagem());
                item.setTitulo(f.getTitulo());
                listaItem.add(item);
            }

            favorito.setTipo(tipo);
            favorito.setListaItem(listaItem);
            listaFavorito.add(favorito);
            cont ++;
        }
        return listaFavorito;
    }
}