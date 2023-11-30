package com.example.talesofcaelumora.data.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.talesofcaelumora.data.local.Converters

@Entity(tableName = "player_table")
data class PlayerLocal(
    @PrimaryKey (autoGenerate = false)
    val uid: String,
    var name: String,
    var level: Int,
    var character: Int,
    var deck: List<String>,
    val titles: List<String>,
    var lastCard: String,
    var hp: Int,
    var exp: Int,
    var bag: List<String>,
    var bagMaxSize: Int,
    var maxLand: Int,
    var maxBank: Int,
    var maxDeckSize: Int,
    var homeArena: Battlefield
) {
    constructor(player: Player) : this(
        player.uid,
        player.name,
        player.level,
        player.character,
        player.deck,
        player.titles,
        player.lastCard,
        player.hp,
        player.exp,
        player.bag,
        player.bagMaxSize,
        player.maxLand,
        player.maxBank,
        player.maxDeckSize,
        player.homeArena
    )
}