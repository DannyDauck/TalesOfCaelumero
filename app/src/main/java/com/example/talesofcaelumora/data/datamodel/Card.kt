package com.example.talesofcaelumora.data.datamodel

import com.example.talesofcaelumora.R
import com.google.firebase.firestore.PropertyName

//muss eine data class sein weil ansonsten die copy Funktion des GameDataApiService
//nicht funktioniert, die automatisch die FirestoreId zuweist

data class Card(
    val id: String,
    val cardName: String,
    val cardType: String,
    val rarity: String,
    val releaseDate: String,

    val type: String,
    val imgSrc: String,
    val hp: Int,

    val firstAbilityName: String,
    val firstAbilityType: String,
    val firstAbilityPoints: Int,
    val firstAbilityCosts: List<String>,
    val firstAbilityDescription: String,

    val secAbilityName: String,
    val secAbilityType: String,
    val secAbilityPoints: Int,
    val secAbilityCosts: List<String>,
    val secAbilityDescription: String,
    var selected: Boolean,
    var currentHp: Int,
    var protected: Boolean,
    var used: Boolean,
) {

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
        firstAbilityCosts = (map["first_ability_costs"] as List<*>).map { it as String },
        firstAbilityDescription = map["first_ability_description"] as String,

        secAbilityName = map["sec_ability_name"] as String,
        secAbilityType = map["sec_ability_type"] as String,
        secAbilityPoints = (map["sec_ability_points"] as Long).toInt(),
        secAbilityCosts = (map["sec_ability_costs"] as List<*>).map { it as String },
        secAbilityDescription = map["sec_ability_description"] as String,

        selected = map["selected"] as Boolean,
        currentHp = (map["current_hp"] as Long).toInt(),
        protected = false,
        used = false,
    )

    fun toCard(): Card {
        // Create a new instance of Card and copy all properties
        return Card(
            id = this.id,
            cardName = this.cardName,
            cardType = this.cardType,
            rarity = this.rarity,
            releaseDate = this.releaseDate,
            type = this.type,
            imgSrc = this.imgSrc,
            hp = this.hp,
            firstAbilityName = this.firstAbilityName,
            firstAbilityType = this.firstAbilityType,
            firstAbilityPoints = this.firstAbilityPoints,
            firstAbilityCosts = this.firstAbilityCosts.toList(), // Create a new list to avoid reference sharing
            firstAbilityDescription = this.firstAbilityDescription,
            secAbilityName = this.secAbilityName,
            secAbilityType = this.secAbilityType,
            secAbilityPoints = this.secAbilityPoints,
            secAbilityCosts = this.secAbilityCosts.toList(), // Create a new list to avoid reference sharing
            secAbilityDescription = this.secAbilityDescription,
            selected = this.selected,
            currentHp = this.currentHp,
            protected = this.protected,
            used = this.used
        )
    }
}


