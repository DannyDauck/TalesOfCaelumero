package com.example.talesofcaelumora.ui

import android.animation.Animator
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
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.CardAdapter
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import com.example.talesofcaelumora.data.datamodel.battlefields
import com.example.talesofcaelumora.data.musicVolume
import com.example.talesofcaelumora.data.songList
import com.example.talesofcaelumora.data.songTitle
import com.example.talesofcaelumora.databinding.FragmentBattleBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class BattleFragment : Fragment() {

    private lateinit var bnd: FragmentBattleBinding
    private var mediaPlayer: MediaPlayer? = null

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

       //viewModel.cardLibrary.observe(viewLifecycleOwner ) {
           if(viewModel.cardLibrary.value != null && viewModel.cardLibrary.value!!.isNotEmpty()){
               var playerHereos = CardAdapter(viewModel.cardLibrary.value?.shuffled()!!.subList(0, 5), "",requireContext())
               var opponentHereos = CardAdapter(viewModel.cardLibrary.value?.shuffled()!!.subList(0, 5), "", requireContext())
               var gridAdapter = CardAdapter(viewModel.cardLibrary.value?.shuffled()!!.plus(viewModel.cardLibrary.value!!.shuffled()),"selection", requireContext())

               bnd.opponentHeroes.adapter = opponentHereos
               bnd.playerHeroes.adapter = playerHereos
               bnd.rvGrid.adapter = gridAdapter
           }
      // }
        //viewModel.getCardLibrary()

        val battlefield = battlefields.random()
        battlefield.setBattlefield(bnd)

        //Backgroundmusic
        mediaPlayer = MediaPlayer.create(requireContext(), battlefield.music)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(musicVolume, musicVolume)
        mediaPlayer?.start()



        //startet die Anfangsanimation des Spielfeldes
        firstAnimation()




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
            if(!bnd.gridSelection.isVisible)slideOut()
            bnd.svLegend.isVisible = false
        }
        bnd.btnOkGrid.setOnClickListener {
            bnd.gridSelection.isVisible = false
        }
        bnd.btnTurnTable.setOnClickListener {

            //keine weitere Rotation mehr, geht zu sehr auf die Performance!!!

            //wenn die Rotation größer als 0 ist wird sie wieder auf 0 gesetzt, ansonsten auf 180
            // (switched also einfach zwischen beiden hin und her)

            //stellt sicher das die Rotation auf 180 oder 0 steht, z.B. nach der firstAnimation steht sie auf 720
            bnd.clBattleground.rotation = bnd.clBattleground.rotation%360-(bnd.clBattleground.rotation%180)

            if (bnd.clBattleground.rotation > 0f) {
                bnd.clBattleground.rotation = 0f
            } else {
                bnd.clBattleground.rotation = 180f
            }
        }
        bnd.landBar.setOnClickListener {
            bnd.gridSelection.isVisible = true
        }
        bnd.btnGraveyard.setOnClickListener {
            bnd.gridSelection.isVisible = true
            slideOut()
        }
        lifecycleScope.launch {
            delay(6000)
            bnd.marqueeText.text = bnd.marqueeText.text.toString()
            bnd.marqueeText.isSelected = true
        }
        bnd.btnLegend.setOnClickListener {
            bnd.svLegend.isVisible = !bnd.svLegend.isVisible
            bnd.txtGridRecyclerHeader.text = getString(R.string.legend)
            bnd.txtGridRecyclerHeaderShadow.text = getString(R.string.legend)
            bnd.gridSelection.isVisible = bnd.svLegend.isVisible
            if(bnd.svLegend.isVisible)bnd.btnLegend.setBackgroundResource(R.color.bottom_bar_selected_ico) else bnd.btnLegend.setBackgroundResource(R.color.nothing)
        }

    }

    private fun changeSong() {
        var songList = songList
        var songTitle = songTitle
        val random = (songList.indices).random()
        viewModel.getDateTime()

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        mediaPlayer = MediaPlayer.create(requireContext(), songList[random])
        mediaPlayer?.setVolume(musicVolume, musicVolume)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        lifecycleScope.launch {
            delay(5000)
            viewModel.getDateTime()
            var dateTimeString = viewModel.dateTime.value?.datetime?.substring(0,16)
            val dateTime = LocalDateTime.parse(dateTimeString)
            //bnd.marqueeText.text = bnd.marqueeText.text.toString() + " " + dateTimeString
            bnd.marqueeText.text = songTitle[random] + "   Es ist jetzt " + dateTime.hour + " Uhr " + dateTime.minute + " am " + dateTime.dayOfMonth + ". " + dateTime.month + "  im Jahr "+ dateTime.year.plus(-1647)+"des Windes in Caelumero"

        }

    }
    fun slideOut(){
        //Die translationX entspricht nicht der in android studio angegebenen,
        // und variiert je nach Handymodell und Bildschirmauflösung,  deswegen so:
        val movement = if (bnd.sidebar.translationX == resources.displayMetrics.density * 80f) {
            -(resources.displayMetrics.density * 80f)
        } else {
            resources.displayMetrics.density * 80f
        }

        val translation = TranslateAnimation(0f, movement, 0f, 0f)
        translation.duration = 1500

        translation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // eventuell noch ein slide sound implemntieren
            }

            override fun onAnimationEnd(animation: Animation?) {
                bnd.sidebar.clearAnimation()
                bnd.sidebar.translationX += movement
                if (bnd.sidebar.translationX == (resources.displayMetrics.density * 80f)) {
                    bnd.btnSidebarOnOff.setImageResource(R.drawable.chevron_left)
                } else {
                    bnd.btnSidebarOnOff.setImageResource(R.drawable.chevron_right)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })

        bnd.sidebar.startAnimation(translation)



    }
    private fun firstAnimation(){
        val scaleXAnimator = ObjectAnimator.ofFloat(bnd.clBattleground, "scaleX", 0.25f)
        val scaleYAnimator = ObjectAnimator.ofFloat(bnd.clBattleground, "scaleY", 0.2f)
        val rotateAnimation = ObjectAnimator.ofFloat(bnd.clBattleground, "rotationX", 40f)
        val turnTable = ObjectAnimator.ofFloat(bnd.clBattleground, "rotation", 360f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, rotateAnimation, turnTable)
        animatorSet.duration = 5000

        animatorSet.addListener(object : Animator.AnimatorListener {
            //man muss alle Methoden implementieren auch wenn sie nicht verwqendet werden.
            //Die EndMethode cleared die Animation auf dem Battleground und löst den MarqueeTextb aus
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {

                bnd.clBattleground.clearAnimation()
                bnd.marqueeText.isSelected = true
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        animatorSet.start()
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
