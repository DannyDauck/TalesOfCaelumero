package com.example.talesofcaelumora;

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.POST_DELAY_REQUEST

class NotificationService : JobIntentService() {

    companion object {
        const val JOB_ID = 1000

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, NotificationService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.d("NotificationService", "onHandleIntent started")
        // Hier wird die Logik für deine Benachrichtigung implementiert
        // Zeige die Benachrichtigung an, z.B. mit NotificationManager
        showNotification()
    }

    private fun showNotification() {
        Log.d("NotificationService", "showNotification started")
        val notificationManager =
            NotificationManagerCompat.from(this)

        // Erstelle einen NotificationChannel für Android Oreo und höher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Erstelle die Benachrichtigung
        val notification = NotificationCompat.Builder(this, "default")
            .setContentTitle("Caelumero")
            .setContentText("Du warst lange nicht mehr in Caelumero")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        Log.d("NotificationService", notification.toString())

        // Zeige die Benachrichtigung an
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return

            return
        }else{Log.d("NofSer", "alles ok")}
        notificationManager.notify(1, notification)
    }
}

