package com.example.talesofcaelumora.data.datamodel

import com.example.talesofcaelumora.R

class Player(
    val uid: String,
    var name: String,
    var level: Int,
) {

    var hp: Int = 150
    var exp: Int = 0
    //initialisiert ein leeres Deck, wird im onBoarding befüllt
    var deck: List<Int> = listOf()

    //setzt die Home Arena per default erstmal auf "Wassertemple"
    var homeArena: Battlefield = Battlefield("$name' s Arena", R.drawable.temple_of_waterbenders, R.drawable.battleground_two, R.drawable.navbar_bg_water, R.raw.watertheme, "The Spirit Of Water - DarT music ")

    //bestimmt den Look des User Screens
    var clan: String = "water"

    var character: Int = R.drawable.elara_solo





}