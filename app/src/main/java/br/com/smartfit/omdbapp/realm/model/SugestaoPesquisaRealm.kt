package br.com.smartfit.omdbapp.realm.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class SugestaoPesquisaRealm : RealmObject() {

    @Required
    @PrimaryKey
    var idSugestao : Int? = 0
    lateinit var texto: String
}