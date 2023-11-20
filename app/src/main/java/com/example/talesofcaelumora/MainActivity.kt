package com.example.talesofcaelumora


import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import androidx.navigation.findNavController
import com.example.talesofcaelumora.data.POST_DELAY_REQUEST
import com.example.talesofcaelumora.ui.LoginFragmentDirections
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //fragt ab ob die Berechtigung für Nachrichten erteilt ist
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                POST_DELAY_REQUEST
            )
            return

        }else{Log.d("MA", "Permission for post notifications granted")}


        //Die Methode ist zwar veraltet, funktioniert aber trotzdem einwandfrei.
        //Alternative wäre der WindowsInsetsController, der läuft aber auch erst ab API 30
        //und da ich nur die Statusleiste und die Steuereungselemente ausblenden möchte, habe ich mich entschieden,
        //die depricated Methode zu benutzen

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

    }

    override fun onDestroy() {

        val intent = Intent(this, NotificationService::class.java)
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        NotificationService.enqueueWork(this, intent)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = SystemClock.elapsedRealtime() + 60 * 1000  // 60 Sekunden in Millisekunden
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
        startService(intent)


        super.onDestroy()
    }

    override fun onPause() {
        Log.d("MAonPause", "onPause getsartet")
        val intent = Intent(this, NotificationService::class.java)
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("MA", intent.toString())
        Log.d("MA", pendingIntent.toString())
        NotificationService.enqueueWork(this, intent)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = SystemClock.elapsedRealtime() + 60 * 1000  // 60 Sekunden in Millisekunden
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)

        startService(intent)

        super.onPause()
    }
}