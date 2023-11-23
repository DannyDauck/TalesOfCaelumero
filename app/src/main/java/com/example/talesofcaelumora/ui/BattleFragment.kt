package com.example.talesofcaelumora.ui

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.CardAdapter
import com.example.talesofcaelumora.data.datamodel.Battle
import com.example.talesofcaelumora.data.datamodel.Card
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
import java.io.File
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
    private lateinit var rvSingle: CardAdapter
    private var currentPlayer = 0f
    private var target = listOf<Card>()
    private lateinit var actionCard: Card
    private var doAction = false
    private var filledHandThisRound = false

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

        //<editor-fold desc="Ok button from grid selection">
        bnd.btnOkGrid.setOnClickListener {

            if (bnd.txtGridRecyclerHeader.text.toString() == "Spielerhand") {
                bnd.gridSelection.isVisible = false
                exampleBattle.sortCards()
            } else if (bnd.txtGridRecyclerHeader.text.toString() == "Landresourcen") {

                if (exampleBattle.playerOneLands.filter { it.selected }.size > 6) throwToast("Bitte wähle maximal 6 Landresouren")
                else {
                    bnd.gridSelection.isVisible = false
                    val selectedRes = listOf(
                        bnd.landSelectOne,
                        bnd.landSelectTwo,
                        bnd.landSelectThree,
                        bnd.landSelectFour,
                        bnd.landSelectFive,
                        bnd.landSelectSix
                    )
                    selectedRes.forEach {
                        it.isVisible = false
                    }
                    repeat(exampleBattle.playerOneLands.filter { it.selected }.size) { index ->
                        selectedRes[index].setImageResource(
                            when (exampleBattle.playerOneLands.filter { card -> card.selected }[index].type) {
                                "fire" -> R.drawable.firechip
                                "water" -> R.drawable.waterchip
                                "air" -> R.drawable.airchip
                                "plant" -> R.drawable.plantchip
                                //kann nicht passieren
                                else -> R.drawable.colorlesschip
                            }
                        )
                        selectedRes[index].isVisible = true
                    }
                    bnd.llLandselection.isVisible = true
                }
            } else if (bnd.txtGridRecyclerHeader.text == getString(R.string.legend)) {
                bnd.gridSelection.isVisible = false
                bnd.svLegend.isVisible = false
                if (bnd.svLegend.isVisible) bnd.btnLegend.setBackgroundResource(R.color.bottom_bar_selected_ico) else bnd.btnLegend.setBackgroundResource(
                    R.color.nothing
                )

            } else if (bnd.txtGridRecyclerHeader.text == "Wähle einen Gegner") {
                //Auch hier sollte die Abfrage durch 'onesel' sinnlos sein
                if (target.filter { it.selected }.size == 1) {
                    Log.d("BattleFragment", "Ok Button in 'Wähle einen Gegner wurde gedrückt'")
                    target = target.filter { it.selected }
                    rvHorizontal.update(exampleBattle.playerTwoBank, "recognize hit progressbar")
                    exampleBattle.startFirstAbility(target, actionCard)
                    target.forEach { it.selected = false }
                } else throwToast("Bitte wähle ein Ziel")
            } else if (bnd.txtGridRecyclerHeader.text == "Opponent Heroes") {
                bnd.gridSelection.isVisible = false
            } else if (bnd.txtGridRecyclerHeader.text == "Heroe Bank") {
                bnd.gridSelection.isVisible = false
            }
        }
        //</editor-fold>

        //<editor-fold desc="First settings when start">
        if (viewModel.cardLibrary.value != null && viewModel.cardLibrary.value!!.isNotEmpty()) {
            Log.d("BattleFragment", viewModel.cardLibrary.value.toString())
            exampleBattle = Battle(
                examplePlayerDanny,
                examplePlayerElara,
                battlefields.random(),
                viewModel.cardLibrary.value!!
            )
            exampleBattle.playerOneCurrentHp = 60
            exampleBattle.playerTwoCurrentHp = 80
            exampleBattle.setBattleCallback(this)
            exampleBattle.playerTwoBank.addAll(exampleBattle.playerTwoStack.filter { it.cardType == "Hero" }
                .subList(1, 4))
            viewModel.setCurrentBattle(exampleBattle)



            viewModel.currentBattle.value!!.setUpBattleField(bnd)
            viewModel.currentCard.observe(viewLifecycleOwner) {
                if (it != null) {
                    if(it.cardType=="Land"&&bnd.txtGridRecyclerHeader.text == "Landresourcen")bnd.btnSetWallpaper.isVisible=true else bnd.btnSetWallpaper.isGone= true
                    if (it.cardType == "Hero") rvSingle.update(
                        listOf(it),
                        "single card progressbar"
                    )
                    else rvSingle.update(listOf(it), "single card")
                    bnd.gridSelection.isVisible = false
                    bnd.bigCard.isVisible = true
                    if (bnd.sidebar.translationX != resources.displayMetrics.density * 80f) slideOut()
                }
            }
            bnd.btnSetWallpaper.setOnClickListener {
                val path = File(requireContext().filesDir, "images/${viewModel.currentCard.value!!.id}.jpeg").absolutePath
                val drawable = Drawable.createFromPath(path)
                bnd.root.background = drawable
                when(viewModel.currentCard.value!!.type){
                    "air" -> {
                        bnd.navbar.setBackgroundResource(R.drawable.navbar_bg_air)
                        bnd.clBattleground.setBackgroundResource(R.drawable.battleground_two)
                    }
                    "water"-> {
                        bnd.navbar.setBackgroundResource(R.drawable.navbar_bg_water)
                        bnd.clBattleground.setBackgroundResource(R.drawable.battleground_two)
                    }
                    "fire"-> {
                        bnd.navbar.setBackgroundResource(R.drawable.navbar_fire)
                        bnd.clBattleground.setBackgroundResource(R.drawable.battleground_three)
                    }
                    "plant"-> {
                        bnd.navbar.setBackgroundResource(R.drawable.navbar_bg_nature)
                        bnd.clBattleground.setBackgroundResource(R.drawable.battleground_one)
                    }
                }
                changeSong()
            }
        }

        //Erste Initialisierung der Adapter und Zuweisung
        playerHeroes = CardAdapter(
            listOf(),
            "",
            requireContext()
        )
        opponentHeroes = CardAdapter(
            listOf(),
            "",
            requireContext()
        )
        rvGrid = CardAdapter(
            listOf(), "selection", requireContext()
        )
        rvHorizontal = CardAdapter(
            listOf(), "", requireContext()
        )
        rvSingle = CardAdapter(
            listOf(), "single card", requireContext()
        )

        bnd.opponentHeroes.adapter = opponentHeroes
        bnd.playerHeroes.adapter = playerHeroes
        bnd.rvGrid.adapter = rvGrid
        bnd.rvHorizontal.adapter = rvHorizontal
        bnd.rvSingleCard.adapter = rvSingle

        bnd.btnSingleCardOk.setOnClickListener {
            bnd.bigCard.isVisible = false
            bnd.gridSelection.isVisible = true
        }


        //</editor-fold>

        //<editor-fold desc="Button DetailView">

        bnd.btnDetailView.setOnClickListener {
            if (bnd.txtGridRecyclerHeader.text == "Spielerhand") {
                if (exampleBattle.playerOneHand.filter { it.selected }.size == 1) {
                    viewModel.setCurrentCard(exampleBattle.playerOneHand.filter { it.selected }
                        .first())
                } else throwToast("Bitte wähle für die Detailansicht nur eine Karte aus")
            }
            if (bnd.txtGridRecyclerHeader.text == "Hero bank") {
                if (exampleBattle.playerOneBank.filter { it.selected }.size == 1) {
                    viewModel.setCurrentCard(exampleBattle.playerOneBank.filter { it.selected }
                        .first())
                } else throwToast("Bitte wähle für die Detailansicht eine Karte aus")
            }
            if (bnd.txtGridRecyclerHeader.text == "Opponent Heroes" || bnd.txtGridRecyclerHeader.text == "Wähle einen Gegner") {

                if (exampleBattle.playerTwoBank.filter { it.selected }.size == 1) {
                    viewModel.setCurrentCard(exampleBattle.playerTwoBank.filter { it.selected }
                        .first())
                } else throwToast("Bitte wähle für die Detailansicht eine Karte aus")
            }
            if (bnd.txtGridRecyclerHeader.text == "Landresourcen") {

                if (exampleBattle.playerOneLands.filter { it.selected }.size == 1) {
                    viewModel.setCurrentCard(exampleBattle.playerOneLands.filter { it.selected }
                        .first())
                } else throwToast("Bitte wähle für die Detailansicht eine Karte aus")
            }
        }
        //</editor-fold>

        //<editor-fold desc="Button First Ability">
        bnd.btnFirstAbility.setOnClickListener {
            Log.d("BattleFragment", "${bnd.clBattleground.rotation} und $currentPlayer")
            if (exampleBattle.round > -1) {
                if (viewModel.currentCard.value!!.cardType == "Hero" && bnd.clBattleground.rotation == currentPlayer && bnd.txtGridRecyclerHeader.text != "Wähle einen Gegner") {
                    Log.d("BattleFragment", "clicked on first ability button")
                    doAction = true
                    doFirstHeroAbility()

                }
            } else throwToast("Du kannst in der ersten Runde noch keine Heldenfähigkeiten verwenden")
        }
        //</editor-fold>

        //<editor-fold desc="Media Player">
        val battlefield = viewModel.currentBattle.value!!.battlefield

        //Backgroundmusic
        mediaPlayer = MediaPlayer.create(requireContext(), battlefield.music)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(musicVolume, musicVolume)
        mediaPlayer?.start()

        //HeroBank
        bnd.btnHeroBank.setOnClickListener {
            if (bnd.gridSelection.isVisible == false && bnd.bigCard.isVisible == false) {

                if (bnd.clBattleground.rotation % 360 == 0f && exampleBattle.playerOneBank.isNotEmpty()) {
                    bnd.txtGridRecyclerHeader.text = "Hero bank"
                    bnd.txtGridRecyclerHeaderShadow.text = "Hero bank"
                    rvHorizontal.update(exampleBattle.playerOneBank, "onesel progressbar")
                } else if (bnd.clBattleground.rotation % 360 == 180f && exampleBattle.playerTwoBank.isNotEmpty()) {
                    bnd.txtGridRecyclerHeader.text = "Opponent Heroes"
                    bnd.txtGridRecyclerHeaderShadow.text = "Opponent Heroes"
                    rvHorizontal.update(exampleBattle.playerTwoBank, "onesel progressbar")
                }
                bnd.gridSelection.isVisible = true
                bnd.rvGrid.isVisible = false
                bnd.rvHorizontal.isVisible = true
                if (bnd.sidebar.translationX != resources.displayMetrics.density * 80f) slideOut()
            }
        }
        //</editor-fold>

        //<editor-fold desc="Top navigation">
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
        //</editor-fold>

        //<editor-fold desc= "Side bar">
        bnd.btnSidebarOnOff.setOnClickListener {
            if (!bnd.gridSelection.isVisible) slideOut()
            bnd.svLegend.isVisible = false
        }


        bnd.btnTurnTable.setOnClickListener {

            //keine weitere Rotationanimation mehr, geht zu sehr auf die Performance!!!

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



        bnd.btnGraveyard.setOnClickListener {
            bnd.txtGridRecyclerHeader.text = "Friedhof"
            bnd.txtGridRecyclerHeaderShadow.text = "Friedhof"
            if (exampleBattle.playerOneGraveyard.isEmpty()) throwToast("Dein Friedhof ist noch leer")
            else {
                rvGrid.update(exampleBattle.playerOneGraveyard)
                rvGrid.changeType("")
                bnd.gridSelection.isVisible = true
            }
            slideOut()
        }
        bnd.btnHand.setOnClickListener {
            bnd.txtGridRecyclerHeader.text = "Spielerhand"
            bnd.txtGridRecyclerHeaderShadow.text = "Spielerhand"
            rvHorizontal.update(exampleBattle.playerOneHand, "selection")
            bnd.rvGrid.isVisible = false
            bnd.rvHorizontal.isVisible = true
            bnd.gridSelection.isVisible = true
            slideOut()
        }
        //</editor-fold>

        //<editor-fold desc="Landbar">
        bnd.landBar.setOnClickListener {
            bnd.rvHorizontal.isVisible = false
            bnd.txtGridRecyclerHeader.text = "Landresourcen"
            bnd.txtGridRecyclerHeaderShadow.text = "Landresourcen"
            rvGrid.update(exampleBattle.playerOneLands)
            bnd.rvGrid.isVisible = true
            bnd.gridSelection.isVisible = true
            if (bnd.sidebar.translationX != resources.displayMetrics.density * 80f) slideOut()
        }
        //</editor-fold>

        //<editor-fold desc="Bottom navigation">
        bnd.btnLegend.setOnClickListener {
            bnd.svLegend.isVisible = !bnd.svLegend.isVisible
            bnd.txtGridRecyclerHeader.text = getString(R.string.legend)
            bnd.txtGridRecyclerHeaderShadow.text = getString(R.string.legend)
            bnd.rvHorizontal.isVisible = false
            bnd.rvGrid.isVisible = false
            bnd.gridSelection.isVisible = bnd.svLegend.isVisible
            if (bnd.svLegend.isVisible) bnd.btnLegend.setBackgroundResource(R.color.bottom_bar_selected_ico) else bnd.btnLegend.setBackgroundResource(
                R.color.nothing
            )
            if (bnd.sidebar.translationX != resources.displayMetrics.density * 80f) slideOut()
        }
        //</editor-fold>

    }

