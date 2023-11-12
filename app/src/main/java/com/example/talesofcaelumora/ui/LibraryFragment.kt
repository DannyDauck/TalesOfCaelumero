package com.example.talesofcaelumora.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.CardAdapter
import com.example.talesofcaelumora.data.datamodel.Card
import com.example.talesofcaelumora.data.musicVolume
import com.example.talesofcaelumora.databinding.FragmentLibraryBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LibraryFragment : Fragment() {

    private lateinit var bnd: FragmentLibraryBinding
    private lateinit var id: String
    private val vm : MainViewModel by activityViewModels()
    private var index: Int = 0
    private var currentList = listOf<Card>()
    private var mediaPlayer: MediaPlayer? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString("id")!!

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentLibraryBinding.inflate(inflater,container,false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //initialisieren der Library
        currentList = vm.cardLibrary.value ?: listOf()

        //MediaPlayer settings
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.watertheme)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(musicVolume, musicVolume)
        mediaPlayer?.start()

        //Adapter immer mit nur einer Card laden
        var adapter = CardAdapter(currentList, "",requireContext())
        vm.cardLibrary.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                delay(1000)
                currentList = vm.cardLibrary.value!!
                adapter = CardAdapter(currentList, "",requireContext())
                bnd.rv.adapter = adapter
                updateUI()
            }

        }
        bnd.btnBackFromLibrary.setOnClickListener {
            findNavController().popBackStack()
        }
        bnd.btnBackInLibrary.setOnClickListener {
            currentList = vm.cardLibrary.value!!
            if(index>0)index--
            adapter.update(listOf( currentList!!.get(index)))
            updateUI()
        }
        bnd.btnForward.setOnClickListener {
            currentList = vm.cardLibrary.value!!
            if(index<currentList.size-1)index++
            bnd.rv.adapter = adapter
            updateUI()
        }
    }
    private fun updateUI(){
        bnd.rv.adapter = CardAdapter(listOf( currentList[index]), "",requireContext())
        if(currentList.isNotEmpty()){
            when(currentList[index].cardType){
                "Land" -> {
                    bnd.firstAbilityHead.isGone = true
                    bnd.txtFirstAbilityType.isGone =true
                    bnd.llSecAbilityHead.isGone = true
                    bnd.txtSecAbilityType.isGone = true
                    bnd.txtName.text = currentList[index].cardName
                    bnd.txtType.text = currentList[index].cardType
                    bnd.txtRarity.text = currentList[index].rarity
                }
                "Supporter" -> {
                    bnd.firstAbilityHead.isGone = true
                    bnd.txtFirstAbilityType.isGone =true
                    bnd.llSecAbilityHead.isGone = true
                    bnd.txtSecAbilityType.isGone = true
                    bnd.txtName.text = currentList[index].cardName
                    bnd.txtType.text = currentList[index].cardType
                    bnd.txtRarity.text = currentList[index].rarity
                }
                "Hero" -> {
                    bnd.firstAbilityHead.isVisible = true
                    bnd.txtFirstAbilityType.isVisible =true
                    bnd.llSecAbilityHead.isVisible = true
                    bnd.txtSecAbilityType.isVisible = true
                    bnd.txtFirstAbilityType.text = currentList[index].firstAbilityType
                    bnd.txtSecAbilityType.text = currentList[index].secAbilityType
                    bnd.txtName.text = currentList[index].cardName
                    bnd.txtType.text = currentList[index].cardType
                    bnd.txtRarity.text = currentList[index].rarity
                }
            }
            bnd.marqueeTextLibrary.text = "This card was released  on ${currentList[index].releaseDate} "
            bnd.marqueeTextLibrary.isSelected = true
        }


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