package com.example.talesofcaelumora.data.utils

import com.example.talesofcaelumora.data.datamodel.AlarmItem

interface AlarmScheduler{
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}