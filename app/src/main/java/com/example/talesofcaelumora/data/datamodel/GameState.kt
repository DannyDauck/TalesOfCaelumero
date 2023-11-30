package com.example.talesofcaelumora.data.datamodel


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_state_table")
data class GameState(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    var onBoarded: Boolean,
    var battleTutorialDone: Boolean = false,
    var profileSettingsTutorialDone: Boolean = false,
    var multiBattleTutorialDone: Boolean = false,
    var caelumero: Int = 1,
    var ignisaria: Int = 1,
    var marinaHaven: Int = 1,
    var verdantHaven: Int = 1,
)