package com.example.talesofcaelumora.adapter

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.datamodel.Battle
import com.example.talesofcaelumora.data.datamodel.Card
import com.example.talesofcaelumora.databinding.BattleItemBinding
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BattleAdapter(
    private var data: List<Battle>,
    private var playerUID: String,
    private val context: Context,
    private val initTime: LocalDateTime,
) :
    RecyclerView.Adapter<BattleAdapter.BattleViewHolder>() {

    inner class BattleViewHolder(var bnd: BattleItemBinding) : RecyclerView.ViewHolder(bnd.root)

    private lateinit var bnd: BattleItemBinding


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BattleAdapter.BattleViewHolder {
        bnd = BattleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BattleViewHolder(bnd)
    }

    override fun onBindViewHolder(holder: BattleViewHolder, position: Int) {
        val battle = data[position]
        bnd.imgBattleField.setImageResource(battle.battlefield.background)
        bnd.imgCharacterPlayerOne.setImageResource(battle.playerOneCharacter)
        bnd.imgCharacterPlayerTwo.setImageResource(battle.playerTwoCharacter)
        bnd.playerOneName.text = battle.playerOneName
        bnd.playerTwoName.text = battle.playerTwoName
        if (playerUID==battle.playerOne){
            holder.bnd.playerOneName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.smooth_gold))
            holder.bnd.playerTwoName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.bnd.activeBg.isVisible = true
            var animation = RotateAnimation(0f,360f, 0.5f,0.5f)
            animation.repeatCount = Animation.INFINITE
            holder.bnd.activeBg.startAnimation(animation)
        }else{
            holder.bnd.playerOneName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.smooth_gold))
            holder.bnd.playerTwoName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.bnd.activeBg.isVisible = false
        }
        if(battle.playerOneCurrentHp==0||battle.playerTwoCurrentHp==0){
            if(battle.playerOne==playerUID){
                battle.playerOneCurrentHp = 0
                bnd.txtWinOrLoose.text = "loose"
                bnd.battleFinished.isVisible = true
            }else{
                battle.playerTwoCurrentHp = 0
                bnd.txtWinOrLoose.text = "won"
                bnd.battleFinished.isVisible = true
            }
        }else bnd.battleFinished.isVisible = false


        var battleTimeStamp = parseDateTime(battle.lastMove)

        if(battle.playerOneCurrentHp!=0&&battle.playerTwoCurrentHp!=0){

            var countdownInMillis = Duration.between(initTime, battleTimeStamp).toMillis().toLong()

            val countDownTimer = object : CountDownTimer(countdownInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val hours = millisUntilFinished / (1000 * 60 * 60)
                    val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
                    val seconds = (millisUntilFinished % (1000 * 60)) / 1000

                    val countdownText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    holder.bnd.txtBattleCounter.text = countdownText
                }

                override fun onFinish() {
                    holder.bnd.txtBattleCounter.text = "Battle abgelaufen"
                    if(battle.playerOne==playerUID){
                        battle.playerOneCurrentHp = 0
                        bnd.txtWinOrLoose.text = "loose"
                        bnd.battleFinished.isVisible = true
                    }else{
                        battle.playerTwoCurrentHp = 0
                        bnd.txtWinOrLoose.text = "won"
                        bnd.battleFinished.isVisible = true
                    }
                }
            }
            countDownTimer.start()
        }
        holder.bnd.root.setOnClickListener{
            //holder.itemView.findNavController().navigate()
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun parseDateTime(dateTimeString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.parse(dateTimeString, formatter)
    }

}