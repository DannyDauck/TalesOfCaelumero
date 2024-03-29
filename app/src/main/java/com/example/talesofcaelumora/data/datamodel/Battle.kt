package com.example.talesofcaelumora.data.datamodel

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import androidx.core.view.isVisible
import com.example.talesofcaelumora.data.utils.BattleCallback
import com.example.talesofcaelumora.databinding.FragmentBattleBinding

class Battle(
    var id: String,
    var battlefield: Battlefield,
    var lastMove: String,
    var playerOne: String,
    var playerTwo: String,
    var playerOneCharacter: Int,
    var playerTwoCharacter: Int,
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
    var playerOneMaxHp: Int,
    var playerOneCurrentHp: Int,
    var playerTwoMaxHp: Int,
    var playerTwoCurrentHp: Int,
    var playerOneMaxBank: Int,
    var playerTwoMaxBank: Int,
    var playerOneMaxLand: Int,
    var playerTwoMaxLand: Int,
    var playerOneLevel: Int,
    var playerTwoLevel: Int,
    val context: Context,
    var round: Int = 0

    ) {

    constructor(map: Map<String, Any>, context: Context): this(
        id = map["id"] as String,
        battlefield = Battlefield(map["battlefield"] as Map<String, Any>),
        lastMove = map["lastMove"] as String,
        playerOne = map["playerOne"] as String,
        playerTwo = map["playerTwo"] as String,
        playerOneCharacter = (map["playerOneCharacter"] as Long).toInt(),
        playerTwoCharacter = (map["playerTwoCharacter"] as Long).toInt(),
        playerOneName = map["playerOneName"] as String,
        playerTwoName = map["playerTwoName"] as String,
        playerOneStack = toCardStack(map["playerOneStack"] as List<Map<String,Any>>),
        playerTwoStack = toCardStack( map["playerTwoStack"] as List<Map<String, Any>>),
        playerOneHand = toCardStack( map["playerOneHand"] as List<Map<String,Any>>),
        playerTwoHand = toCardStack( map["playerTwoHand"] as List<Map<String,Any>>),
        playerOneBank = toCardStack( map["playerOneBank"] as List<Map<String,Any>>),
        playerTwoBank = toCardStack( map["playerTwoBank"] as List<Map<String,Any>>),
        playerOneGraveyard = toCardStack( map["playerOneGraveyard"] as List<Map<String,Any>>),
        playerTwoGraveyard = toCardStack( map["playerTwoGraveyard"] as List<Map<String,Any>>),
        playerOneLands = toCardStack( map["playerOneLands"] as List<Map<String,Any>>),
        playerTwoLands = toCardStack( map["playerTwoLands"] as List<Map<String,Any>>),
        playerOneMaxHp = (map["playerOneMaxHp"] as Long).toInt(),
        playerOneCurrentHp = (map["playerOneCurrentHp"] as Long).toInt(),
        playerTwoMaxHp = (map["playerTwoMaxHp"] as Long).toInt(),
        playerTwoCurrentHp = (map["playerTwoCurrentHp"] as Long).toInt(),
        playerOneMaxBank = (map["playerOneMaxBank"] as Long).toInt(),
        playerTwoMaxBank = (map["playerTwoMaxBank"] as Long).toInt(),
        playerOneMaxLand = (map["playerOneMaxLand"] as Long).toInt(),
        playerTwoMaxLand = (map["playerTwoMaxLand"] as Long).toInt(),
        playerOneLevel = (map["playerOneLevel"] as Long).toInt(),
        playerTwoLevel = (map["playerTwoLevel"] as Long).toInt(),
        round = (map["round"] as Long).toInt(),
        context = context
    )

    constructor(
        playerOnePlayer: Player,
        playerTwoPlayer: Player,
        battlefieldIn: Battlefield,
        time: String,
        context: Context,
        library: List<Card>
    ) : this(
        id = playerOnePlayer.uid + "_vs_" + playerTwoPlayer.uid,
        battlefield = battlefieldIn,
        lastMove = time,
        playerOne = playerOnePlayer.uid,
        playerTwo = playerTwoPlayer.uid,
        playerOneCharacter =playerOnePlayer.character!!,
        playerTwoCharacter =playerTwoPlayer.character!!,
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
        context = context
    )

    //wenn true ist der derzeiteige Spieler playerOne, wenn false playerTwo

    private lateinit var bnd: FragmentBattleBinding
    private lateinit var battleCallback: BattleCallback
    var battleStarted = round>1

    fun setUpBattleField(
        binding: FragmentBattleBinding,
    ): Int {
        bnd = binding
        battlefield.setBattlefield(context, bnd)
        if (battleStarted) {
            bnd.clBattleground.scaleX = 0.25f
            bnd.clBattleground.scaleY = 0.2f
            bnd.clBattleground.rotationX = 40f
            bnd.clBattleground.rotation = 0f
            bnd.svLegend.isVisible = false
            bnd.gridSelection.isVisible = false
            bnd.bigCard.isVisible = false
            battleCallback.updateLifebars()
            battleCallback.updateUI()
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
                    bnd.rvHorizontal.isVisible = true
                    bnd.gridSelection.isVisible = true
                    bnd.svLegend.isVisible = false
                    battleStarted = true
                    bnd.clBattleground.rotation = 0f
                    battleCallback.updateLifebars()
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
        hand.add(stack.removeFirst().toCard())
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
                    playerOneBank.add(it.toCard())
                    listToRemove.add(it)
                }
                if(it.cardType=="Land"){
                    it.selected = false
                    playerOneLands.add(it.toCard())
                    listToRemove.add(it)
                }

            }
            playerOneHand.removeAll(listToRemove)
            playerOneLands = playerOneLands.sortedBy { it.type }.toMutableList()
            battleCallback.updateUI()
        }
    }
    fun startAbility(target: List<Card>, actionCard: Card, type: Int){
        //flips the abilities if the second ability is used
        val flipCard = if(type==1)actionCard else actionCard.flipAbility()
        if(target.isEmpty()){
            if(round%2==1)playerOneCurrentHp -= minOf(flipCard.firstAbilityPoints, playerOneCurrentHp)
            else playerTwoCurrentHp -= minOf(flipCard.firstAbilityPoints, playerTwoCurrentHp)
            battleCallback.updateLifebars()
            battleCallback.doActionReset()

        }else{
            var heal = 0

            target.forEach { card ->
                var rest = 0
                //checken ob der Angriff mehr Schaden zufügt als die GegnerKarte hat oder die Gegnerkarte protected ist
                if(!card.protected){
                    if(card.currentHp<getDamage(card, actionCard,flipCard.firstAbilityPoints)) rest += getDamage(card, actionCard,flipCard.firstAbilityPoints)-card.currentHp
                    card.currentHp -= minOf(getDamage(card, actionCard,flipCard.firstAbilityPoints), card.currentHp)
                    if(rest!=0){
                        playerTwoCurrentHp -= minOf(rest, playerTwoCurrentHp)
                        battleCallback.updateLifebars()
                    }
                    if(flipCard.firstAbilityType.contains("heal"))heal += getDamage(card, actionCard,flipCard.firstAbilityPoints)/2
                }else card.protected = false
            }
            battleCallback.startRecyclerAnimation(target, type)
            if(flipCard.firstAbilityType.contains("protect"))  actionCard.protected = true
            actionCard.used = true
            if(heal!=0 && flipCard.firstAbilityType == "single damage and heal")actionCard.currentHp += minOf(heal, playerOneMaxHp-playerOneCurrentHp)
            battleCallback.doActionReset()
        }
    }
    fun getDamage(target: Card, actionCard: Card, points: Int): Int{
        //List die Klassevor- und nachteile aus
        var damage = points
        val damageIncrease = points/20
        when(actionCard.type){
            "fire" -> when(target.type){
                "air" -> damage += damageIncrease
                "water" -> damage -= damageIncrease
            }
            "water" -> when(target.type){
                "fire" -> damage += damageIncrease
                "plant" -> damage -= damageIncrease
            }
            "plant" -> when(target.type){
                "water" -> damage += damageIncrease
                "air" -> damage -= damageIncrease
            }
            "air" -> when(target.type){
                "plant" -> damage += damageIncrease
                "fire" -> damage -= damageIncrease
            }
        }
        return damage
    }
}
fun Battle.battleToMap(): Map<String,Any>{
    return mapOf(
        "id" to id,
        "battlefield" to battlefield,
        "lastMove" to lastMove,
        "playerOne" to playerOne,
        "playerTwo" to playerTwo,
        "playerOneCharacter" to playerOneCharacter,
        "playerTwoCharacter" to playerTwoCharacter,
        "playerOneName" to playerOneName,
        "playerTwoName" to playerTwoName,
        "playerOneStack" to playerOneStack,
        "playerTwoStack" to playerTwoStack,
        "playerOneHand" to playerOneHand,
        "playerTwoHand" to playerTwoHand,
        "playerOneBank" to playerOneBank,
        "playerTwoBank" to playerTwoBank,
        "playerOneGraveyard" to playerOneGraveyard,
        "playerTwoGraveyard" to playerTwoGraveyard,
        "playerOneLands" to playerOneLands,
        "playerTwoLands" to playerTwoLands,
        "playerOneMaxHp" to playerOneMaxHp,
        "playerOneCurrentHp" to playerOneCurrentHp,
        "playerTwoMaxHp" to playerTwoMaxHp,
        "playerTwoCurrentHp" to playerTwoCurrentHp,
        "playerOneMaxBank" to playerOneMaxBank,
        "playerTwoMaxBank" to playerTwoMaxBank,
        "playerOneMaxLand" to playerOneMaxLand,
        "playerTwoMaxLand" to playerTwoMaxLand,
        "playerOneLevel" to playerOneLevel,
        "playerTwoLevel" to playerTwoLevel,
        "round" to round
    )


}

