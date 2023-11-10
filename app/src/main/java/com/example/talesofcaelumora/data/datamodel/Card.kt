package com.example.talesofcaelumora.data.datamodel

import com.example.talesofcaelumora.R
import com.google.firebase.firestore.PropertyName

//muss eine data class sein weil ansonsten die copy Funktion des GameDataApiService
//nicht funktioniert, die automatisch die FirestoreId zuweist

data class Card(
    val id: String,
    @get: PropertyName("card_name")val cardName: String,
    @get: PropertyName("card_type")val cardType: String,
    @get: PropertyName("rarity")val rarity: String,
    @get: PropertyName("release_date")val releaseDate: String,

    @get: PropertyName("type")val type: String,
    @get: PropertyName("img_src")val imgSrc: String,
    @get: PropertyName("hp")val hp: Int,

    @get: PropertyName("first_ability_name")val firstAbilityName: String,
    @get: PropertyName("first_ability_type")val firstAbilityType: String,
    @get: PropertyName("first_ability_points")val firstAbilityPoints: Int,
    @get: PropertyName("first_ability_costs")val firstAbilityCosts: Array<String>,
    @get: PropertyName("first_ability_description")val firstAbilityDescription: String,

    @get: PropertyName("sec_ability_name")val secAbilityName: String,
    @get: PropertyName("sec_ability_type")val secAbilityType: String,
    @get: PropertyName("sec_ability_points")val secAbilityPoints: Int,
    @get: PropertyName("sec_ability_costs")val secAbilityCosts: Array<String>,
    @get: PropertyName("sec_ability_description")val secAbilityDescription: String,
    @get: PropertyName("selected")var selected: Boolean,
    @get: PropertyName("current_hp")var currentHp: Int
) {
/*
    @JvmOverloads
    constructor(
        cardName: String = "Fehler",
        cardType: String = "",
        rarity: String = "",
        releaseDate: String = "",

        type: String = "",
        imgSrc: String = "https://firebasestorage.googleapis.com/v0/b/tales-of-caelumero.appspot.com/o/card%2Fimages%2Fland_air_tal.jpeg?alt=media&token=2168b2ee-9ff4-4c9b-a07d-afb5552148e7",
        hp: Int = 0,

        firstAbilityName: String = "",
        firstAbilityType: String = "",
        firstAbilityPoints: Int = 0,
        firstAbilityCosts: Array<String> = emptyArray<String>(),
        firstAbilityDescription: String = "",

        secAbilityName: String = "",
        secAbilityType: String = "",
        secAbilityPoints: Int = 0,
        secAbilityCosts: Array<String> = emptyArray(),
        secAbilityDescription: String = "",
        selected: Boolean = false,
        currentHp: Int = 0
    ) : this(
        id = "",
        cardName = cardName,
        cardType = cardType,
        rarity = rarity,
        releaseDate = releaseDate,
        type = type,
        imgSrc = imgSrc,
        hp = hp,
        firstAbilityName = firstAbilityName,
        firstAbilityType = firstAbilityType,
        firstAbilityPoints = firstAbilityPoints,
        firstAbilityCosts = firstAbilityCosts,
        firstAbilityDescription = firstAbilityDescription,
        secAbilityName = secAbilityName,
        secAbilityType = secAbilityType,
        secAbilityPoints = secAbilityPoints,
        secAbilityCosts = secAbilityCosts,
        secAbilityDescription = secAbilityDescription,
        selected = selected,
        currentHp = currentHp
    )
    */

    constructor(map: Map<String, Any>) : this(
        id = map["id"] as String,
        cardName = map["card_name"] as String,
        cardType = map["card_type"] as String,
        rarity = map["rarity"] as String,
        releaseDate = map["release_date"] as String,

        type = map["type"] as String,
        imgSrc = map["img_src"] as String,
        hp = (map["hp"] as Long).toInt(),

        firstAbilityName = map["first_ability_name"] as String,
        firstAbilityType = map["first_ability_type"] as String,
        firstAbilityPoints = (map["first_ability_points"] as Long).toInt(),
        firstAbilityCosts = (map["first_ability_costs"] as List<*>).map { it as String }.toTypedArray(),
        firstAbilityDescription = map["first_ability_description"] as String,

        secAbilityName = map["sec_ability_name"] as String,
        secAbilityType = map["sec_ability_type"] as String,
        secAbilityPoints = (map["sec_ability_points"] as Long).toInt(),
        secAbilityCosts = (map["sec_ability_costs"] as List<*>).map { it as String }.toTypedArray(),
        secAbilityDescription = map["sec_ability_description"] as String,

        selected = map["selected"] as Boolean,
        currentHp = (map["current_hp"] as Long).toInt()
    )


}
