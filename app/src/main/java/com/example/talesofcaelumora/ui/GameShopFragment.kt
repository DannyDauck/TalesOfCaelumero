package com.example.talesofcaelumora.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.FragmentGameShopBinding
import com.example.talesofcaelumora.data.utils.GradientText
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import com.google.firebase.database.collection.LLRBNode.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GameShopFragment : Fragment() {

    private lateinit var bnd: FragmentGameShopBinding
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
        bnd = FragmentGameShopBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        startTurning(bnd.imgBg)


        vm.getDayOfTheWeek()
        setBagIncreaseDones()

        vm.dayOfTheWeek.observe(viewLifecycleOwner) {
            setDayBoosterLayout(it)
        }

        vm.countdownText.observe(viewLifecycleOwner) {
            bnd.dayBoosterCountdown.text = it
        }

        bnd.btnBooster.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            bnd.boosterOffers.isVisible = true
            bnd.singleCardOffers.isGone = true
            bnd.othersOffers.isGone = true
            bnd.txtBooster.setTextColor(ContextCompat.getColor(requireContext(), R.color.smooth_gold))
            bnd.txtSingleCard.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_diselected))
            bnd.txtOthers.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_diselected))
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.register_selected))
            bnd.btnOthers.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.register_disselected))
            bnd.btnSingleCards.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.register_disselected))
        }
        bnd.btnSingleCards.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            bnd.boosterOffers.isGone = true
            bnd.singleCardOffers.isVisible = true
            bnd.othersOffers.isGone = true
            bnd.txtBooster.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_diselected))
            bnd.txtSingleCard.setTextColor(ContextCompat.getColor(requireContext(), R.color.smooth_gold))
            bnd.txtOthers.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_diselected))
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.register_selected))
            bnd.btnOthers.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.register_disselected))
            bnd.btnBooster.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.register_disselected))
        }
        bnd.btnOthers.setOnClickListener {
            setBagIncreaseDones()
            soundManager.playSound(R.raw.button_click)
            bnd.boosterOffers.isGone = true
            bnd.singleCardOffers.isGone = true
            bnd.othersOffers.isVisible = true
            bnd.txtBooster.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_diselected))
            bnd.txtSingleCard.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_diselected))
            bnd.txtOthers.setTextColor(ContextCompat.getColor(requireContext(), R.color.smooth_gold))
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.register_selected))
            bnd.btnBooster.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.register_disselected))
            bnd.btnSingleCards.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.register_disselected))
        }

        bnd.btnBoosterNormal.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            vm.getFreeCards(5)
            findNavController().navigate(GameShopFragmentDirections.actionGameShopFragmentToGettingCardsFragment())
        }
        bnd.btnRareBooster.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            vm.getRareBooster()
            lifecycleScope.launch {
                delay(1200)
                findNavController().navigate(GameShopFragmentDirections.actionGameShopFragmentToGettingCardsFragment())
            }
       }
        bnd.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun setDayBoosterLayout(day: String) {

        when (day) {
            "MONDAY" -> {
                bnd.imgBoosterOfTheDayBg.setImageResource(R.drawable.air_deck_bg)
                bnd.imgBoosterOfTheDay.setImageResource(R.drawable.male_air_character_removebg_preview)
                bnd.txtBoosterOfTheDayInfo.text = getString(R.string.booster_of_the_day_air)
                bnd.txtBoosterOfTheDayInfoShadow.text = getString(R.string.booster_of_the_day_air)
                bnd.btnBoosterOfTheDay.setOnClickListener {
                    vm.getBoosterOfTheDay("air")
                    findNavController().navigate(GameShopFragmentDirections.actionGameShopFragmentToGettingCardsFragment())
                }
            }

            "TUESDAY" -> {
                bnd.imgBoosterOfTheDayBg.setImageResource(R.drawable.magic_waterfalls)
                bnd.imgBoosterOfTheDay.setImageResource(R.drawable.female_water_character_2)
                bnd.txtBoosterOfTheDayInfo.text = getString(R.string.booster_of_the_day_water)
                bnd.txtBoosterOfTheDayInfoShadow.text = getString(R.string.booster_of_the_day_water)
                bnd.btnBoosterOfTheDay.setOnClickListener {
                    vm.getBoosterOfTheDay("water")
                    findNavController().navigate(GameShopFragmentDirections.actionGameShopFragmentToGettingCardsFragment())
                }
            }

            "WEDNESDAY" -> {
                bnd.imgBoosterOfTheDayBg.setImageResource(R.drawable.fire_battle_bg_two)
                bnd.imgBoosterOfTheDay.setImageResource(R.drawable.male_fire_character_removebg_preview)
                bnd.txtBoosterOfTheDayInfo.text = getString(R.string.booster_of_the_day_fire)
                bnd.txtBoosterOfTheDayInfoShadow.text = getString(R.string.booster_of_the_day_fire)
                bnd.btnBoosterOfTheDay.setOnClickListener {
                    vm.getBoosterOfTheDay("fire")
                    findNavController().navigate(GameShopFragmentDirections.actionGameShopFragmentToGettingCardsFragment())
                }
            }
            "THURSDAY" -> {
                bnd.imgBoosterOfTheDayBg.setImageResource(R.drawable.nature_deck_bg)
                bnd.imgBoosterOfTheDay.setImageResource(R.drawable.female_character_cutted)
                bnd.txtBoosterOfTheDayInfo.text = getString(R.string.booster_of_the_day_plant)
                bnd.txtBoosterOfTheDayInfoShadow.text = getString(R.string.booster_of_the_day_plant)
                bnd.btnBoosterOfTheDay.setOnClickListener {
                    vm.getBoosterOfTheDay("plant")
                    findNavController().navigate(GameShopFragmentDirections.actionGameShopFragmentToGettingCardsFragment())
                }
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

    private fun setBagIncreaseDones(){

        Log.d("GameShopFragment", vm.player.value!!.bagIncrease.toString() + vm.player.value!!.name.toString())
        bnd.btnIncreaseBag.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            var player = vm.player.value!!
            player.bagMaxSize += 20
            player.bagIncrease += 1
            //upsertFireBasePlayer updated auch den vm.player
            vm.upsertFirebasePLayer(player)
            vm.upsertPlayer(player)
            setBagIncreaseDones()
        }
        if (vm.player.value!!.bagIncrease>=1) {
            bnd.txtIncreaseBagDoneOne.isVisible = true
            if (vm.player.value!!.bagIncrease >= 2) {
                bnd.txtIncreaseBagDoneTwo.isVisible = true
                if (vm.player.value!!.bagIncrease == 3) {
                    bnd.txtIncreaseBagDoneThree.isVisible = true
                    bnd.btnIncreaseBag.setOnClickListener(null)
                    bnd.txtIncreaseBagInfo.text = getString(R.string.max_reached)
                    bnd.txtIncreaseBagInfoShadow.text = getString(R.string.max_reached)
                }
            }
        }
    }

}