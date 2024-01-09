package com.example.talesofcaelumora.data.datamodel

import java.time.LocalDateTime

data class ChatItem(
    val uid: String,
    val character: Int,
    val time: LocalDateTime,
    val name: String,
    val message: String,
    val language: String){

    constructor(message: String, player: Player, time: LocalDateTime, lang: String): this(uid = player.uid, character = player.character, time, player.name, message, lang )

    constructor(map: Map<String, Any>) : this(
        uid = map["uid"] as? String ?: "",
        character = (map["character"] as? Long)?.toInt() ?: 0,
        time = map["time"] as? LocalDateTime ?: LocalDateTime.now(),
        name = map["name"] as? String ?: "",
        message = map["message"] as? String ?: "",
        language = map["language"] as? String ?: ""
    )
}