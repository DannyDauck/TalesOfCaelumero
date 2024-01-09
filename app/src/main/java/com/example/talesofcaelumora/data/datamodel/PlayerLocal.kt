package com.example.talesofcaelumora.data.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.talesofcaelumora.data.local.Converters

@Entity(tableName = "player_table")
@TypeConverters(Converters::class)
data class PlayerLocal(
    @PrimaryKey (autoGenerate = false)
    val uid: String,
    var name: String,
    var level: Int,
    var character: Int,
    var deck: List<String>,
    val titles: List<String>,
    val currentTitle: String,
    var lastCard: String,
    var hp: Int,
    var exp: Int,
    var bag: List<String>,
    var bagMaxSize: Int,
    var maxLand: Int,
    var maxBank: Int,
    var maxDeckSize: Int,
    var homeArena: Battlefield,
    var balance: Int,
    var bagIncrease: Int,
    var battles: MutableList<String>
) {
    constructor(player: Player) : this(
        player.uid,
        player.name,
        player.level,
        player.character,
        player.deck,
        player.titles,
        player.currentTitle,
        player.lastCard,
        player.hp,
        player.exp,
        player.bag,
        player.bagMaxSize,
        player.maxLand,
        player.maxBank,
        player.maxDeckSize,
        player.homeArena,
        player.balance,
        player.bagIncrease,
        player.battles
    )
}