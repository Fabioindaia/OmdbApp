package br.com.smartfit.omdbapp.network;

import br.com.smartfit.omdbapp.model.Item;
import br.com.smartfit.omdbapp.model.ReturnData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface responsável para fazer a chamada com a API
 */
public interface ApiCall {

    @GET("/")
    Call<ReturnData> listarItem(@Query("s") String titulo,
                                @Query("type") String tipo,
                                @Query("page") int pagina);

    @GET("/")
    Call<Item> buscarDadosItem(@Query("i") String idItem);
}
