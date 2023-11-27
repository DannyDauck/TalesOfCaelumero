package com.example.talesofcaelumora.ui

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
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
import com.example.talesofcaelumora.data.utils.BattleCallback
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.databinding.FragmentBattleBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


class BattleFragment : Fragment(), BattleCallback {

    private lateinit var bnd: FragmentBattleBinding
    private lateinit var soundManager: SoundManager
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

        soundManager = SoundManager.getInstance(requireContext())
        //<editor-fold desc="Ok button from grid selection">

        //Boolean to request twice
        var sureBool = false
        bnd.btnOkGrid.setOnClickListener {

            if (bnd.txtGridRecyclerHeader.text.toString() == "Spielerhand") {
                bnd.gridSelection.isVisible = false
                exampleBattle.sortCards()
                soundManager.playSound(R.raw.sort_cards)
            } else if (bnd.txtGridRecyclerHeader.text.toString() == "Landresourcen") {

                if (exampleBattle.playerOneLands.filter { it.selected }.size > 6) throwToast("Bitte wähle maximal 6 Landresouren")
                else {
                    bnd.gridSelection.isVisible = false
                    if (exampleBattle.playerOneLands.filter { it.selected }
                            .isNotEmpty()) soundManager.playSound(R.raw.sort_cards) else soundManager.playSound(
                        R.raw.button_click
                    )
                    updateSelectedLands()
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
                    exampleBattle.startAbility(target, actionCard, 1)
                    target.forEach { it.selected = false }
                } else throwToast("Bitte wähle ein Ziel")
            //Achtung darf nicht gelöscht werden, hier durch den das Wort
            // "ein" im RecyclerHeader filtert ob es first oder second ability ist

            }else if (bnd.txtGridRecyclerHeader.text == "Wähle ein Gegner") {
                //Auch hier sollte die Abfrage durch 'onesel' sinnlos sein
                if (target.filter { it.selected }.size == 1) {
                    Log.d("BattleFragment", "Ok Button in 'Wähle einen Gegner wurde gedrückt'")
                    target = target.filter { it.selected }
                    rvHorizontal.update(exampleBattle.playerTwoBank, "recognize hit progressbar")
                    exampleBattle.startAbility(target, actionCard, 2)
                    target.forEach { it.selected = false }
                } else throwToast("Bitte wähle ein Ziel")


            } else if (bnd.txtGridRecyclerHeader.text == "Opponent Heroes") {
                bnd.gridSelection.isVisible = false
            } else if (bnd.txtGridRecyclerHeader.text == "Hero bank") {
                bnd.gridSelection.isVisible = false
                exampleBattle.playerOneHand.forEach { !it.selected }
            } else if (bnd.txtGridRecyclerHeader.text.contains("Friedhof")) {
                bnd.gridSelection.isVisible = false
            } else if (bnd.txtGridRecyclerHeader.text == "Wähle ${actionCard.firstAbilityPoints} Länder") {

                if (exampleBattle.playerOneLands.filter { it.selected }.size == actionCard.firstAbilityPoints || sureBool) {
                    val cards = exampleBattle.playerOneLands.filter { it.selected }
                    cards.forEach {
                        it.used = false
                        it.selected = false
                    }
                    lifecycleScope.launch {
                        delay(1000)
                        rvGrid.update(exampleBattle.playerOneLands, "untap")
                        delay(1000)
                        sureBool = false
                        bnd.gridSelection.isVisible = false
                        exampleBattle.playerOneHand.remove(actionCard)
                        actionCard.selected = false
                        exampleBattle.playerOneGraveyard.add(actionCard)
                    }


                } else if (exampleBattle.playerOneLands.filter { it.selected }.size > actionCard.firstAbilityPoints) {
                    throwToast("Bitte wähle nur ${actionCard.firstAbilityPoints} Länder aus")
                } else if (exampleBattle.playerOneLands.filter { it.selected }.size < actionCard.firstAbilityPoints && !sureBool) {
                    throwToast(
                        "Du kannst noch ${actionCard.firstAbilityPoints - exampleBattle.playerOneLands.filter { it.selected }.size} Land wählen." +
                                "\nBist du sicher?"
                    )
                    sureBool = true
                }
            } else if (bnd.txtGridRecyclerHeader.text == "Wähle ein Verbündeten") {
                if (exampleBattle.playerOneBank.filter { it.selected }.size == 1) {
                    Log.d("BattleFragment", "Single heal gestartet")
                    bnd.txtGridRecyclerHeader.text = "Hero bank"
                    bnd.txtGridRecyclerHeaderShadow.text = "Hero bank"
                    startHealAnimation(exampleBattle.playerOneBank.filter { it.selected })
                }
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

            setUpExampleBattle()

            viewModel.currentCard.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it.cardType == "Land" && bnd.txtGridRecyclerHeader.text == "Landresourcen") bnd.btnSetWallpaper.isVisible =
                        true else bnd.btnSetWallpaper.isGone = true
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

                val path = File(
                    requireContext().filesDir,
                    "images/${viewModel.currentCard.value!!.id}.jpeg"
                ).absolutePath
                val drawable = Drawable.createFromPath(path)
                bnd.root.background = drawable
                when (viewModel.currentCard.value!!.type) {
                    "air" -> {
                        bnd.navbar.setBackgroundResource(R.drawable.navbar_bg_air)
                        bnd.clBattleground.setBackgroundResource(R.drawable.battleground_two)
                    }

                    "water" -> {
                        bnd.navbar.setBackgroundResource(R.drawable.navbar_bg_water)
                        bnd.clBattleground.setBackgroundResource(R.drawable.battleground_two)
                    }

                    "fire" -> {
                        bnd.navbar.setBackgroundResource(R.drawable.navbar_fire)
                        bnd.clBattleground.setBackgroundResource(R.drawable.battleground_three)
                    }

                    "plant" -> {
                        bnd.navbar.setBackgroundResource(R.drawable.navbar_bg_nature)
                        bnd.clBattleground.setBackgroundResource(R.drawable.battleground_one)
                    }
                }
                soundManager.changeSong()
                lifecycleScope.launch {
                    delay(2000)
                    bnd.marqueeText.text =
                        "Welcome to ${viewModel.currentCard.value!!.firstAbilityName}. You are listen to ${soundManager.song}    "
                }
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

        exampleBattle.setUpBattleField(bnd)


        bnd.btnSingleCardOk.setOnClickListener {
            bnd.bigCard.isVisible = false
            bnd.gridSelection.isVisible = true
        }


        //</editor-fold>


        //<editor-fold desc="Button DetailView">

        bnd.btnDetailView.setOnClickListener {
            if (bnd.txtGridRecyclerHeader.text == "Spielerhand") {
                soundManager.playSound(R.raw.button_click)
                if (exampleBattle.playerOneHand.filter { it.selected }.size == 1) {
                    viewModel.setCurrentCard(exampleBattle.playerOneHand.filter { it.selected }
                        .first())
                } else throwToast("Bitte wähle für die Detailansicht nur eine Karte aus")
            }
            if (bnd.txtGridRecyclerHeader.text == "Hero bank") {
                soundManager.playSound(R.raw.button_click)
                if (exampleBattle.playerOneBank.filter { it.selected }.size == 1) {
                    viewModel.setCurrentCard(exampleBattle.playerOneBank.filter { it.selected }
                        .first())
                } else throwToast("Bitte wähle für die Detailansicht eine Karte aus")
            }
            if (bnd.txtGridRecyclerHeader.text == "Opponent Heroes" || bnd.txtGridRecyclerHeader.text == "Wähle einen Gegner") {
                soundManager.playSound(R.raw.button_click)
                if (exampleBattle.playerTwoBank.filter { it.selected }.size == 1) {

                    viewModel.setCurrentCard(exampleBattle.playerTwoBank.filter { it.selected }
                        .first())
                } else throwToast("Bitte wähle für die Detailansicht eine Karte aus")
            }
            if (bnd.txtGridRecyclerHeader.text == "Landresourcen") {
                soundManager.playSound(R.raw.button_click)

                if (exampleBattle.playerOneLands.filter { it.selected }.size == 1) {
                    viewModel.setCurrentCard(exampleBattle.playerOneLands.filter { it.selected }
                        .first())
                } else throwToast("Bitte wähle für die Detailansicht eine Karte aus")
            }
        }
        //</editor-fold>

        //<editor-fold desc="Button First Ability">
        bnd.btnFirstAbility.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            if (bnd.clBattleground.rotation == 0f && bnd.txtGridRecyclerHeader.text != "Wähle einen Gegner" && bnd.txtGridRecyclerHeader.text != "Wähle ein Gegner" && bnd.txtGridRecyclerHeader.text != "Friedhof") {
                //Hero
                if (viewModel.currentCard.value!!.cardType == "Hero") {
                    if (exampleBattle.round < 2) {
                        if (!viewModel.currentCard.value!!.used) {
                            if (checkAbilityCosts()) {
                                doAction = true
                                doHeroAbility()
                            } else throwToast("Bitte wähle genugend Resourcen aus")
                        } else throwToast("Die Karte wurde in dieser Runde schon benutzt")
                    } else throwToast("Du kannst in der ersten Runde noch keine Heldenfähigkeiten benuzten")
                }

                //Supporter
                else if (viewModel.currentCard.value!!.cardType == "Supporter") {

                    actionCard = viewModel.currentCard.value!!
                    executeSupporterCard()
                    doAction = true

                }
            }
        }
        //</editor-fold>

