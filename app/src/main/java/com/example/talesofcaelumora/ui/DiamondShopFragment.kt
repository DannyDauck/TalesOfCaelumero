package com.example.talesofcaelumora.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.databinding.FragmentDiamondShopBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DiamondShopFragment : Fragment() {


    private lateinit var bnd: FragmentDiamondShopBinding
    private lateinit var soundManager: SoundManager
    private val vm: MainViewModel by activityViewModels()
    private var balance = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        soundManager = SoundManager.getInstance(requireContext())
        bnd = FragmentDiamondShopBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.clearNewCards()

        startTurning(bnd.imgBg)
        balance = vm.player.value!!.balance

        bnd.txtPlayerBalance.text = balance.toString()
        bnd.txtPlayerBalanceShadow.text = balance.toString()


        bnd.btnHome.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            if (vm.newCards.isEmpty())
                findNavController().navigate(DiamondShopFragmentDirections.actionDiamondShopFragmentToHomeFragment())
            else {
                val newPlayer = vm.player.value!!
                newPlayer.balance = balance
                vm.upsertPlayer(newPlayer)
                vm.upsertFirebasePLayer(newPlayer)
                findNavController().navigate(DiamondShopFragmentDirections.actionDiamondShopFragmentToGettingCardsFragment())
            }
        }
        bnd.btnSell.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            if(balance!= vm.player.value!!.balance){
                vm.player.value!!.balance = balance
                vm.upsertPlayer(vm.player.value!!)
                vm.upsertFirebasePLayer(vm.player.value!!)
                vm.clearNewCards()
            }
            findNavController().navigate(DiamondShopFragmentDirections.actionDiamondShopFragmentToSellCardsFragment())
        }

        bnd.btnInfo.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            bnd.llInfo.isGone = !bnd.llInfo.isGone
        }
        bnd.btnCancel.setOnClickListener {
            bnd.confirmToast.isGone = true
        }

        //shopitems
        listOf(
            bnd.btnAirLand,
            bnd.btnSingleFireLand,
            bnd.btnSinglePlantLand,
            bnd.btnSingleWaterLand
        ).forEach { button ->
            button.setOnClickListener {
                addLand(
                    when (button) {
                        bnd.btnAirLand -> "air"
                        bnd.btnSingleWaterLand -> "water"
                        bnd.btnSingleFireLand -> "fire"
                        else -> "plant"
                    }
                )
            }
        }
        bnd.btnRandomHeroCard.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            if (balance >= 150) {
                bnd.txtConfirmInfo.text = getString(R.string.purchase_item_confirmation, "150")
                bnd.confirmToast.isGone = false
                bnd.btnConfirm.isGone = false
                bnd.btnConfirm.setOnClickListener {
                    soundManager.playSound(R.raw.hit)
                    vm.addRandomHero(150, null)
                    balance -= 150
                    bnd.txtPlayerBalance.text = balance.toString()
                    findNavController().navigate(DiamondShopFragmentDirections.actionDiamondShopFragmentToGettingCardsFragment())
                }
            }else {
                bnd.txtConfirmInfo.text = getString(R.string.not_enough_diamonds)
                bnd.btnConfirm.isGone = true
                bnd.confirmToast.isGone =false
            }
        }
        bnd.btnRndSupporterCard.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            if (balance >= 60) {
                bnd.txtConfirmInfo.text = getString(R.string.purchase_item_confirmation, "150")
                bnd.confirmToast.isGone = false
                bnd.btnConfirm.isGone = false
                bnd.btnConfirm.setOnClickListener {
                    soundManager.playSound(R.raw.hit)
                    vm.addRandomSupporter(60)
                    balance -= 60
                    bnd.txtPlayerBalance.text = balance.toString()
                    findNavController().navigate(DiamondShopFragmentDirections.actionDiamondShopFragmentToGettingCardsFragment())
                }
            }else {
                bnd.txtConfirmInfo.text = getString(R.string.not_enough_diamonds)
                bnd.btnConfirm.isGone = true
                bnd.confirmToast.isGone =false
            }
        }
        bnd.btnRndRareOrUltraRare.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            if (balance >= 350) {
                bnd.txtConfirmInfo.text = getString(R.string.purchase_item_confirmation, "350")
                bnd.confirmToast.isGone = false
                bnd.btnConfirm.isGone = false
                bnd.btnConfirm.setOnClickListener {
                    soundManager.playSound(R.raw.hit)
                    vm.addRandomRareOrUltraRare(350)
                    balance -= 350
                    bnd.txtPlayerBalance.text = balance.toString()
                    findNavController().navigate(DiamondShopFragmentDirections.actionDiamondShopFragmentToGettingCardsFragment())
                }
            }else {
                bnd.txtConfirmInfo.text = getString(R.string.not_enough_diamonds)
                bnd.btnConfirm.isGone = true
                bnd.confirmToast.isGone =false
            }

        }
        bnd.btnBoosterNormal.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            if (balance >= 120) {
                bnd.txtConfirmInfo.text = getString(R.string.purchase_item_confirmation, "120")
                bnd.confirmToast.isGone = false
                bnd.btnConfirm.isGone = false
                bnd.btnConfirm.setOnClickListener {
                    balance -= 120
                    bnd.txtPlayerBalance.text = balance.toString()
                    soundManager.playSound(R.raw.hit)
                    val newPlayer = vm.player.value!!
                    newPlayer.balance = balance
                    vm.getFreeCards(5, balance)
                    lifecycleScope.launch {
                        bnd.btnConfirm.setOnClickListener(null)
                        delay(1000)
                        soundManager.playSound(R.raw.message_in)
                        delay(500)
                        findNavController().navigate(DiamondShopFragmentDirections.actionDiamondShopFragmentToGettingCardsFragment())
                    }
                }


            } else {
                bnd.txtConfirmInfo.text = getString(R.string.not_enough_diamonds)
                bnd.btnConfirm.isGone = true
                bnd.confirmToast.isGone =false
            }
        }
        bnd.btnRareBooster.setOnClickListener {
            if (balance >= 450) {
                bnd.txtConfirmInfo.text = getString(R.string.purchase_item_confirmation, "450")
                bnd.confirmToast.isGone = false
                bnd.btnConfirm.isGone = false
                bnd.btnConfirm.setOnClickListener {
                    soundManager.playSound(R.raw.hit)
                    vm.getRareBooster(450)
                    balance -= 450
                    bnd.txtPlayerBalance.text = balance.toString()
                    lifecycleScope.launch {
                        delay(2000)
                        soundManager.playSound(R.raw.message_in)
                        findNavController().navigate(DiamondShopFragmentDirections.actionDiamondShopFragmentToGettingCardsFragment())
                    }
                }
            } else {
                bnd.txtConfirmInfo.text = getString(R.string.not_enough_diamonds)
                bnd.btnConfirm.isGone = true
                bnd.confirmToast.isGone =false
            }
        }

    }


    fun startTurning(view: View) {
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

    private fun addLand(filter: String) {
        soundManager.playSound(R.raw.button_click)
        if (balance >= 20) {
            bnd.txtConfirmInfo.text = getString(R.string.purchase_item_confirmation, "20")
            bnd.confirmToast.isGone = false
            bnd.btnConfirm.isGone = false
            bnd.btnConfirm.setOnClickListener {
                soundManager.playSound(R.raw.message_in)
                vm.addCards(listOf(vm.cardLibrary.value!!.filter { it.cardType == "Land" && it.type == filter }
                    .random().toCard()))
                balance -= 20
                bnd.txtPlayerBalance.text = balance.toString()
                bnd.txtPlayerBalanceShadow.text = balance.toString()
                bnd.confirmToast.isGone = true
            }
        } else {
            bnd.txtConfirmInfo.text = getString(R.string.not_enough_diamonds)
            bnd.btnConfirm.isGone = true
            bnd.confirmToast.isGone =false
        }
    }

}