//<editor-fold desc="Main overrides">
    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        bnd.gridSelection.isVisible = false
        bnd.bigCard.isVisible = false
        super.onResume()
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
//</editor-fold>

//<editor-fold desc= "Media Player">

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
            var place = if(viewModel.currentCard.value!!.cardType=="Land")viewModel.currentCard.value!!.firstAbilityName else "Caelumero"
            bnd.marqueeText.text =
                songTitle[random] + "   Willkommen in $place"

        }
    }

//</editor-fold>

// <editor-fold desc="Side bar">

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
    //</editor-fold>

//<editor-fold desc="UI and gamelogic">

    fun getPlayerHand() {
        exampleBattle.playerOneStack.shuffle()
        lifecycleScope.launch {
            delay(1000)
            exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
            rvHorizontal.update(exampleBattle.playerOneHand)
            rvHorizontal.startAnimation(
                listOf(exampleBattle.playerOneHand.last()),
                resources.displayMetrics.density
            )
            delay(1000)
            exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
            rvHorizontal.update(exampleBattle.playerOneHand)
            rvHorizontal.startAnimation(
                listOf(exampleBattle.playerOneHand.last()),
                resources.displayMetrics.density
            )
            delay(1000)
            exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
            rvHorizontal.update(exampleBattle.playerOneHand)
            rvHorizontal.startAnimation(
                listOf(exampleBattle.playerOneHand.last()),
                resources.displayMetrics.density
            )
            delay(1000)
            exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
            rvHorizontal.startAnimation(
                listOf(exampleBattle.playerOneHand.last()),
                resources.displayMetrics.density
            )
            rvHorizontal.update(exampleBattle.playerOneHand)
            delay(1000)
            exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
            rvHorizontal.update(exampleBattle.playerOneHand)
            rvHorizontal.startAnimation(
                listOf(exampleBattle.playerOneHand.last()),
                resources.displayMetrics.density
            )
            delay(2000)
            checkFirstPlayerHandForHeroes()
        }
    }

    fun checkFirstPlayerHandForHeroes() {
        if (exampleBattle.playerOneHand.filter { it.cardType == "Hero" }.isEmpty()) {
            throwToast("Erste Hand enthielt keinen Helden. Hand kommt zurück in den Stapel")
            lifecycleScope.launch {
                delay(2000)
                exampleBattle.playerHandToStack()
                rvHorizontal.update(exampleBattle.playerOneHand)
                getPlayerHand()
            }
        } else {
            rvHorizontal.changeType("selection")
            filledHandThisRound = true
        }
    }


    fun doFirstHeroAbility() {
        Log.d("BattleFragment", "doFirstHeroAbility started")
        val card = viewModel.currentCard.value!!
        if (card.firstAbilityType == "single damage") {
            Log.d("BattleFragment", "single damage recognized")
            bnd.bigCard.isVisible = false
            actionCard = card

            target =
                if (currentPlayer == 0.0f) exampleBattle.playerTwoBank else exampleBattle.playerOneBank
            Log.d("BattleFragment", "$currentPlayer")
            if (target.isNotEmpty()) {
                rvHorizontal.update(target, "onesel progressbar")
                bnd.txtGridRecyclerHeader.text = "Wähle einen Gegner"
                bnd.txtGridRecyclerHeaderShadow.text = "Wähle einen Gegner"
                bnd.gridSelection.isVisible = true
            } else {
                Log.d("BattleFragment", "attacking opponent")
                exampleBattle.startFirstAbility(listOf(), card)
            }

        }
    }

