package com.example.talesofcaelumora.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.characters
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.databinding.FragmentInitilaDeckChooseBinding
import com.google.android.material.shape.CornerFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitialDeckChooseFragment : Fragment() {


    private lateinit var bnd: FragmentInitilaDeckChooseBinding
    private lateinit var soundManager: SoundManager
    private var name  = ""
    private var character = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name")!!
            character = it.getInt("character")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        soundManager = SoundManager.getInstance(requireContext())
        bnd = FragmentInitilaDeckChooseBinding.inflate(inflater,container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //rundet die Ecken der inneren ImageView ab
        bnd.imgDeckAirBg.shapeAppearanceModel = bnd.imgDeckAirBg.shapeAppearanceModel.toBuilder()
            .setBottomRightCorner(CornerFamily.ROUNDED, 60f)
            .setBottomLeftCorner(CornerFamily.ROUNDED,30f)
            .setTopRightCorner(CornerFamily.ROUNDED, 30f)
            .setTopLeftCorner(CornerFamily.ROUNDED, 60f)
            .build()

        bnd.imgDeckWaterBg.shapeAppearanceModel = bnd.imgDeckWaterBg.shapeAppearanceModel.toBuilder()
            .setBottomRightCorner(CornerFamily.ROUNDED, 60f)
            .setBottomLeftCorner(CornerFamily.ROUNDED,30f)
            .setTopRightCorner(CornerFamily.ROUNDED, 30f)
            .setTopLeftCorner(CornerFamily.ROUNDED, 60f)
            .build()

        bnd.imgDeckFireBg.shapeAppearanceModel = bnd.imgDeckFireBg.shapeAppearanceModel.toBuilder()
            .setBottomRightCorner(CornerFamily.ROUNDED, 60f)
            .setBottomLeftCorner(CornerFamily.ROUNDED,30f)
            .setTopRightCorner(CornerFamily.ROUNDED, 30f)
            .setTopLeftCorner(CornerFamily.ROUNDED, 60f)
            .build()
        bnd.imgDeckNatureBg.shapeAppearanceModel = bnd.imgDeckNatureBg.shapeAppearanceModel.toBuilder()
            .setBottomRightCorner(CornerFamily.ROUNDED, 60f)
            .setBottomLeftCorner(CornerFamily.ROUNDED,30f)
            .setTopRightCorner(CornerFamily.ROUNDED, 30f)
            .setTopLeftCorner(CornerFamily.ROUNDED, 60f)
            .build()

        bnd.imgPlayerCharacter.setImageResource(character)



    }
    private fun slowPrintText(text: String){
        val handler = Handler(Looper.getMainLooper())
        var index = 1


        val runnable = object : Runnable {
            override fun run() {
                bnd.btnFastTxt.setOnClickListener {
                    index = text.length -4
                }
                if (index < text.length) {
                    bnd.txtElara.text = text.substring(0, index + 1)
                    index++
                    handler.postDelayed(this, 50)
                }else{
                    lifecycleScope.launch {

                        delay(2000)
                        bnd.btnFastTxt.setOnClickListener(null)
                        bnd.llDecks.isVisible = true
                        soundManager.playSound(R.raw.message_in)

                        bnd.airDeck.setOnClickListener {

                        }
                        bnd.fireDeck.setOnClickListener {

                        }
                        bnd.waterDeck.setOnClickListener {

                        }
                        bnd.plantDeck.setOnClickListener {

                        }

                    }

                }
            }
        }

        handler.postDelayed(runnable, 100)
    }



}