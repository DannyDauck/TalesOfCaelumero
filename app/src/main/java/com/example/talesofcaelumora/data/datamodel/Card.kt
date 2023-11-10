package com.example.talesofcaelumora.data.datamodel

import com.example.talesofcaelumora.R

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
) {
    var selected = false
    var currentHp: Int = hp
}

/*
//<editor-fold desc="Example">
val heroDeck = listOf(
    Card(
        "Elara",
        "Hero",
        "air",
        R.drawable.elara,
        210,
        "Luftsschnitt",
        20,
        listOf("air", "colorless"),
        "Scharfe Winde wüten",
        "Orkan",
        120,
        listOf("air", "air", "air", "colorless"),
        "Der Wind gibt mir Kraft"
    ),
    Card(
        "Kiara",
        "Hero",
        "fire",
        R.drawable.kiara,
        190,
        "Feuerball",
        25,
        listOf("fire", "colorless"),
        "Die Flamme in mir brennt",
        "Inferno",
        200,
        listOf("fire", "fire", "fire", "fire"),
        "Spüre die Macht des Feuers"
    ),
    Card(
        "Sylvan",
        "Hero",
        "plant",
        R.drawable.sylvan,
        180,
        "Pflanzenwuchs",
        18,
        listOf("plant", "colorless"),
        "Die Natur erblüht",
        "Dschungelgebrüll",
        140,
        listOf("plant", "plant", "plant", "colorless"),
        "Hörst du den Ruf des Dschungels?"
    ),
    Card(
        "Aqua",
        "Hero",
        "water",
        R.drawable.aqua,
        220,
        "Wasserstrahl",
        22,
        listOf("water", "colorless"),
        "Das Wasser fließt",
        "Tsunami",
        180,
        listOf("water", "water", "water", "colorless"),
        "Die Flut kommt!"
    ),
    Card(
        "Flora",
        "Hero",
        "plant",
        R.drawable.flora,
        195,
        "Blütenregen",
        24,
        listOf("plant", "colorless"),
        "Die Blumen erblühen",
        "Naturgewalt",
        160,
        listOf("plant", "plant", "plant", "colorless"),
        "Die Macht der Natur!"
    ),
    Card(
        "Hydra",
        "Hero",
        "water",
        R.drawable.hydra,
        240,
        "Wasserwoge",
        26,
        listOf("water", "colorless"),
        "Die Wogen steigen",
        "Tiefseezerstörung",
        220,
        listOf("water", "water", "water", "water"),
        "Aus der Tiefe der Meere"
    ),
    Card(
        "Wald",
        "Land",
        "plant",
        R.drawable.land_forest_one,
        1,
        "Verzaubertes Dickicht",
        0,
        listOf(),
        "\"Das Verzauberte Dickicht ist ein märchenhafter Ort, an dem wundersame Blumen den Weg beleuchten und die Naturzauber Wünsche erfüllen.\"",
        "",
        0,
        listOf(),
        ""
    ),
    Card(
        "Wald",
        "Land",
        "plant",
        R.drawable.land_forest_two,
        1,
        "Silberwald",
        0,
        listOf(),
        "\"Die silbernen Blätter der Bäume glänzen im Morgentau und ein magischer Glanz erlauchted den Weg. Die Naturgeister des Waldes hüten sein Geheimniss\"",
        "",
        0,
        listOf(),
        ""
    ),
    Card(
        "Supporter",
        "Supporter",
        "supporter",
        R.drawable.chibi_cyberpunk_mouse2,
        1,
        "Energieschub",
        0,
        listOf(),
        "Stelle deine alle in diesem Zug verbrauchten Energieressourcen wieder her",
        "",
        0,
        listOf(),
        ""
    )


)
//</editor-fold>
*/
