package com.example.talesofcaelumora.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.CardAdapter
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.databinding.FragmentGettingCardsBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime


class GettingCardsFragment : Fragment() {

    private lateinit var bnd: FragmentGettingCardsBinding
    private var night = false
    private lateinit var soundManager: SoundManager
    private val vm: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        soundManager = SoundManager.getInstance(requireContext())
        night = (LocalTime.now().hour < 8 || LocalTime.now().hour > 19)
        bnd = FragmentGettingCardsBinding.inflate(inflater,container,false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bnd.imgBgNigth.isVisible = night
        bnd.imgBgNigthForeground.isVisible = night
        bnd.txtNewCardsReceived.text = getString(R.string.get_cards, "0")
        bnd.txtNewCardsReceivedShadow.text = getString(R.string.get_cards, "0")
        if (night) startTurning(bnd.imgBgNigth) else startTurning(bnd.imgBg)

        var adapter = CardAdapter(vm.newCards, "onesel", requireContext())
        bnd.rvNewCards.adapter = adapter
        adapter.startAnimation(vm.newCards, resources.displayMetrics.density)
        soundManager.playSound(R.raw.message_in)
        var counter = 0
        val job = lifecycleScope.launch(Dispatchers.IO){
            while(counter<vm.newCards.size){
                //um unabhängig von der Anzahl der Karten das Hochzählen auf 2 Sekunden zu beschränkeen
                delay(2000L/vm.newCards.size)
                counter++
                withContext(Dispatchers.Main){
                    bnd.txtNewCardsReceived.text = getString(R.string.get_cards, counter.toString())
                    bnd.txtNewCardsReceivedShadow.text = getString(R.string.get_cards, counter.toString())
                    soundManager.playSound(R.raw.card_flip)
                }

            }
        }
        bnd.btnOkGettingCards.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            if (vm.player.value!!.bag.size<=vm.player.value!!.bagMaxSize){
                vm.clearNewCards()
                findNavController().navigate(GettingCardsFragmentDirections.actionGettingCardsFragmentToHomeFragment())
            }else{
                vm.clearNewCards()
                findNavController().navigate(GettingCardsFragmentDirections.actionGettingCardsFragmentToSellCardsFragment())
            }
        }
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