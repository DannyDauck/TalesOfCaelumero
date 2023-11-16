package com.example.talesofcaelumora.data.datamodel

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.talesofcaelumora.adapter.CardAdapter
import com.example.talesofcaelumora.data.utils.BattleCallback
import com.example.talesofcaelumora.databinding.FragmentBattleBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel

class Battle(
    var id: String,
    var battlefield: Battlefield,
    var playerOne: String,
    var playerTwo: String,
    var playerOneName: String,
    var playerTwoName: String,
    var playerOneStack: MutableList<Card>,
    var playerTwoStack: MutableList<Card>,
    var playerOneHand: MutableList<Card>,
    var playerTwoHand: MutableList<Card>,
    var playerOneBank: MutableList<Card>,
    var playerTwoBank: MutableList<Card>,
    var playerOneGraveyard: MutableList<Card>,
    var playerTwoGraveyard: MutableList<Card>,
    var playerOneLands: MutableList<Card>,
    var playerTwoLands: MutableList<Card>,
    val playerOneMaxHp: Int,
    var playerOneCurrentHp: Int,
    val playerTwoMaxHp: Int,
    var playerTwoCurrentHp: Int,
    var playerOneMaxBank: Int,
    var playerTwoMaxBank: Int,
    var playerOneMaxLand: Int,
    var playerTwoMaxLand: Int,
    val playerOneLevel: Int,
    val playerTwoLevel: Int,

    ) {

    constructor(
        playerOnePlayer: Player,
        playerTwoPlayer: Player,
        battlefieldIn: Battlefield,
        library: List<Card>
    ) : this(
        id = playerOnePlayer.uid + "_vs_" + playerTwoPlayer.uid,
        battlefield = battlefieldIn,
        playerOne = playerOnePlayer.uid,
        playerTwo = playerTwoPlayer.uid,
        playerOneName = playerOnePlayer.name,
        playerTwoName = playerTwoPlayer.name,
        playerOneStack = playerOnePlayer.returnDeck(library),
        playerTwoStack = playerTwoPlayer.returnDeck(library),
        playerOneHand = mutableListOf<Card>(),
        playerTwoHand = mutableListOf<Card>(),
        playerOneBank = mutableListOf<Card>(),
        playerTwoBank = mutableListOf<Card>(),
        playerOneGraveyard = mutableListOf<Card>(),
        playerTwoGraveyard = mutableListOf<Card>(),
        playerOneLands = mutableListOf<Card>(),
        playerTwoLands = mutableListOf<Card>(),
        playerOneMaxHp = playerOnePlayer.hp,
        playerOneCurrentHp = playerOnePlayer.hp,
        playerTwoMaxHp = playerTwoPlayer.hp,
        playerTwoCurrentHp = playerTwoPlayer.hp,
        playerOneMaxBank = playerOnePlayer.maxBank,
        playerTwoMaxBank = playerTwoPlayer.maxBank,
        playerOneMaxLand = playerOnePlayer.maxLand,
        playerTwoMaxLand = playerTwoPlayer.maxLand,
        playerOneLevel = playerOnePlayer.level,
        playerTwoLevel = playerTwoPlayer.level,
    )

    //wenn true ist der derzeiteige Spieler playerOne, wenn false playerTwo
    val currentPlayer = true
    private lateinit var bnd: FragmentBattleBinding
    private lateinit var battleCallback: BattleCallback
    var battleStarted = false

    fun setUpBattleField(
        binding: FragmentBattleBinding,
    ): Int {
        bnd = binding
        battlefield.setBattlefield(bnd)
        if (battleStarted) {
            bnd.clBattleground.scaleX = 0.25f
            bnd.clBattleground.scaleY = 0.2f
            bnd.clBattleground.rotationX = 40f
        } else {
            val scaleXAnimator = ObjectAnimator.ofFloat(bnd.clBattleground, "scaleX", 0.25f)
            val scaleYAnimator = ObjectAnimator.ofFloat(bnd.clBattleground, "scaleY", 0.2f)
            val rotateAnimation = ObjectAnimator.ofFloat(bnd.clBattleground, "rotationX", 40f)
            val turnTable = ObjectAnimator.ofFloat(bnd.clBattleground, "rotation", 360f)

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleXAnimator, scaleYAnimator, rotateAnimation, turnTable)
            animatorSet.duration = 5000

            animatorSet.addListener(object : Animator.AnimatorListener {
                //man muss alle Methoden implementieren auch wenn sie nicht verwqendet werden.
                //Die EndMethode cleared die Animation auf dem Battleground und löst den MarqueeTextb aus
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {

                    bnd.clBattleground.clearAnimation()
                    bnd.txtGridRecyclerHeader.text = "Spielerhand"
                    bnd.txtGridRecyclerHeaderShadow.text = "Spielerhand"
                    bnd.marqueeText.isSelected = true
                    bnd.rvHorizontal.isVisible = true
                    bnd.gridSelection.isVisible = true
                    bnd.svLegend.isVisible = false
                    battleStarted = true
                    battleCallback.getPlayerOneHand()

                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })

            animatorSet.start()
        }
        return battlefield.music
    }
    fun getCard(stack: MutableList<Card>, hand: MutableList<Card>){
        hand.add(stack.removeFirst())
    }
    fun playerHandToStack(){
        playerOneStack.addAll(playerOneHand)
        playerOneHand.removeAll(playerOneHand)
        playerOneStack.shuffle()
    }
    fun setBattleCallback(callback: BattleCallback) {
        this.battleCallback = callback
    }
    fun sortCards(){
        if(playerOneHand.filter { it.selected&&it.cardType=="Hero" }.size + playerOneBank.size > playerOneMaxBank)
            battleCallback.throwToast("Du hast nicht genug Platz auf deiner Helden Bank")
        else if(playerOneHand.filter{it.selected&&it.cardType=="Land"&&it.type=="water"}.size+playerOneLands.filter { it.type=="water" }.size>playerOneMaxLand)
            battleCallback.throwToast("Du hast nicht genug Platz für Wasserresourcen")
        else if(playerOneHand.filter{it.selected&&it.cardType=="Land"&&it.type=="air"}.size+playerOneLands.filter { it.type=="air" }.size>playerOneMaxLand)
            battleCallback.throwToast("Du hast nicht genug Platz für Luftresourcen")
        else if(playerOneHand.filter{it.selected&&it.cardType=="Land"&&it.type=="fire"}.size+playerOneLands.filter { it.type=="fire" }.size>playerOneMaxLand)
            battleCallback.throwToast("Du hast nicht genug Platz für Feuerresourcen")
        else if(playerOneHand.filter{it.selected&&it.cardType=="Land"&&it.type=="plant"}.size+playerOneLands.filter { it.type=="plant" }.size>playerOneMaxLand)
            battleCallback.throwToast("Du hast nicht genug Platz für Naturresourcen")
        else{
            //braucht die Variable, weil sofortiges entfernen aus der Hand die forEach-Schleife zum Absturz bringt
            var listToRemove = mutableListOf<Card>()
            playerOneHand.filter { card -> card.selected }.forEach{
                if(it.cardType=="Hero"){
                    it.selected = false
                    playerOneBank.add(it)
                    listToRemove.add(it)
                }
                if(it.cardType=="Land"){
                    it.selected = false
                    playerOneLands.add(it)
                    listToRemove.add(it)
                }

            }
            playerOneHand.removeAll(listToRemove)
            playerOneLands = playerOneLands.sortedBy { it.type }.toMutableList()
            battleCallback.updateUI()
        }
    }

}
