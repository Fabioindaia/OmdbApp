package br.com.smartfit.omdbapp.model

import com.google.gson.annotations.SerializedName

class Item {

    @SerializedName("Title")
    var titulo: String? = null
    @SerializedName("Year")
    var ano: String? = null
    @SerializedName("Released")
    var estreia: String? = null
    @SerializedName("Runtime")
    var duracao: String? = null
    @SerializedName("Genre")
    var genero: String? = null
    @SerializedName("Director")
    var diretor: String? = null
    @SerializedName("Actors")
    var elenco: String? = null
    @SerializedName("Plot")
    var sinopse: String? = null
    @SerializedName("Language")
    var idioma: String? = null
    @SerializedName("Poster")
    var imagem: String? = null
    @SerializedName("Ratings")
    var avaliacao: List<Ratings>? = null
    @SerializedName("imdbID")
    var id: String? = null
    @SerializedName("Type")
    var tipo: String? = null
}