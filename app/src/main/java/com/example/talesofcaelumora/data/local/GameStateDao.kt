package com.example.talesofcaelumora.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.talesofcaelumora.data.datamodel.GameState
import com.example.talesofcaelumora.data.datamodel.PlayerLocal


@Dao
interface GameStateDao{

    @Upsert
    suspend fun saveGameState(gameState: GameState)

    @Update
    suspend fun update(gameState: GameState)


    @Query("SELECT * from game_state_table WHERE id= :uid")
    fun getGameState(uid: String): GameState?

    @Query("SELECT * from player_table WHERE uid= :uid")
    fun getPlayer(uid: String): PlayerLocal?




    @Query("DELETE from game_state_table WHERE id= :uid")
    suspend fun deleteGameState(uid: String)

    @Query("DELETE from player_table WHERE uid= :uid")
    suspend fun deletePlayer(uid: String)

    @Query("DELETE from game_state_table")
    suspend fun clearAllGameStats()

    @Query("DELETE from player_table")
    suspend fun clearAllPlayers()
}
