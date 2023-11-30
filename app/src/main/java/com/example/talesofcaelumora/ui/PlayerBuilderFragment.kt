package com.example.talesofcaelumora.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.CircularMiniCharacterAdapter
import com.example.talesofcaelumora.data.characters
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.databinding.FragmentPlayerBuilderBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerBuilderFragment : Fragment() {

    private lateinit var bnd: FragmentPlayerBuilderBinding
    private lateinit var soundManager: SoundManager
    private var name = ""
    private var targetPosition = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name")!!
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        soundManager = SoundManager.getInstance(requireContext())
        bnd = FragmentPlayerBuilderBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var adapter = CircularMiniCharacterAdapter(characters, R.drawable.male_character_water)
        //verdreifacht den Datensatz um eine endlos scrolling im zweiten Datensatz zu ermöglichen
        adapter.tripleData()
        bnd.rvMiniCharacters.adapter = adapter


        bnd.imgCharacterBig.setImageResource(characters[targetPosition])

        //sperrt die RecyclerView für Benutzerinteraktion, diese sollen nur über die Buttons laufen
        bnd.rvMiniCharacters.setOnTouchListener { _, _ -> true }
        bnd.rvMiniCharacters.smoothScrollToPosition((characters.size*2)-1)
        adapter.setIndicator(characters[targetPosition])


        bnd.btnPreviouse.setOnClickListener {
            //berechnen der targetPosition der Recyclerview und setzen der korrekten Position der RecyclerView im zweiten Datensatz
            targetPosition--
            if (targetPosition <= -1)
                targetPosition = characters.size - 1
            var space = 1
            if(targetPosition>= characters.size-2)space= 1
            if(targetPosition>= characters.size-1)space= -1
            if (targetPosition==0)bnd.rvMiniCharacters.scrollToPosition((characters.size*2)-1)
            bnd.rvMiniCharacters.smoothScrollToPosition(targetPosition + characters.size - space)
            bnd.imgCharacterBig.setImageResource(characters[targetPosition])
            lifecycleScope.launch {
                adapter.setIndicator(characters[targetPosition])
            }

        }
        bnd.btnNext.setOnClickListener {
            //berechnen der targetPosition der Recyclerview und setzen der korrekten Position der RecyclerView im zweiten Datensatz
            targetPosition++
            if (targetPosition >= characters.size)
                targetPosition = 0
            var space = -1
            if(targetPosition>=1)space= 1
            if (targetPosition==0)bnd.rvMiniCharacters.scrollToPosition(characters.size+1)
            bnd.rvMiniCharacters.smoothScrollToPosition(targetPosition + characters.size+space)
            bnd.imgCharacterBig.setImageResource(characters[targetPosition])
            lifecycleScope.launch {
                adapter.setIndicator(characters[targetPosition])
            }
        }
        bnd.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        slowPrintText(getString(R.string.on_boarding_two, name))
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
                        bnd.btnFastTxt.isGone = true
                        bnd.btnOk.isVisible = true
                        bnd.rvMiniCharacters.isVisible =true
                        bnd.imgCharacterBig.isVisible = true
                        bnd.btnNext.isVisible = true
                        bnd.btnPreviouse.isVisible = true
                        soundManager.playSound(R.raw.message_in)
                        bnd.btnOk.setOnClickListener{
                            findNavController().navigate(PlayerBuilderFragmentDirections.actionPlayerBuilderFragmentToInitilaDeckChooseFragment(name, characters[targetPosition]))
                        }

                    }

                }
            }
        }

        handler.postDelayed(runnable, 100)
    }

}

