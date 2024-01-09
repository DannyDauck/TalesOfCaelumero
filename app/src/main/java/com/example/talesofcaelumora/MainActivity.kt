package com.example.talesofcaelumora


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import com.example.talesofcaelumora.data.datamodel.AlarmItem
import com.example.talesofcaelumora.data.local.GameStateDatabase
import com.example.talesofcaelumora.data.utils.InsetsWithKeyboardCallback
import com.example.talesofcaelumora.data.utils.NotificationAlarmScheduler
import com.example.talesofcaelumora.data.utils.SoundManager
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {


    val POST_NOTIFICATIONS = 2001
    private lateinit var scheduler: NotificationAlarmScheduler
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var gameStateDataBase: GameStateDatabase
    private lateinit var soundManager: SoundManager


    override fun onCreate(savedInstanceState: Bundle?) {
        gameStateDataBase = GameStateDatabase.getInstance(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scheduler = NotificationAlarmScheduler(this)

        val root = findViewById<ConstraintLayout>(R.id.content_id)

        val insetsWithKeyboardCallback = InsetsWithKeyboardCallback(window)
        ViewCompat.setOnApplyWindowInsetsListener(root, insetsWithKeyboardCallback)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                POST_NOTIFICATIONS
            )
        }

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

    override fun onStart() {
        soundManager = SoundManager.getInstance(this)
        super.onStart()
    }

    override fun onDestroy() {
        //sendNotification("Schau doch mal wieder vor vorbei", 1)
        soundManager.release()
        super.onDestroy()


    }


    override fun onPause() {
        soundManager.pause()
        sendNotification("Du warst schon lange nicht mehr in Caelumero", 1)
        super.onPause()
    }


    fun sendNotification(text: String? = null, afterMinutes: Long = 1) {
        Log.d("MainActivity", "sendNotification startet")
        val alarmItem = AlarmItem(
            LocalDateTime.now().plusMinutes(afterMinutes),
            text ?: "Du warst lange nicht mehr in Caelumero"
        )
        alarmItem.let(scheduler::schedule)
    }

}

