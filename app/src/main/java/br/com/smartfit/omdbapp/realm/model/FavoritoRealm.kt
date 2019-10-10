package br.com.smartfit.omdbapp.realm.model

import io.realm.RealmObject
import io.realm.annotations.Required

open class FavoritoRealm : RealmObject() {

    @Required
    var idItem : String? = null
    var titulo : String? = null
    var tipo : String? = null
    var imagem : String? = null
}