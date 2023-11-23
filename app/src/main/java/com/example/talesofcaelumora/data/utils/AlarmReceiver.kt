package com.example.talesofcaelumora.data.utils

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.talesofcaelumora.MainActivity
import com.example.talesofcaelumora.R

class AlarmReceiver: BroadcastReceiver() {
    val CHANNEL_ID = "talesOfCaelumeroChannelID"
    val CHANNEL_NAME = "TalesOfCaelumeroChannel"
    val NOTIFICATION_ID = 0
    private lateinit var notification: Notification

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "OnReceive")
        val message = intent?.getStringExtra("MESSAGE") ?: return
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_MUTABLE)
        }
        //ab Android Oreo wir ein Notification Channel benötigt
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                manager.createNotificationChannel(channel)
            }
        }
        if (message != null) {


            notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
                .setContentTitle("Caelumero")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        }else{

            notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
                .setContentTitle("Caelumero")
                .setContentText("Du warst schon lange nicht mehr in Caelumero")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        }

        val notificationManager = NotificationManagerCompat.from(context)

        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }


        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}