        //<editor-fold desc="Button Second Ability">
        bnd.btnSecAbility.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            if (bnd.clBattleground.rotation == 0f && bnd.txtGridRecyclerHeader.text != "Wähle einen Gegner" && bnd.txtGridRecyclerHeader.text != "Friedhof") {
                //Hero
                if (viewModel.currentCard.value!!.cardType == "Hero") {
                    if (exampleBattle.round < 2) {
                        if (!viewModel.currentCard.value!!.used) {
                            if (checkAbilityCosts(2)) {
                                doAction = true
                                doHeroAbility(2)
                            } else throwToast("Bitte wähle genugend Resourcen aus")
                        } else throwToast("Die Karte wurde in dieser Runde schon benutzt")
                    } else throwToast("Du kannst in der ersten Runde noch keine Heldenfähigkeiten benuzten")
                }

                //Supporter
                else if (viewModel.currentCard.value!!.cardType == "Supporter") {

                    actionCard = viewModel.currentCard.value!!
                    executeSupporterCard()
                    doAction = true

                }
            }
        }
        //</editor-fold>

        //<editor-fold desc="Media Player">
        val battlefield = viewModel.currentBattle.value!!.battlefield

        //Backgroundmusic
        soundManager.startRadio(battlefield.music)

        //HeroBank
        bnd.btnHeroBank.setOnClickListener {


            if (bnd.gridSelection.isVisible == false && bnd.bigCard.isVisible == false) {
                soundManager.playSound(R.raw.button_click)

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
            soundManager.playSound(R.raw.button_click)
            if (soundManager.radio.isPlaying) {
                soundManager.pause()
                bnd.btnMusicOnOffImg.setImageResource(R.drawable.music_off)
            } else {
                soundManager.resume()
                bnd.btnMusicOnOffImg.setImageResource(R.drawable.music_on)
            }
        }
        bnd.btnBack.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            findNavController().popBackStack()
        }
        bnd.btnChangeSong.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            soundManager.changeSong()
        }

        //</editor-fold>

        //<editor-fold desc= "Side bar">
        bnd.btnSidebarOnOff.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            if (!bnd.gridSelection.isVisible) slideOut()
            bnd.svLegend.isVisible = false
        }


        bnd.btnTurnTable.setOnClickListener {
            soundManager.playSound(R.raw.button_click)

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
            soundManager.playSound(R.raw.button_click)

            if (bnd.clBattleground.rotation == 0f) {
                if (exampleBattle.playerOneGraveyard.isEmpty()) throwToast("Dein Friedhof ist noch leer")
                else {
                    rvGrid.update(exampleBattle.playerOneGraveyard)
                    rvGrid.changeType("")
                    bnd.txtGridRecyclerHeader.text = "Friedhof"
                    bnd.txtGridRecyclerHeaderShadow.text = "Friedhof"
                    bnd.rvHorizontal.isVisible = false
                    bnd.gridSelection.isVisible = true
                }
            } else {
                if (exampleBattle.playerTwoGraveyard.isEmpty()) throwToast("Gegner Friedhof ist noch leer")
                else {
                    rvGrid.update(exampleBattle.playerTwoGraveyard)
                    rvGrid.changeType("")
                    bnd.txtGridRecyclerHeader.text = "Friedhof des Gegners"
                    bnd.txtGridRecyclerHeaderShadow.text = "Friedhof des Gegners"
                    bnd.rvHorizontal.isVisible = false
                    bnd.gridSelection.isVisible = true
                }


            }
            slideOut()

        }
        bnd.btnHand.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
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
            soundManager.playSound(R.raw.button_click)
            bnd.rvHorizontal.isVisible = false
            bnd.txtGridRecyclerHeader.text = "Landresourcen"
            bnd.txtGridRecyclerHeaderShadow.text = "Landresourcen"
            rvGrid.update(exampleBattle.playerOneLands, "selection")
            bnd.rvGrid.isVisible = true
            bnd.gridSelection.isVisible = true
            if (bnd.sidebar.translationX != resources.displayMetrics.density * 80f) slideOut()
        }
        //</editor-fold>

        //<editor-fold desc="Bottom navigation">
        bnd.btnLegend.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
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

        startMarqueeText()

    }

    //<editor-fold desc="Main overrides">
    override fun onPause() {
        super.onPause()
        soundManager.pause()
    }

    override fun onResume() {
        bnd.gridSelection.isVisible = false
        bnd.bigCard.isVisible = false
        super.onResume()
        soundManager.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
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

    private fun checkAbilityCosts(ability: Int? = 1): Boolean {
        //muss hier gemacht werden weil die aktive Karte erst übergeben werden kann,
        // wenn die Kosten auch gedeckt sind
        var cardList =
            if (currentPlayer == 0f) exampleBattle.playerOneLands.filter { it.selected } else exampleBattle.playerTwoBank.filter { it.selected }
        val mutableList = mutableListOf<String>()
        cardList.forEach { mutableList.add(it.type) }
        val listToCheck =
            if (ability == 1) viewModel.currentCard.value!!.firstAbilityCosts.toMutableList() else viewModel.currentCard.value!!.secAbilityCosts.toMutableList()
        if (!mutableList.containsAll(listToCheck.filter { it != "colorless" })) return false
        else {
            mutableList.removeAll(listToCheck.filter { it != "colorless" })
            if (mutableList.size >= listToCheck.filter { it == "colorless" }.size) {
                listToCheck.filter { it != "colorless" }.forEach {
                    var checkList = listToCheck.filter { it != "colorless" }.toMutableList()
                    cardList.forEach { card ->
                        if (checkList.contains(card.type)) {
                            card.selected = false
                            card.used = true
                            checkList.remove(card.type)
                        }
                    }
                }
                cardList = cardList.filter { it.selected }
                repeat(listToCheck.filter { it == "colorless" }.size) {
                    cardList[it].selected = false
                    cardList[it].used = true
                }
                if (ability == 2) {
                    val removeList = if (currentPlayer == 0f) exampleBattle.playerOneLands.filter {
                        it.used
                    } else exampleBattle.playerTwoLands.filter {
                        it.used
                    }
                    if (currentPlayer == 0f) {
                        exampleBattle.playerOneLands.removeAll(removeList)
                        removeList.forEach { it.used = false }
                        exampleBattle.playerOneGraveyard.addAll(removeList)

                    } else {
                        exampleBattle.playerTwoLands.removeAll(removeList)
                        removeList.forEach { it.used = false}
                        exampleBattle.playerTwoGraveyard.addAll(removeList)
                    }

                }
                updateSelectedLands()
            }
            return (mutableList.size >= listToCheck.filter { it == "colorless" }.size)
        }
    }

    private fun startMarqueeText() {
        //ist die einzige Möglichkeit die funktioniert hat
        bnd.marqueeText.text =
            "Welcome to Calumero. ++ You are listening to \"${soundManager.song}\"     ++ Enjoy the battle ++ "
        lifecycleScope.launch {
            delay(500)
            bnd.marqueeText.isSelected = true
            bnd.marqueeText.text =
                "Welcome to Calumero. ++ You are listening to \"${soundManager.song}\"     ++ Enjoy the battle ++ "
        }


    }

    fun updateSelectedLands() {
        //setzt die MiniIcons der gerade selektierten Länder
        val views = listOf(
            bnd.landSelectOne,
            bnd.landSelectTwo,
            bnd.landSelectThree,
            bnd.landSelectFour,
            bnd.landSelectFive,
            bnd.landSelectSix
        )
        views.forEach { it.isVisible = false }
        if (currentPlayer == 0f) {
            repeat(exampleBattle.playerOneLands.filter { it.selected }.size) { index ->
                views[index].setImageResource(
                    when (exampleBattle.playerOneLands.filter { card -> card.selected }[index].type) {
                        "fire" -> R.drawable.firechip
                        "water" -> R.drawable.waterchip
                        "air" -> R.drawable.airchip
                        "plant" -> R.drawable.plantchip
                        //kann nicht passieren
                        else -> R.drawable.colorlesschip
                    }
                )
                views[index].isVisible = true
            }
        } else {
            repeat(exampleBattle.playerTwoLands.filter { it.selected }.size) { index ->
                views[index].setImageResource(
                    when (exampleBattle.playerTwoLands.filter { card -> card.selected }[index].type) {
                        "fire" -> R.drawable.firechip
                        "water" -> R.drawable.waterchip
                        "air" -> R.drawable.airchip
                        "plant" -> R.drawable.plantchip
                        //kann nicht passieren
                        else -> R.drawable.colorlesschip
                    }
                )
                views[index].isVisible = true
            }
        }
    }

    fun getPlayerHand() {
        exampleBattle.playerOneStack.shuffle()
        lifecycleScope.launch {
            repeat(5 - exampleBattle.playerOneHand.size) {

                delay(1000)
                exampleBattle.getCard(exampleBattle.playerOneStack, exampleBattle.playerOneHand)
                rvHorizontal.update(exampleBattle.playerOneHand)
                soundManager.playSound(R.raw.card_flip)
                rvHorizontal.startAnimation(
                    listOf(exampleBattle.playerOneHand.last()),
                    resources.displayMetrics.density
                )
            }
            delay(2000)
            if (exampleBattle.round < 2) checkFirstPlayerHandForHeroes()
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


    fun doHeroAbility(type:Int? = 1) {
        Log.d("BattleFragment", "doFirstHeroAbility started")
        val card = viewModel.currentCard.value!!
        val flipCard = if(type== 2)card.flipAbility()else card
        if (flipCard.firstAbilityType.contains("single damage")) {
            bnd.bigCard.isVisible = false
            actionCard = card

            target =
                if (currentPlayer == 0.0f) exampleBattle.playerTwoBank else exampleBattle.playerOneBank
            Log.d("BattleFragment", "$currentPlayer")
            if (target.isNotEmpty()&&type==1) {
                rvHorizontal.update(target, "onesel progressbar")
                bnd.txtGridRecyclerHeader.text = "Wähle einen Gegner"
                bnd.txtGridRecyclerHeaderShadow.text = "Wähle einen Gegner"
                bnd.gridSelection.isVisible = true
            }else if (target.isNotEmpty()&&type==2) {
                rvHorizontal.update(target, "onesel progressbar")
                bnd.txtGridRecyclerHeader.text = "Wähle ein Gegner"
                bnd.txtGridRecyclerHeaderShadow.text = "Wähle ein Gegner"
                bnd.gridSelection.isVisible = true
            } else {
                Log.d("BattleFragment", "attacking opponent")
                exampleBattle.startAbility(listOf(), card, type!!)
            }

        }
        if (flipCard.firstAbilityType.contains("multi damage")) {
            actionCard = card
            bnd.rvGrid.isVisible = false
            bnd.rvHorizontal.isVisible = true
            rvHorizontal.update(exampleBattle.playerTwoBank, "recognize hit")
            bnd.txtGridRecyclerHeader.text = "Opponent Heroes"
            bnd.txtGridRecyclerHeaderShadow.text = "Opponent Heroes"
            bnd.bigCard.isVisible = false
            bnd.gridSelection.isVisible = true
            exampleBattle.startAbility(exampleBattle.playerTwoBank, card, type!!)
        }
        if (flipCard.firstAbilityType.contains("single heal")) {
            actionCard = card
            bnd.rvGrid.isVisible = false
            bnd.rvHorizontal.isVisible = true
            exampleBattle.playerOneBank.forEach { it.selected = false }
            rvHorizontal.update(exampleBattle.playerOneBank, "heal onesel progressbar")
            bnd.txtGridRecyclerHeader.text = "Wähle ein Verbündeten"
            bnd.txtGridRecyclerHeaderShadow.text = "Wähle ein Verbündeten"
            bnd.bigCard.isVisible = false
            bnd.gridSelection.isVisible = true
        }
        if (flipCard.firstAbilityType.contains("multi heal")) {
            actionCard = card
            bnd.rvGrid.isVisible = false
            bnd.rvHorizontal.isVisible = true
            exampleBattle.playerOneBank.forEach { it.selected = false }
            rvHorizontal.update(exampleBattle.playerOneBank, "heal progressbar")
            bnd.txtGridRecyclerHeader.text = "Multi Heal"
            bnd.txtGridRecyclerHeaderShadow.text = "Multi Heal"
            bnd.bigCard.isVisible = false
            bnd.gridSelection.isVisible = true
            startHealAnimation(exampleBattle.playerOneBank, 2)
        }
        if (flipCard.firstAbilityType.contains("player heal")) {
            actionCard = card
            exampleBattle.playerOneCurrentHp += flipCard.firstAbilityPoints
            bnd.gridSelection.isVisible = false
            bnd.bigCard.isVisible = false
            updateLifebars()
        }


    }

    fun executeSupporterCard() {
        if (actionCard.firstAbilityType.contains("untap land")) {
            bnd.txtGridRecyclerHeader.text = "Wähle ${actionCard.firstAbilityPoints} Länder"
            bnd.txtGridRecyclerHeaderShadow.text = "Wähle ${actionCard.firstAbilityPoints} Länder"
            exampleBattle.playerOneLands.forEach { !it.selected }
            rvGrid.update(exampleBattle.playerOneLands, "untap")
            bnd.rvHorizontal.isVisible = false
            bnd.rvGrid.isVisible = true
            bnd.bigCard.isVisible = false
            bnd.gridSelection.isVisible = true
        } else if (actionCard.firstAbilityType.contains("untap all lands")) {
            bnd.txtGridRecyclerHeader.text = "Landresourcen"
            bnd.txtGridRecyclerHeaderShadow.text = "Landresourcen"
            bnd.rvHorizontal.isVisible = false
            bnd.rvGrid.isVisible = true
            rvGrid.update(exampleBattle.playerOneLands, "selection")
            bnd.bigCard.isVisible = false
            bnd.gridSelection.isVisible = true
            exampleBattle.playerOneLands.forEach {
                it.used = false
                it.selected = false
            }
            lifecycleScope.launch {
                delay(1000)
                rvGrid.update(exampleBattle.playerOneLands, "selection")
                delay(1000)
                bnd.gridSelection.isVisible = false
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
            if (exampleBattle.playerOneCurrentHp * 100 / exampleBattle.playerOneMaxHp < bnd.playerLifebar.progress)
                soundManager.playSound(R.raw.wooden_wheel)
            else soundManager.playSound(R.raw.wooden_wheel_reversd)
        }
        if (exampleBattle.playerTwoCurrentHp * 100 / exampleBattle.playerTwoMaxHp != bnd.oppnentLifebar.progress) {
            colorAnimatorPlayerTwo.start()
            progressAnimatorPlayerTwo.start()
            if (exampleBattle.playerTwoCurrentHp * 100 / exampleBattle.playerTwoMaxHp < bnd.oppnentLifebar.progress)
                soundManager.playSound(R.raw.wooden_wheel)
            else soundManager.playSound(R.raw.wooden_wheel_reversd)
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
        if (actionCard.cardType == "Hero") actionCard.used = true
        if (actionCard.firstAbilityType.contains("protect")) actionCard.protected = true
        updateUI()
        updateLifebars()
        lifecycleScope.launch {
            delay(1500)
            bnd.gridSelection.isVisible = false
        }
    }

    override fun getPlayerOneHand() {
        getPlayerHand()
    }

    override fun startRecyclerAnimation(cards: List<Card>, type: Int?) {

        var abilitytype = if(type==2) actionCard.secAbilityType else actionCard.firstAbilityType

        lifecycleScope.launch {
            cards.forEach {
                rvHorizontal.startAnimation(listOf(it), resources.displayMetrics.density)
                if (doAction && abilitytype.contains("damage")) soundManager.playSound(R.raw.hit)
                else soundManager.playSound(R.raw.card_flip)
                delay(2000)
                if (it.currentHp == 0) {
                    rvHorizontal.changeType("")
                    rvHorizontal.startAnimation(listOf(it), resources.displayMetrics.density)
                    delay(1000)
                    exampleBattle.playerTwoBank.remove(it)
                    exampleBattle.playerTwoGraveyard.add(it)
                    soundManager.playSound(R.raw.card_flip)
                    rvHorizontal.update(exampleBattle.playerTwoBank)
                    updateUI()
                    delay(1500)
                }
            }
            bnd.gridSelection.isVisible = false
        }
    }

    fun startHealAnimation(bank: List<Card>,type:Int? = 1) {
        lifecycleScope.launch {
            bank.forEach {
                rvHorizontal.startAnimation(
                    listOf(it),
                    resources.displayMetrics.density
                )
                soundManager.playSound(R.raw.message_in)
                delay(2000)
                var heal = if(type==1)actionCard.firstAbilityPoints else actionCard.flipAbility().firstAbilityPoints
                it.currentHp += minOf(heal, it.hp - it.currentHp)
                rvHorizontal.notifyDataSetChanged()
            }
            doActionReset()
        }
    }
//</editor-fold>


    //<editor-fold desc="Functions to delete after all works">
    fun setUpExampleBattle() {
        exampleBattle.battleStarted = true
        exampleBattle.playerOneCurrentHp = 60
        exampleBattle.playerTwoCurrentHp = 80
        exampleBattle.playerTwoBank.addAll(
            listOf(
                tc("air_hero_elara_rare_cFf2Y5asKSMAGJWM1Y5e"),
                tc("fire_hero_melina_hqzyueKa6MqYZJfGoZt9"),
                tc("fire_hero_ray_JvR2UTtuwM2xTgBIiq8o")
            )
        )
        exampleBattle.playerTwoBank[2].currentHp = 17
        exampleBattle.playerOneBank.addAll(
            listOf(
                tc("water_hero_clarice_rareT1sRioMfKMyraQlraP4m"),
                tc("water_hero_kiara_XEjrQjpyPija7C7yUSMG"),
                tc("water_hero_maestro_roda_2WILHPamCL1KBKe9igMh"),
                tc("land_hero_hiker_SCHhfainnwj3Y5Vp6LFy")
            )
        )
        exampleBattle.playerOneMaxBank = 4
        exampleBattle.playerOneBank[2].currentHp = 85
        exampleBattle.playerOneLands.addAll(
            listOf(
                tc("nM4a5LlMqjzAGhPPeHpO"),
                tc("nM4a5LlMqjzAGhPPeHpO"),
                tc("nM4a5LlMqjzAGhPPeHpO"),
                tc("oL681YzLakMlKEDw4BKY"),
                tc("land_air_city_WjW0scCoqxkirUNr8BVb"),
                tc("land_air_city_WjW0scCoqxkirUNr8BVb"),
                tc("hNV0ovNakVZb4RuyANk2"),
                tc("hNV0ovNakVZb4RuyANk2"),
                tc("land_fire_desert_81r18gBVcOejlmIkVg3l"),
                tc("land_fire_desert_81r18gBVcOejlmIkVg3l"),
                tc("land_fire_desert_81r18gBVcOejlmIkVg3l"),
                tc("land_fire_desert_81r18gBVcOejlmIkVg3l")
            ).sortedBy { it.type }
        )
        exampleBattle.playerTwoLands.addAll(
            listOf(
                tc("nM4a5LlMqjzAGhPPeHpO"),
                tc("nM4a5LlMqjzAGhPPeHpO"),
                tc("nM4a5LlMqjzAGhPPeHpO"),
                tc("oL681YzLakMlKEDw4BKY"),
                tc("land_air_city_WjW0scCoqxkirUNr8BVb"),
                tc("land_air_city_WjW0scCoqxkirUNr8BVb"),
                tc("hNV0ovNakVZb4RuyANk2"),
                tc("hNV0ovNakVZb4RuyANk2"),
                tc("land_fire_desert_81r18gBVcOejlmIkVg3l"),
                tc("land_fire_desert_81r18gBVcOejlmIkVg3l"),
                tc("land_fire_desert_81r18gBVcOejlmIkVg3l")
            ).sortedBy { it.type }
        )
        exampleBattle.playerOneHand.addAll(
            listOf(
                tc("supporter_sandra_XML3SJrrV03qUi9TQnSD"),
                tc("fire_hero_seline_rare_XxNaO990P0kv1rJVkdLo"),
                tc("supporter_tech_support_Efor4MpB0IcKxMGyib8I")
            )
        )
        exampleBattle.playerTwoBank[2].protected = true
        exampleBattle.setBattleCallback(this)
        viewModel.setCurrentBattle(exampleBattle)
        exampleBattle.playerOneBank.forEach { it.currentHp -= 40 }


    }

    fun tc(id: String): Card {
        var library = viewModel.cardLibrary.value!!
        return library.filter { it.id == id }.first().toCard()
    }

    //</editor-fold>
}
