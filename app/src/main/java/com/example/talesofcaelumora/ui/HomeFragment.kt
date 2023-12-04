package com.example.talesofcaelumora.ui

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.OFFSET_1900_TO_1970
import com.example.talesofcaelumora.data.musicVolume
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.data.vfxVolume
import com.example.talesofcaelumora.databinding.FragmentHomeBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import com.google.android.material.shape.CornerFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.commons.net.ntp.NTPUDPClient
import java.net.InetAddress
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


class HomeFragment : Fragment() {

    private lateinit var bnd: FragmentHomeBinding
    private lateinit var soundManager: SoundManager
    private var night = false
    private val vm: MainViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //verhindert das die App wieder in den Lade-/SplashScreen springt und fragt stattdessen ob der User die App beenden möchte
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.close_app)
                builder.setMessage(R.string.sure_to_leave)
                builder.setPositiveButton(R.string.yes) { _, which ->
                    requireActivity().finish()
                }
                builder.setNegativeButton(R.string.no) { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        night = (LocalTime.now().hour < 8 || LocalTime.now().hour > 19)
        bnd = FragmentHomeBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        bnd.imgFreeCard.shapeAppearanceModel = bnd.imgFreeCard.shapeAppearanceModel.toBuilder()
            .setBottomRightCorner(CornerFamily.ROUNDED, 80f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 30f)
            .setTopRightCorner(CornerFamily.ROUNDED, 30f)
            .setTopLeftCorner(CornerFamily.ROUNDED, 80f)
            .build()
        bnd.imgFreeCardLayer.shapeAppearanceModel = bnd.imgFreeCard.shapeAppearanceModel.toBuilder()
            .setBottomRightCorner(CornerFamily.ROUNDED, 80f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 30f)
            .setTopRightCorner(CornerFamily.ROUNDED, 30f)
            .setTopLeftCorner(CornerFamily.ROUNDED, 80f)
            .build()

        bnd.imgBgNigth.isVisible = night
        bnd.imgBgNigthForeground.isVisible = night
        vm.startCountdown()
        vm.countdownText.observe(viewLifecycleOwner){
            bnd.txtFreeCardCounter.text = it
            bnd.txtFreeCardCounterShadow.text = it
            if(it==getString(R.string.now_available)){
                bnd.imgFreeCardLayer.isVisible = false
                bnd.chipFreeCard.setOnClickListener{
                    vm.setNewLastCard()
                    vm.getFreeCards(20)
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToGettingCardsFragment())
                }
            }else{
                bnd.chipFreeCard.setOnClickListener(null)
                bnd.imgFreeCardLayer.isVisible = true
            }
        }

        if (night) startTurning(bnd.imgBgNigth) else startTurning(bnd.imgBg)
        val theme = R.raw.main_theme
        soundManager = SoundManager.getInstance(requireContext())
        soundManager.startRadio(R.raw.main_theme)

        bnd.musicVolumeSeekBar.progress = (musicVolume * 100).toInt()
        bnd.vfxVolumeSeekBar.progress = (vfxVolume * 100).toInt()

        bnd.musicVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val volume = progress / 100f
                musicVolume = progress / 100f
                musicVolume = volume
                soundManager.setRadioVolume()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        bnd.vfxVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                vfxVolume = bnd.vfxVolumeSeekBar.progress.toFloat() / 100
                soundManager.setVolume()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                soundManager.playSound(R.raw.sort_cards)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                soundManager.playSound(R.raw.sort_cards)
            }

        })


        bnd.btnSetiings.setOnClickListener {
            bnd.clSettings.isVisible = !bnd.clSettings.isVisible
        }
        bnd.btnGame.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBattleFragment())
        }
        bnd.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.loginFragment)
        }
        bnd.btnLibrary.setOnClickListener {
            findNavController().navigate(R.id.sellCardsFragment)
        }


    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }

    private fun startTurning(view: View) {
        val translation = TranslateAnimation(0f, 200f, 0f, 0f)
        translation.duration = 10000

        lifecycleScope.launch {
            view.startAnimation(translation)
            delay(10000)
            view.translationX += 200f

            turnBack(view)

        }


    }

    //zewite Funtkition um Rekusivschleife zu umgehen
    private fun turnBack(view: View) {
        val translation = TranslateAnimation(0f, -200f, 0f, 0f)
        translation.duration = 10000

        lifecycleScope.launch {

            view.startAnimation(translation)
            delay(10000)
            view.translationX -= 200f

            startTurning(view)
        }
    }



}