//</editor-fold>


//<editor-fold desc="BattleCallback overrides">

    override fun throwToast(string: String) {
        //Ein richtiger Toast braucht zu lange zum laden, von dem abgesehen ist das auffälliger und passt besser ins Design
        bnd.txtToast.text = string
        bnd.txtToast.isVisible = true
        lifecycleScope.launch {
            delay(3000)
            bnd.txtToast.isVisible = false
        }
    }

    override fun updateUI() {
        playerHeroes.update(exampleBattle.playerOneBank)
        opponentHeroes.update(exampleBattle.playerTwoBank)
        bnd.txtAirResources.text =
            exampleBattle.playerOneLands.filter { it.type == "air" }.size.toString() + "/" + exampleBattle.playerOneMaxLand
        bnd.txtWaterResources.text =
            exampleBattle.playerOneLands.filter { it.type == "water" }.size.toString() + "/" + exampleBattle.playerOneMaxLand
        bnd.txtFireResources.text =
            exampleBattle.playerOneLands.filter { it.type == "fire" }.size.toString() + "/" + exampleBattle.playerOneMaxLand
        bnd.txtNatureResources.text =
            exampleBattle.playerOneLands.filter { it.type == "plant" }.size.toString() + "/" + exampleBattle.playerOneMaxLand
    }

    override fun updateLifebars() {

        val progressAnimatorPlayerTwo = ObjectAnimator.ofInt(
            bnd.oppnentLifebar,
            "progress",
            bnd.oppnentLifebar.progress,
            100 * exampleBattle.playerTwoCurrentHp / exampleBattle.playerTwoMaxHp
        )
        val progressAnimatorPlayerOne = ObjectAnimator.ofInt(
            bnd.playerLifebar,
            "progress",
            bnd.playerLifebar.progress,
            100 * exampleBattle.playerOneCurrentHp / exampleBattle.playerOneMaxHp
        )

        //get the Lifebarcolors
        val colorPlayerOne =
            if (exampleBattle.playerOneCurrentHp.toFloat() / exampleBattle.playerOneMaxHp > 0.4) Color.GREEN
            else if (exampleBattle.playerOneCurrentHp.toFloat() / exampleBattle.playerOneMaxHp > 0.2) Color.YELLOW
            else Color.RED

        val colorPlayerTwo =
            if (exampleBattle.playerTwoCurrentHp.toFloat() / exampleBattle.playerTwoMaxHp > 0.4) Color.GREEN
            else if (exampleBattle.playerTwoCurrentHp.toFloat() / exampleBattle.playerTwoMaxHp > 0.2) Color.YELLOW
            else Color.RED

        //create Animators
        val colorAnimatorPlayerOne =
            ValueAnimator.ofObject(ArgbEvaluator(), Color.RED, colorPlayerOne)
        colorAnimatorPlayerOne.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            bnd.playerLifebar.progressTintList = ColorStateList.valueOf(color)
        }

        val colorAnimatorPlayerTwo =
            ValueAnimator.ofObject(ArgbEvaluator(), Color.RED, colorPlayerTwo)
        colorAnimatorPlayerTwo.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            bnd.oppnentLifebar.progressTintList = ColorStateList.valueOf(color)
        }

        progressAnimatorPlayerOne.duration = 2000
        progressAnimatorPlayerTwo.duration = 2000
        colorAnimatorPlayerOne.duration = 2000
        colorAnimatorPlayerTwo.duration = 2000

        progressAnimatorPlayerOne.interpolator = DecelerateInterpolator()
        progressAnimatorPlayerTwo.interpolator = DecelerateInterpolator()



        if (exampleBattle.playerOneCurrentHp * 100 / exampleBattle.playerOneMaxHp != bnd.playerLifebar.progress) {
            colorAnimatorPlayerOne.start()
            progressAnimatorPlayerOne.start()
        }
        if (exampleBattle.playerTwoCurrentHp * 100 / exampleBattle.playerTwoMaxHp != bnd.oppnentLifebar.progress) {
            colorAnimatorPlayerTwo.start()
            progressAnimatorPlayerTwo.start()
        }


        bnd.txtPlayerHp.text =
            exampleBattle.playerOneCurrentHp.toString() + "/" + exampleBattle.playerOneMaxHp + "HP"
        bnd.txtOpponentHp.text =
            exampleBattle.playerTwoCurrentHp.toString() + "/" + exampleBattle.playerTwoMaxHp + "HP"

        //Animations releasen
        lifecycleScope.launch {
            delay(2100)
            bnd.oppnentLifebar.clearAnimation()
            bnd.playerLifebar.clearAnimation()
        }
    }

    override fun doActionReset() {
        doAction = false
    }

    override fun getPlayerOneHand() {
        getPlayerHand()
    }

    override fun startRecyclerAnimation(cards: List<Card>) {
        lifecycleScope.launch {
            cards.forEach {
                rvHorizontal.startAnimation(listOf(it), resources.displayMetrics.density)
                delay(2000)
            }
            bnd.gridSelection.isVisible = false
        }
    }
    //</editor-fold>


}
