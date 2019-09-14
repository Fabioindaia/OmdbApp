package br.com.smartfit.omdbapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.smartfit.omdbapp.model.Favorito;
import br.com.smartfit.omdbapp.model.Item;

public class ItemDao {

    private BaseDao auxBd;
    private SQLiteDatabase bd;

    /**
     * Passa o contexto
     *
     * @param context contexto
     */
    public ItemDao(Context context){
        auxBd = new BaseDao(context);
    }


    /**
     * Insere a lista de sugestões de pesquisa no banco SQLite
     *
     * @param listaPesquisa lista de sugestões de pesquisa
     *
     */
    public void inserirSugestaoPesquisa(List listaPesquisa) {
        bd = auxBd.getWritableDatabase();
        bd.delete("SugestaoPesquisa", null, null);
        ContentValues cv = new ContentValues();
        for (int i = 0; i < listaPesquisa.size(); i++){
            cv.put("texto", listaPesquisa.get(i).toString());
            bd.insert("SugestaoPesquisa", null, cv);
        }
        bd.close();
    }

    /**
     * Insere item selecionado no banco SQLite
     *
     * @param item objeto com os dados do item
     *
     */
    public void inserirFavorito(Item item) {
        if (verificarNaoExisteFavorito(item.getId())) {
            bd = auxBd.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("iditem", item.getId());
            cv.put("tipo", item.getTipo());
            cv.put("titulo", item.getTitulo());
            cv.put("imagem", item.getImagem());
            bd.insert("Favoritos", null, cv);
            bd.close();
        }
    }

    /**
     * Remove item selecionado do banco SQLite
     *
     * @param idItem id do item selecionado
     *
     */
    public void removerFavorito(String idItem) {
        bd = auxBd.getWritableDatabase();
        bd.delete("Favoritos", "iditem = '" + idItem + "'", null);
        bd.close();
    }

    /**
     * @return a lista de sugestão de pesquisa cadastrada no banco SQLite
     */
    public List<String> retornarSugestaoPesquisa(){
        List<String> listSugestao = new ArrayList<>();
        bd = auxBd.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM SugestaoPesquisa", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listSugestao.add(cursor.getString(cursor.getColumnIndex("texto")));
                cursor.moveToNext();
            }
            cursor.close();
        }
        bd.close();
        return listSugestao;
    }

    /**
     * Verifica se já existe o item cadastrado no banco SQLite
     *
     * @param idItem id do item
     * @return se não existe cadastro
     */
    private boolean verificarNaoExisteFavorito(String idItem){
        boolean naoExiste = true;
        bd = auxBd.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT id FROM Favoritos WHERE iditem = '" + idItem + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0){
                naoExiste = false;
            }
            cursor.close();
        }
        bd.close();
        return naoExiste;
    }

    /**
     * Retorna lista dos itens favoritos cadastrados no SQLite
     *
     * @return lista de favoritos
     */
    public List<Favorito> retornarFavoritos(){
        int cont = 0;
        String tipoWs, tipo;

        bd = auxBd.getReadableDatabase();
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
            Cursor cursor = bd.rawQuery("SELECT * FROM Favoritos WHERE tipo = '" + tipoWs + "' ORDER BY titulo", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Item item = new Item();
                    item.setId(cursor.getString(cursor.getColumnIndex("iditem")));
                    item.setImagem(cursor.getString(cursor.getColumnIndex("imagem")));
                    item.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
                    listaItem.add(item);
                    cursor.moveToNext();
                }
                cursor.close();
            }

            favorito.setTipo(tipo);
            favorito.setListaItem(listaItem);
            listaFavorito.add(favorito);
            cont ++;
        }
        bd.close();
        return listaFavorito;
    }
}
