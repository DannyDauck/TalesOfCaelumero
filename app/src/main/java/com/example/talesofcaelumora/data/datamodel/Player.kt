package com.example.talesofcaelumora.data.datamodel

import android.util.Log
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import java.lang.Exception

class Player(
    val uid: String,
    var name: String,
    var level: Int,

    ) {

    var hp: Int = 150
    var exp: Int = 0

    //Variable für alle Karten die der Spieler gesammelt hat
    var bag = listOf<String>()

    //Variable die den Höchstwert des Bags angibt
    var bagMaxSize = 50

    var maxLand = 5

    var maxBank = 3

    //initialisiert ein leeres Deck, wird im onBoarding befüllt
    var deck : List<String> ?= initialDeckAir
    var maxDeckSize = 30

    //setzt die Home Arena per default erstmal auf "Wassertemple"
    var homeArena: Battlefield = Battlefield(
        "$name' s Arena",
        R.drawable.temple_of_waterbenders,
        R.drawable.battleground_two,
        R.drawable.navbar_bg_water,
        R.raw.watertheme,
        "The Spirit Of Water - DarT music "
    )


    var character: Int = R.drawable.elara_solo

    fun returnDeck(library: List<Card>): MutableList<Card> {
        Log.d("Player", "Try to fetch deck")
        var list: MutableList<Card> = mutableListOf()
        initialDeckAir.forEach { entry ->
            Log.d("Player", entry)
            try{
                list.add(library.filter{ card -> card.id == entry }.first())
                Log.d("Player", "succesful added")
            }catch (e: Exception){
                Log.d("Player", entry + "wurde nicht gefunden")
            }

        }
        return list
    }

}

var examplePlayerDanny = Player(
    "ExamplePLayerDanny",
    "Danny",
    1
)

var examplePlayerElara = Player(
    "ExcamplePlayerElara",
    "Elara",
    1
)


val initialDeckAir = listOf(
    //lands
    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_valley_tUC4Pdq3eHN2kFOGfAec",
    "land_air_valley_tUC4Pdq3eHN2kFOGfAec",
    "land_air_valley_tUC4Pdq3eHN2kFOGfAec",
    "land_air_valley_tUC4Pdq3eHN2kFOGfAec",
    "land_air_valley_tUC4Pdq3eHN2kFOGfAec",
    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_fire_desert_81r18gBVcOejlmIkVg3l",
    "land_fire_desert_81r18gBVcOejlmIkVg3l",
    "land_fire_desert_81r18gBVcOejlmIkVg3l",
    "CdoYqWZZ9d93iIUkwU9h",
    "CdoYqWZZ9d93iIUkwU9h",
    "nM4a5LlMqjzAGhPPeHpO",
    "nM4a5LlMqjzAGhPPeHpO",
    "hNV0ovNakVZb4RuyANk2",
    //heroes
    "air_hero_anddi_la2E16HTT2vd0TciHKx2",
    "air_hero_anddi_la2E16HTT2vd0TciHKx2",
    "air_hero_cynthia_9puJmZLnZwZFkhBJ4ZMP",
    "air_hero_cynthia_9puJmZLnZwZFkhBJ4ZMP",
    "air_hero_cynthia_9puJmZLnZwZFkhBJ4ZMP",
    "hNV0ovNakVZb4RuyANk2",
    //supporter cards
    "supporter_sandra_XML3SJrrV03qUi9TQnSD",
    "supporter_sandra_XML3SJrrV03qUi9TQnSD",
    "supporter_temple_cp_LjYwR5M5oCp3oV6Hfpnq",
    "supporter_temple_cp_LjYwR5M5oCp3oV6Hfpnq"
)