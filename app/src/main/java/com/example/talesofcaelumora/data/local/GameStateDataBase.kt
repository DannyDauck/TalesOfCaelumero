package com.example.talesofcaelumora.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.talesofcaelumora.data.datamodel.GameState
import com.example.talesofcaelumora.data.datamodel.PlayerLocal


@Database(entities = [GameState::class, PlayerLocal::class], version = 1)
@TypeConverters(Converters::class)
abstract class GameStateDatabase : RoomDatabase() {

    abstract val gameStateDao: GameStateDao

    companion object : SingletonHolder<GameStateDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            GameStateDatabase::class.java,
            "game_state_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    })
}