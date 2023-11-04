package com.example.talesofcaelumora.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.TranslateAnimation
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.CardAdapter
import com.example.talesofcaelumora.data.heroDeck
import com.example.talesofcaelumora.data.musicVolume
import com.example.talesofcaelumora.data.songList
import com.example.talesofcaelumora.data.songTitle
import com.example.talesofcaelumora.databinding.FragmentBattleBinding


class BattleFragment : Fragment() {

    private lateinit var bnd: FragmentBattleBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO implementieren das beim zurück gehen gefragt wird ob er aufgeben möchte
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentBattleBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var playerHereos = CardAdapter(heroDeck.shuffled().subList(0, 5))
        var opponentHereos = CardAdapter(heroDeck.shuffled().subList(0, 5))

        //Backgroundmusic
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.forest_battle)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(musicVolume, musicVolume)
        mediaPlayer?.start()

        bnd.opponentHeroes.adapter = opponentHereos
        bnd.playerHeroes.adapter = playerHereos
        bnd.marqueeText.isSelected = true

        //startet die Anfangsanimation des Spielfeldes
        firstAnimation()


        bnd.btnTurnTable.setOnClickListener {

            //keine weitere Rotation mehr, geht zu sehr auf die Performance

            //wenn die Rotation größer als 0 ist wird sie wieder auf 0 gesetzt, ansonsten auf 180
            // (switched also einfach zwischen beiden hin und her)

            if (bnd.clBattleground.rotation > 0f) {
                bnd.clBattleground.rotation = 0f
            } else {
                bnd.clBattleground.rotation = 180f
            }
        }

        //Top-Navigation
        bnd.btnMusicOnOff.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                bnd.btnMusicOnOffImg.setImageResource(R.drawable.music_off)
            } else {
                mediaPlayer?.start()
                bnd.btnMusicOnOffImg.setImageResource(R.drawable.music_on)
            }
        }
        bnd.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        bnd.btnChangeSong.setOnClickListener {
            changeSong()
        }

        //Sidebar
        bnd.btnSidebarOnOff.setOnClickListener {
            slideOut()
        }
    }

    private fun changeSong() {
        var songList = songList
        var songTitle = songTitle
        val random = (songList.indices).random()

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        mediaPlayer = MediaPlayer.create(requireContext(), songList[random])
        mediaPlayer?.setVolume(musicVolume, musicVolume)
        bnd.marqueeText.text = songTitle[random]
        mediaPlayer?.start()

    }
    fun slideOut(){
        //Die translationX entspricht nicht der in android studio angegebenen,
        // und variiert je nach Handymodell, deswegen so:
        val movement = if(bnd.sidebar.translationX == (resources.displayMetrics.density*80f)) -(resources.displayMetrics.density*80f) else resources.displayMetrics.density * 80f

        val translation =
            TranslateAnimation(0f,  movement, 0f, 0f)
        translation.duration = 1500

        translation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // eventuell noch ein slide sound implemntieren
            }

            override fun onAnimationEnd(animation: Animation?) {

                bnd.sidebar.clearAnimation()
                bnd.sidebar.translationX = bnd.sidebar.translationX + movement
                if(bnd.sidebar.translationX == (resources.displayMetrics.density*80f))bnd.btnSidebarOnOff.setImageResource(R.drawable.chevron_left)
                else bnd.btnSidebarOnOff.setImageResource(R.drawable.chevron_right)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        bnd.sidebar.startAnimation(translation)

    }
    fun firstAnimation(){
        val scaleXAnimator = ObjectAnimator.ofFloat(bnd.clBattleground, "scaleX", 0.25f)
        val scaleYAnimator = ObjectAnimator.ofFloat(bnd.clBattleground, "scaleY", 0.25f)
        val rotateAnimation = ObjectAnimator.ofFloat(bnd.clBattleground, "rotationX", 40f)
        val turnTable = ObjectAnimator.ofFloat(bnd.clBattleground, "rotation", 360f)

        var animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, rotateAnimation, turnTable)
        animatorSet.duration = 5000
        animatorSet.start()

        animatorSet = AnimatorSet()
        animatorSet.play(turnTable)
    }



    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
}