fun toCardStack(list: List<Map<String, Any>>): MutableList<Card>{
    var mutableList = mutableListOf<Card>()
    if(list.isNotEmpty())list.forEach {
        mutableList.add(Card(it, true))
    }
    return mutableList
}

fun Battle.flipBattle() {
    // Werte zwischenspeichern
    val tempPlayerOne = this.playerOne
    val tempPlayerOneCharacter = this.playerOneCharacter
    val tempPlayerOneName = this.playerOneName
    val tempPlayerOneStack = this.playerOneStack
    val tempPlayerOneHand = this.playerOneHand
    val tempPlayerOneBank = this.playerOneBank
    val tempPlayerOneGraveyard = this.playerOneGraveyard
    val tempPlayerOneLands = this.playerOneLands
    val tempPlayerOneMaxHp = this.playerOneMaxHp
    val tempPlayerOneCurrentHp = this.playerOneCurrentHp
    val tempPlayerOneMaxBank = this.playerOneMaxBank
    val tempPlayerOneMaxLand = this.playerOneMaxLand
    val tempPlayerOneLevel = this.playerOneLevel

    // Werte austauschen
    this.playerOne = this.playerTwo
    this.playerOneCharacter = this.playerTwoCharacter
    this.playerOneName = this.playerTwoName
    this.playerOneStack = this.playerTwoStack
    this.playerOneHand = this.playerTwoHand
    this.playerOneBank = this.playerTwoBank
    this.playerOneGraveyard = this.playerTwoGraveyard
    this.playerOneLands = this.playerTwoLands
    this.playerOneMaxHp = this.playerTwoMaxHp
    this.playerOneCurrentHp = this.playerTwoCurrentHp
    this.playerOneMaxBank = this.playerTwoMaxBank
    this.playerOneMaxLand = this.playerTwoMaxLand
    this.playerOneLevel = this.playerTwoLevel

    // Setze die Werte von playerTwo auf die temporär gespeicherten Werte von playerOne
    this.playerTwo = tempPlayerOne
    this.playerTwoCharacter = tempPlayerOneCharacter
    this.playerTwoName = tempPlayerOneName
    this.playerTwoStack = tempPlayerOneStack
    this.playerTwoHand = tempPlayerOneHand
    this.playerTwoBank = tempPlayerOneBank
    this.playerTwoGraveyard = tempPlayerOneGraveyard
    this.playerTwoLands = tempPlayerOneLands
    this.playerTwoMaxHp = tempPlayerOneMaxHp
    this.playerTwoCurrentHp = tempPlayerOneCurrentHp
    this.playerTwoMaxBank = tempPlayerOneMaxBank
    this.playerTwoMaxLand = tempPlayerOneMaxLand
    this.playerTwoLevel = tempPlayerOneLevel

    round++
    battleStarted = true
}


