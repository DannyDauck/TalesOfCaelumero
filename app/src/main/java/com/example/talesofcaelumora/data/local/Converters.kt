package com.example.talesofcaelumora.data.local

import androidx.room.TypeConverter
import com.example.talesofcaelumora.data.datamodel.Battlefield
import com.example.talesofcaelumora.data.datamodel.PlayerLocal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromBattlefield(battlefield: Battlefield): String {
        return Gson().toJson(battlefield)
    }

    @TypeConverter
    fun toBattlefield(battlefieldString: String): Battlefield {
        val type = object : com.google.gson.reflect.TypeToken<Battlefield>() {}.type
        return Gson().fromJson(battlefieldString, type)
    }
}