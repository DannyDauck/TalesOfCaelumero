package com.example.talesofcaelumora.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.CardAdapter
import com.example.talesofcaelumora.data.datamodel.Battle
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import com.example.talesofcaelumora.data.datamodel.battlefields
import com.example.talesofcaelumora.data.datamodel.examplePlayerDanny
import com.example.talesofcaelumora.data.datamodel.examplePlayerElara
import com.example.talesofcaelumora.data.musicVolume
import com.example.talesofcaelumora.data.songList
import com.example.talesofcaelumora.data.songTitle
import com.example.talesofcaelumora.data.utils.BattleCallback
import com.example.talesofcaelumora.databinding.FragmentBattleBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class BattleFragment : Fragment(), BattleCallback {

    private lateinit var bnd: FragmentBattleBinding
    private var mediaPlayer: MediaPlayer? = null
    private var battleId = "no battle set"
    private lateinit var exampleBattle: Battle
    private lateinit var playerHeroes: CardAdapter
    private lateinit var opponentHeroes: CardAdapter
    private lateinit var rvHorizontal: CardAdapter
    private lateinit var rvGrid: CardAdapter

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //Battle Id beim eröffnen des Battles abfragen
            battleId = it.getString("battle").toString()
        }
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
        if (viewModel.cardLibrary.value != null && viewModel.cardLibrary.value!!.isNotEmpty()) {
            Log.d("BattleFragment", viewModel.cardLibrary.value.toString())
            exampleBattle = Battle(
                examplePlayerDanny,
                examplePlayerElara,
                battlefields.random(),
                viewModel.cardLibrary.value!!
            )
            exampleBattle.playerOneCurrentHp = 60
            exampleBattle.setBattleCallback(this)
            viewModel.setCurrentBattle(exampleBattle)

            playerHeroes = CardAdapter(
                viewModel.currentBattle.value!!.playerOneHand,
                "",
                requireContext()
            )
            opponentHeroes = CardAdapter(
                viewModel.currentBattle.value!!.playerTwoBank,
                "",
                requireContext()
            )
            rvGrid = CardAdapter(
                viewModel.cardLibrary.value?.shuffled()!!
                    .plus(viewModel.cardLibrary.value!!.shuffled()), "selection", requireContext()
            )
            rvHorizontal = CardAdapter(
                listOf(), "", requireContext()
            )

            bnd.opponentHeroes.adapter = opponentHeroes
            bnd.playerHeroes.adapter = playerHeroes
            bnd.rvGrid.adapter = rvGrid
            bnd.rvHorizontal.adapter = rvHorizontal

            fun updateHeroAdapters() {
                opponentHeroes.update(viewModel.currentBattle.value!!.playerTwoBank)
                playerHeroes.update(viewModel.currentBattle.value!!.playerTwoBank)
            }

            fun updateLifebars() {
                bnd.playerLifebar.progress =
                    viewModel.currentBattle.value!!.playerOneMaxHp / 100 * viewModel.currentBattle.value!!.playerOneCurrentHp
                bnd.oppnentLifebar.progress =
                    viewModel.currentBattle.value!!.playerTwoMaxHp / 100 * viewModel.currentBattle.value!!.playerTwoCurrentHp
                bnd.txtPlayerHp.text =
                    viewModel.currentBattle.value!!.playerOneCurrentHp.toString() + "/" + viewModel.currentBattle.value!!.playerOneMaxHp + "HP"
                bnd.txtOpponentHp.text =
                    viewModel.currentBattle.value!!.playerTwoCurrentHp.toString() + "/" + viewModel.currentBattle.value!!.playerTwoMaxHp + "HP"
            }
            viewModel.currentBattle.observe(viewLifecycleOwner) {
                updateHeroAdapters()
                updateLifebars()
                rvHorizontal.update(viewModel.currentBattle.value!!.playerOneHand)
            }
            viewModel.currentBattle.value!!.setUpBattleField(bnd)
        }
        // }
        //viewModel.getCardLibrary()

        val battlefield = viewModel.currentBattle.value!!.battlefield


        //Backgroundmusic
        mediaPlayer = MediaPlayer.create(requireContext(), battlefield.music)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(musicVolume, musicVolume)
        mediaPlayer?.start()

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
            if (!bnd.gridSelection.isVisible) slideOut()
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
            bnd.clBattleground.rotation =
                bnd.clBattleground.rotation % 360 - (bnd.clBattleground.rotation % 180)

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
            if (bnd.svLegend.isVisible) bnd.btnLegend.setBackgroundResource(R.color.bottom_bar_selected_ico) else bnd.btnLegend.setBackgroundResource(
                R.color.nothing
            )
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
            var dateTimeString = viewModel.dateTime.value?.datetime?.substring(0, 16)
            val dateTime = LocalDateTime.parse(dateTimeString)
            //bnd.marqueeText.text = bnd.marqueeText.text.toString() + " " + dateTimeString
            bnd.marqueeText.text =
                songTitle[random] + "   Es ist jetzt " + dateTime.hour + " Uhr " + dateTime.minute + " am " + dateTime.dayOfMonth + ". " + dateTime.month + "  im Jahr " + dateTime.year.plus(
                    -1647
                ) + "des Windes in Caelumero"

        }

    }

    fun slideOut() {
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

    fun getPlayerHand() {
        exampleBattle.playerOneStack.shuffle()
        bnd.marqueeText.text = "Kartendeck wurde gemischt"
        lifecycleScope.launch {
            delay(1000)
            exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
            rvHorizontal.update(exampleBattle.playerOneHand)
            delay(1000)
            exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
            rvHorizontal.update(exampleBattle.playerOneHand)
            delay(1000)
            exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
            rvHorizontal.update(exampleBattle.playerOneHand)
            delay(1000)
            exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
            rvHorizontal.update(exampleBattle.playerOneHand)
            delay(1000)
            exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
            rvHorizontal.update(exampleBattle.playerOneHand)
            delay(2000)
            checkFirstPlayerHandForHeroes()
        }

    }

    fun checkFirstPlayerHandForHeroes() {
        if (exampleBattle.playerOneHand.filter { it.cardType == "Hero" }.isEmpty()) {
            bnd.marqueeText.text =
                "Erste Spielerhand enthielt keinen Helden. Karten kommen zurück in den Stapel. Stapel wurde gemischt"
            lifecycleScope.launch {
                delay(3000)
                exampleBattle.playerHandToStack()
                rvHorizontal.update(exampleBattle.playerOneHand)
                getPlayerHand()
            }
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

    override fun getPlayerOneHand() {
        getPlayerHand()
    }

    override fun updateUI() {
        playerHeroes.update(exampleBattle.playerOneBank)
        opponentHeroes.update(exampleBattle.playerTwoBank)
        bnd.txtAirResources.text = exampleBattle.playerOneLands.filter { it.type == "Air" }.size.toString() + "/" + exampleBattle.playerOneMaxLand
        bnd.txtWaterResources.text = exampleBattle.playerOneLands.filter { it.type == "Water" }.size.toString() + "/" + exampleBattle.playerOneMaxLand
        bnd.txtFireResources.text = exampleBattle.playerOneLands.filter { it.type == "Fire" }.size.toString() + "/" + exampleBattle.playerOneMaxLand
        bnd.txtNatureResources.text = exampleBattle.playerOneLands.filter { it.type == "Plant" }.size.toString() + "/" + exampleBattle.playerOneMaxLand
    }
}
