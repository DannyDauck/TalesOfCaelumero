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
    var deck: List<String> = listOf()

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
}


val initialDeck = listOf(
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