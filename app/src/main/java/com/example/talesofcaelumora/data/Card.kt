package com.example.talesofcaelumora.data

class Card(
    val cardName: String,
    val cardType: String,

    val type: String,
    val imgSrc: String,
    var hp: Int,

    val firstAbilityName: String,
    val firstAbilityPoints: Int,
    val firstAbilityCosts: List<String>,
    val firstAbilityDescription: String,

    val secAbilityName: String,
    val secAbilityPoints: Int,
    val secAbilityCosts: List<String>,
    val secAbilityDescription: String,
) {
}


//<editor-fold desc="Example">
val heroDeck = listOf(
    Card(
        "Elara",
        "Hero",
        "Air",
        "R.drawable.elara", // Ressourcen-ID als Zeichenkette
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
        "Fire",
        "R.drawable.kiara", // Ressourcen-ID als Zeichenkette
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
        "Plant",
        "R.drawable.sylvan", // Ressourcen-ID als Zeichenkette
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
        "Water",
        "R.drawable.aqua", // Ressourcen-ID als Zeichenkette
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
        "Plant",
        "R.drawable.flora", // Ressourcen-ID als Zeichenkette
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
        "Water",
        "R.drawable.hydra", // Ressourcen-ID als Zeichenkette
        240,
        "Wasserwoge",
        26,
        listOf("water", "colorless"),
        "Die Wogen steigen",
        "Tiefseezerstörung",
        220,
        listOf("water", "water", "water", "water"),
        "Aus der Tiefe der Meere"
    )
)
//</editor-fold>