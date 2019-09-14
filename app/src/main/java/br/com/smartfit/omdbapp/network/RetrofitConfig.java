package br.com.smartfit.omdbapp.network;

import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe responsável por configurar a API retrofit
 */
public class RetrofitConfig {
    private final Retrofit retrofit;
    private final String BASE_URL = "http://www.omdbapi.com";

    public RetrofitConfig() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", "3727d53f")
                    .build();

            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();

            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public ApiCall apiCall(){
        return this.retrofit.create(ApiCall.class);
    }
}
