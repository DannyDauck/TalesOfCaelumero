package com.example.talesofcaelumora

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.talesofcaelumora.ui.LoginFragmentDirections
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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




}