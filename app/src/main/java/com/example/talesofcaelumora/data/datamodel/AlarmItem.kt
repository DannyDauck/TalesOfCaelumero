package com.example.talesofcaelumora.data.datamodel

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String,
) {
}