package com.example.talesofcaelumora.data.datamodel

import android.util.Log
import com.example.talesofcaelumora.R


//<editor-fold desc="Initial decks">
//muss hier oben stehen  da sonst das Deck noch nicht als default gesetzt werden, da es noch nicht initialisiert wurde
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
    "hNV0ovNakVZb4RuyANk2",
    //heroes
    "air_hero_anddi_la2E16HTT2vd0TciHKx2",
    "air_hero_anddi_la2E16HTT2vd0TciHKx2",
    "air_hero_cynthia_9puJmZLnZwZFkhBJ4ZMP",
    "air_hero_cynthia_9puJmZLnZwZFkhBJ4ZMP",
    "air_hero_cynthia_9puJmZLnZwZFkhBJ4ZMP",

    //supporter cards
    "supporter_sandra_XML3SJrrV03qUi9TQnSD",
    "supporter_sandra_XML3SJrrV03qUi9TQnSD",
    "supporter_temple_cp_LjYwR5M5oCp3oV6Hfpnq",
    "supporter_temple_cp_LjYwR5M5oCp3oV6Hfpnq"
)
val initialDeckFire = listOf(
    //lands
    "land_fire_desert_81r18gBVcOejlmIkVg3l",
    "land_fire_desert_81r18gBVcOejlmIkVg3l",
    "land_fire_desert_81r18gBVcOejlmIkVg3l",
    "land_fire_desert_81r18gBVcOejlmIkVg3l",
    "land_fire_desert_w4yVJjEWCtuMhNu6JVTs",
    "land_fire_desert_w4yVJjEWCtuMhNu6JVTs",
    "land_fire_desert_w4yVJjEWCtuMhNu6JVTs",
    "land_fire_desert_w4yVJjEWCtuMhNu6JVTs",
    "land_fire_desert_w4yVJjEWCtuMhNu6JVTs",
    "land_fire_desert_w4yVJjEWCtuMhNu6JVTs",
    "land_fire_desert_w4yVJjEWCtuMhNu6JVTs",

    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_valley_tUC4Pdq3eHN2kFOGfAec",

    "CdoYqWZZ9d93iIUkwU9h",
    "CdoYqWZZ9d93iIUkwU9h",
    "nM4a5LlMqjzAGhPPeHpO",
    "nM4a5LlMqjzAGhPPeHpO",
    "hNV0ovNakVZb4RuyANk2",
    "hNV0ovNakVZb4RuyANk2",
    //heroes
    "fire_hero_melina_hqzyueKa6MqYZJfGoZt9",
    "fire_hero_melina_hqzyueKa6MqYZJfGoZt9",
    "fire_hero_ray_JvR2UTtuwM2xTgBIiq8o",
    "fire_hero_ray_JvR2UTtuwM2xTgBIiq8o",
    "fire_hero_ray_JvR2UTtuwM2xTgBIiq8o",
    //supporter cards
    "supporter_sandra_XML3SJrrV03qUi9TQnSD",
    "supporter_sandra_XML3SJrrV03qUi9TQnSD",
    "supporter_temple_cp_LjYwR5M5oCp3oV6Hfpnq",
    "supporter_temple_cp_LjYwR5M5oCp3oV6Hfpnq"
)
val initialDeckWater = listOf(
    //lands
    "oL681YzLakMlKEDw4BKY",
    "oL681YzLakMlKEDw4BKY",
    "oL681YzLakMlKEDw4BKY",
    "oL681YzLakMlKEDw4BKY",
    "oL681YzLakMlKEDw4BKY",
    "oL681YzLakMlKEDw4BKY",
    "oL681YzLakMlKEDw4BKY",
    "nM4a5LlMqjzAGhPPeHpO",
    "nM4a5LlMqjzAGhPPeHpO",
    "nM4a5LlMqjzAGhPPeHpO",
    "nM4a5LlMqjzAGhPPeHpO",

    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_valley_tUC4Pdq3eHN2kFOGfAec",

    "CdoYqWZZ9d93iIUkwU9h",
    "CdoYqWZZ9d93iIUkwU9h",
    "land_fire_desert_81r18gBVcOejlmIkVg3l",
    "land_fire_desert_w4yVJjEWCtuMhNu6JVTs",
    "hNV0ovNakVZb4RuyANk2",
    "hNV0ovNakVZb4RuyANk2",
    //heroes
    "water_hero_aria_Cqvc1RyQgpCVR2YOdXUo",
    "water_hero_aria_Cqvc1RyQgpCVR2YOdXUo",
    "water_hero_lireal_UMt3ueipy2wztAY8NRDY",
    "water_hero_lireal_UMt3ueipy2wztAY8NRDY",
    "water_hero_lireal_UMt3ueipy2wztAY8NRDY",
    //supporter cards
    "supporter_sandra_XML3SJrrV03qUi9TQnSD",
    "supporter_sandra_XML3SJrrV03qUi9TQnSD",
    "supporter_temple_cp_LjYwR5M5oCp3oV6Hfpnq",
    "supporter_temple_cp_LjYwR5M5oCp3oV6Hfpnq"
)
val initialDeckPlant = listOf(
    //lands
    "CdoYqWZZ9d93iIUkwU9h",
    "CdoYqWZZ9d93iIUkwU9h",
    "CdoYqWZZ9d93iIUkwU9h",
    "CdoYqWZZ9d93iIUkwU9h",
    "CdoYqWZZ9d93iIUkwU9h",
    "CdoYqWZZ9d93iIUkwU9h",
    "CdoYqWZZ9d93iIUkwU9h",
    "hNV0ovNakVZb4RuyANk2",
    "hNV0ovNakVZb4RuyANk2",
    "hNV0ovNakVZb4RuyANk2",
    "hNV0ovNakVZb4RuyANk2",

    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_city_WjW0scCoqxkirUNr8BVb",
    "land_air_valley_tUC4Pdq3eHN2kFOGfAec",

    "nM4a5LlMqjzAGhPPeHpO",
    "nM4a5LlMqjzAGhPPeHpO",
    "land_fire_desert_81r18gBVcOejlmIkVg3l",
    "land_fire_desert_w4yVJjEWCtuMhNu6JVTs",
    "oL681YzLakMlKEDw4BKY",
    "oL681YzLakMlKEDw4BKY",
    //heroes
    "nature_hero_Freija_veKY8HGUfByUyQZxnvLu",
    "nature_hero_Freija_veKY8HGUfByUyQZxnvLu",
    "nature_hero_joel_qg2zT8tAszewcRs65kDb",
    "nature_hero_joel_qg2zT8tAszewcRs65kDb",
    "nature_hero_joel_qg2zT8tAszewcRs65kDb",
    //supporter cards
    "supporter_sandra_XML3SJrrV03qUi9TQnSD",
    "supporter_sandra_XML3SJrrV03qUi9TQnSD",
    "supporter_temple_cp_LjYwR5M5oCp3oV6Hfpnq",
    "supporter_temple_cp_LjYwR5M5oCp3oV6Hfpnq"
)
//</editor-fold>



