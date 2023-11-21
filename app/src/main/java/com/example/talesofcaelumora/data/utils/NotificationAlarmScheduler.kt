package com.example.talesofcaelumora.data.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ContentInfoCompat.Flags
import com.example.talesofcaelumora.data.datamodel.AlarmItem
import java.time.ZoneId

class NotificationAlarmScheduler(private val context: Context) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    override fun schedule(item: AlarmItem) {
        Log.d("NotificationAlarmScheduler", "schedule() startet")
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("MESSAGE", item.message)
            action = "com.example.talesofcaelumora.ALARM_ACTION"
        }
        alarmManager!!.set(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.d("NotificationAlarmScheduler", alarmManager.toString() + "\n" + intent.toString())


    }

    override fun cancel(item: AlarmItem) {
        alarmManager!!.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        )

    }

}