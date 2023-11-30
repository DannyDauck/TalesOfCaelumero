package com.example.talesofcaelumora.data.local

import androidx.room.TypeConverter
import com.example.talesofcaelumora.data.datamodel.PlayerLocal
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromPlayerLocal(playerLocal: PlayerLocal): String {
        return Gson().toJson(playerLocal)
    }

    @TypeConverter
    fun toPlayerLocal(playerLocalString: String): PlayerLocal {
        val type = object : com.google.gson.reflect.TypeToken<PlayerLocal>() {}.type
        return Gson().fromJson(playerLocalString, type)
    }
}