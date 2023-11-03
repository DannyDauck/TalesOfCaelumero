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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var bnd: FragmentHomeBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //verhindert das die App wieder in den Lade-/SplashScreen springt und fragt stattdessen ob der User die App beenden möchte

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.close_app)
                builder.setMessage(R.string.sure_to_leave)
                builder.setPositiveButton(R.string.yes) { dialog, which ->
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
        bnd = FragmentHomeBinding.inflate(inflater, container,false)


        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        startTurning(bnd.imgBg)


        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.main_theme)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()


        bnd.musicVolumeSeekBar.progress = 50    //TODO im viewModel ne float für die Lautstärken einrichten damit nicht jedesmal der Progress auf 50 gesetzt wird
        bnd.vfxVolumeSeekBar.progress = 50
        bnd.musicVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val volume = progress / 100f
                mediaPlayer?.setVolume(volume, volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //muss zugefügt werden auch wenn nichts macht
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nicht erforderlich
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    private fun startTurning(view: View){
        val translation = TranslateAnimation(0f, 200f, 0f, 0f)
        translation.duration = 10000

        lifecycleScope.launch{
            view.startAnimation(translation)
            delay(10000)
            view.translationX += 200f

            turnBack(view)
        }


    }
    private fun turnBack(view: View){
        val translation = TranslateAnimation(0f, -200f, 0f, 0f)
        translation.duration = 10000

        lifecycleScope.launch{

            view.startAnimation(translation)
            delay(10000)
            view.translationX -= 200f

            startTurning(view)
        }
    }

}