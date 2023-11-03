package com.example.talesofcaelumora.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.SmallCardAdapter
import com.example.talesofcaelumora.data.heroDeck
import com.example.talesofcaelumora.data.musicVolume
import com.example.talesofcaelumora.databinding.FragmentBattleBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        var playerHereos = SmallCardAdapter(heroDeck.shuffled().subList(0,5))
        var opponentHereos = SmallCardAdapter(heroDeck.shuffled().subList(0,5))

        //Backgroundmusic
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.forest_battle)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(musicVolume, musicVolume)
        mediaPlayer?.start()

        bnd.opponentHereos.adapter = opponentHereos
        bnd.playerHereos.adapter = playerHereos


        val scaleXAnimator = ObjectAnimator.ofFloat(bnd.clBattleground, "scaleX", 0.25f)
        val scaleYAnimator = ObjectAnimator.ofFloat(bnd.clBattleground, "scaleY", 0.25f)
        val rotateAnimation = ObjectAnimator.ofFloat(bnd.clBattleground, "rotationX", 40f)
        val turnTable = ObjectAnimator.ofFloat(bnd.clBattleground, "rotation", 180f)

        var animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, rotateAnimation,turnTable)
        animatorSet.duration = 5000
        animatorSet.start()



        bnd.btnTurnTable.setOnClickListener{
            turnTable.start()
            when(bnd.txtPlayerInfo.text){
                "Deine Seite" -> bnd.txtPlayerInfo.text = "Seite des Gegners"
                else -> bnd.txtPlayerInfo.text = "Deine Seite"
            }
            lifecycleScope.launch {
                delay(5000)
                bnd.txtPlayerInfo.isVisible = true
                delay(3000)
                bnd.txtPlayerInfo.isVisible = false
            }

        }

    }
    fun turnBattleground(){

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