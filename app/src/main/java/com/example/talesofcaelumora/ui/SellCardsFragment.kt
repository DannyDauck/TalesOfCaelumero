package com.example.talesofcaelumora.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.CardAdapter
import com.example.talesofcaelumora.data.datamodel.Card
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.databinding.FragmentSellCardsBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

class SellCardsFragment : Fragment() {

    private lateinit var bnd: FragmentSellCardsBinding
    private val vm: MainViewModel by activityViewModels()
    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        soundManager = SoundManager.getInstance(requireContext())
        bnd = FragmentSellCardsBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentAskingPrice = 0

        var night = (LocalTime.now().hour < 8 || LocalTime.now().hour > 19)
        bnd.imgBgNigth.isVisible = night
        bnd.imgBgNigthForeground.isVisible = night

        if (night){
            bnd.navbarSellFragment.setBackgroundResource(R.drawable.navbar_bg_water)
            startTurning(bnd.imgBgNigth)
        } else{
            bnd.navbarSellFragment.setBackgroundResource(R.drawable.navbar_bg_air)
            startTurning(bnd.imgBg)
        }

        bnd.txtSellWorth.text = currentAskingPrice.toString()
        bnd.txtBalance.text = vm.player.value!!.balance.toString()
        bnd.currentBagSize.text = vm.player.value!!.bag.size.toString()
        bnd.maxBagSize.text = "/${vm.player.value!!.bagMaxSize.toString()}"
        bnd.txtBalanceShadow.text = vm.player.value!!.balance.toString()
        bnd.currentBagSizeShadow.text = vm.player.value!!.bag.size.toString()
        bnd.maxBagSizeShadow.text = "/${vm.player.value!!.bagMaxSize.toString()}"

        vm.player.observe(viewLifecycleOwner){
            bnd.txtBalance.text = vm.player.value!!.balance.toString()
            bnd.currentBagSize.text = vm.player.value!!.bag.size.toString()
            bnd.txtBalanceShadow.text = vm.player.value!!.balance.toString()
            bnd.currentBagSizeShadow.text = vm.player.value!!.bag.size.toString()
            if(vm.player.value?.bag!!.size>vm.player.value!!.bagMaxSize){
                bnd.currentBagSize.setTextColor(Color.RED)
            }else bnd.currentBagSize.setTextColor(Color.WHITE)
        }
        var askingPrice = 0
        var playerDeck = vm.player.value!!.deck.toMutableList()
        var adapterSet = mutableListOf<Card>()
        var adapter = CardAdapter(adapterSet,"selection", requireContext())
        lifecycleScope.launch {
            vm.player.value!!.bag.forEach { idstring ->
                var card = vm.cardLibrary.value?.filter { it.id == idstring}!!.first().toCard()
                if(playerDeck.contains(idstring)){
                    card.protected = true
                    playerDeck.remove(idstring)
                }
                adapterSet.add(card)
            }
            adapterSet.sortBy { it.type }
            adapterSet.sortWith(compareBy({ it.cardType == "Hero" }, { it.cardType == "Supporter" }, { it.cardType == "Land" }, { it.cardType }))
            adapterSet.sortByDescending { it.protected }

            soundManager.playSound(R.raw.sort_cards)
            adapter.notifyDataSetChanged()
        }
        bnd.rvPlayerBag.adapter = adapter
        adapter.update.observe(viewLifecycleOwner){
            askingPrice = 0
            adapterSet.forEach {
                if (it.selected){
                    if(it.cardType=="Land")askingPrice += 5
                    if(it.cardType=="Supporter"){
                        if(it.rarity=="rare")askingPrice += 50
                        else if(it.rarity=="ultra rare")askingPrice += 80
                        else askingPrice += 15
                    }
                    if(it.cardType=="Hero"){
                        if(it.rarity=="rare")askingPrice += 80
                        else if(it.rarity=="ultra rare")askingPrice += 120
                        else askingPrice += 40
                    }
                }
            }
            bnd.currentBagSize.text = adapterSet.filter { !it.selected }.size.toString()
            bnd.currentBagSizeShadow.text = adapterSet.filter { !it.selected }.size.toString()
            if(adapterSet.size>vm.player.value!!.bagMaxSize){
                bnd.currentBagSize.setTextColor(Color.RED)
            }else bnd.currentBagSize.setTextColor(Color.WHITE)
            bnd.txtSellWorth.text = "+" + askingPrice.toString()
            adapter.notifyDataSetChanged()
        }
        bnd.btnSell.setOnClickListener {

            if(adapterSet.filter { it.protected && it.cardType=="Hero" && !it.selected}.isNotEmpty()) {
                soundManager.playSound(R.raw.button_click)
                adapterSet.forEach {
                    if (it.selected) {
                        vm.player.value!!.bag = vm.player.value!!.bag.minus(it.id)
                        if (it.protected) vm.player.value!!.deck =
                            vm.player.value!!.deck.minus(it.id)
                    }
                }
                adapterSet.removeIf { it.selected }
                bnd.txtSellWorth.text = "0"
                vm.player.value!!.balance += askingPrice
                askingPrice = 0
                bnd.txtBalance.text = vm.player.value!!.balance.toString()
                adapter.notifyDataSetChanged()
                bnd.currentBagSize.text = adapterSet.size.toString()
                bnd.currentBagSizeShadow.text = adapterSet.size.toString()
                if (adapterSet.size > vm.player.value!!.bagMaxSize) {
                    bnd.currentBagSize.setTextColor(Color.RED)
                } else bnd.currentBagSize.setTextColor(Color.WHITE)
            }else throwToast(getString(R.string.you_can_not_sell_your_decks_last_hero))
        }

        bnd.btnInfo.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            bnd.llInfo.isGone = !bnd.llInfo.isGone
        }

        bnd.btnHome.setOnClickListener {
            if(adapterSet.size<=vm.player.value!!.bagMaxSize){
                soundManager.playSound(R.raw.button_click)
                var player = vm.player.value!!
                var deck = mutableListOf<String>()
                var bag = mutableListOf<String>()
                adapterSet.forEach {
                    bag.add(it.id)
                    if(it.protected)deck.add(it.id)
                }
                player.bag = bag
                player.deck = deck
                vm.upsertPlayer(player)
                vm.upsertFirebasePLayer(player)
                findNavController().navigate(SellCardsFragmentDirections.actionSellCardsFragmentToHomeFragment())
            }else throwToast(getString(R.string.you_ve_got_to_sell_cards))
        }

    }
    fun throwToast(text: String){
        bnd.toast.text = text
        bnd.toast.isVisible = true
        soundManager.playSound(R.raw.message_in)
        lifecycleScope.launch {
            delay(2000)
            bnd.toast.isGone = true
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