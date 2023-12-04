package com.example.talesofcaelumora.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.talesofcaelumora.data.datamodel.PlayerLocal
import com.example.talesofcaelumora.data.datamodel.GameState



@Dao
interface GameStateDao{

    @Upsert
    suspend fun saveGameState(gameState: GameState)

    @Upsert
    suspend fun savePlayer(player: PlayerLocal)


    @Query("SELECT * from game_state_table WHERE id= :uid")
    suspend fun getGameState(uid: String): GameState?

    @Query("SELECT * from player_table WHERE uid= :uid")
    suspend fun getPlayer(uid: String): PlayerLocal?

    @Query("DELETE from game_state_table WHERE id= :uid")
    suspend fun deleteGameState(uid: String)

    @Query("DELETE from player_table WHERE uid= :uid")
    suspend fun deletePlayer(uid: String)

    @Query("DELETE from game_state_table")
    suspend fun clearAllGameStats()

    @Query("DELETE from player_table")
    suspend fun clearAllPlayers()
}