class Player(
    val uid: String,
    var name: String,
    var level: Int,
    var character: Int = R.drawable.elara_solo,
    var deck: List<String> = initialDeckAir,
    val titles: List<String> = listOf("Rooky"),
    var currentTitle: String = "",
    var lastCard: String = "",
    var hp: Int = 150,
    var exp: Int = 0,
    //Variable für alle Karten die der Spieler gesammelt hat
    var bag: List<String> = listOf<String>(),
    //Variable die den Höchstwert des Bags angibt
    var bagMaxSize: Int = 50,
    var maxLand: Int = 5,
    var maxBank: Int = 3,


    //initialisiert ein leeres Deck, wird im onBoarding befüllt

    var maxDeckSize: Int = 30,

    //setzt die Home Arena per default erstmal auf "Wassertemple"
    var homeArena: Battlefield = Battlefield(
        "$name' s Arena",
        "oL681YzLakMlKEDw4BKY",
        R.drawable.battleground_two,
        R.drawable.navbar_bg_water,
        R.raw.watertheme,
        "The Spirit Of Water - DarT music "
    ),
    var balance: Int = 0,
    var bagIncrease: Int = 0,
    var battles: MutableList<String> = mutableListOf()
    ) {

    constructor(map: Map<String, Any>) : this(
        uid = map["uid"] as String,
        name = map["name"] as String,
        level = (map["level"] as Long).toInt(),
        character = (map["character"] as Long).toInt(),
        deck = (map["deck"] as List<*>).map { it as String },
        titles = (map["titles"] as List<*>).map { it as String },
        currentTitle = map["currentTitle"] as String,
        lastCard = map["lastCard"] as String,
        hp = (map["hp"] as Long).toInt(),
        exp = (map["exp"] as Long).toInt(),
        bag = (map["bag"] as List<*>).map { it as String },
        bagMaxSize = (map["bagMaxSize"] as Long).toInt(),
        maxLand = (map["maxLand"] as Long).toInt(),
        maxBank = (map["maxBank"] as Long).toInt(),
        maxDeckSize = (map["maxDeckSize"] as Long).toInt(),
        homeArena = Battlefield(map["homeArena"] as Map<String, Any>),
        balance = (map["balance"] as Long).toInt(),
        bagIncrease = (map["bagIncrease"] as Long).toInt(),
        battles = (map["battles"] as List<*>).map { it as String }.toMutableList()
    )

    fun returnDeck(library: List<Card>): MutableList<Card> {
        Log.d("Player", "Try to fetch deck")
        var list: MutableList<Card> = mutableListOf()
        deck.forEach { entry ->
            Log.d("Player", entry)
            try{
                list.add(library.filter{ card -> card.id == entry }.first().toCard())
                Log.d("Player", "succesful added")
            }catch (e: Exception){
                Log.d("Player", entry + "wurde nicht gefunden")
            }

        }
        return list
    }

}



fun convertPlayerLocalToPlayer(playerEntity: PlayerLocal): Player {
    return Player(
        uid = playerEntity.uid,
        name = playerEntity.name,
        level = playerEntity.level,
        character = playerEntity.character,
        deck = playerEntity.deck,
        titles = playerEntity.titles,
        currentTitle = playerEntity.currentTitle,
        lastCard = playerEntity.lastCard,
        hp = playerEntity.hp,
        exp = playerEntity.exp,
        bag = playerEntity.bag,
        bagMaxSize = playerEntity.bagMaxSize,
        maxLand = playerEntity.maxLand,
        maxBank = playerEntity.maxBank,
        maxDeckSize = playerEntity.maxDeckSize,
        homeArena = playerEntity.homeArena,
        balance = playerEntity.balance,
        bagIncrease = playerEntity.bagIncrease,
        battles = playerEntity.battles
    )
}


var examplePlayerDanny = Player(
    "multibattle",
    "Danny",
    1,
    R.drawable.male_character_water
)

var examplePlayerElara = Player(
    "multibattle",
    "Elara",
    1,
    R.drawable.female_character_cutted
)

