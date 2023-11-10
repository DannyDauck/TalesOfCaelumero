package com.example.talesofcaelumora.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.talesofcaelumora.data.datamodel.Card
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.CardItemBinding
import com.google.firebase.storage.FirebaseStorage

class CardAdapter(
    private var data: List<Card>,
    private var type: String = "",
    private val context: Context
) :
    RecyclerView.Adapter<CardAdapter.ListHolder>() {

    inner class ListHolder(val bnd: CardItemBinding) : RecyclerView.ViewHolder(bnd.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {

        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHolder(binding)

    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val card = data[position]
        holder.bnd.tvCardname.text = card.cardName
        Glide.with(context)
            .load("https://firebasestorage.googleapis.com/v0/b/tales-of-caelumero.appspot.com/o/card%2Fimages%2Fcity.jpeg?alt=media&token=76c9d25a-0152-4012-8bd5-f907ac8702de&_gl=1*1fcv8wa*_ga*NjczMjA1OTkxLjE2OTkyNTU5Nzk.*_ga_CW55HF8NVT*MTY5OTUzODY0OS4yMC4xLjE2OTk1Mzk4MDMuNjAuMC4w")
            .placeholder(R.drawable.elara)
            .into(holder.bnd.imgCard)
        holder.bnd.tvHp.text = when (card.cardType) {
            "Hero" -> card.hp.toString() + "HP"
            "Land" -> "Land"
            "Supporter" -> "S"
            else -> ""
        }
        holder.bnd.imgCardType.setImageResource(getChip(card.type))

        //Set card background
        holder.bnd.card.setBackgroundResource(
            when (card.type) {
                "air" -> R.drawable.card_air_hero
                "plant" -> R.drawable.card_plant_hero
                "fire" -> R.drawable.card_fire_hero
                "water" -> R.drawable.card_water_hero
                "supporter" -> R.drawable.card_supporter
                else -> R.drawable.card_air_hero
            }
        )
        if (card.cardType == "Hero") {

            //First Ability
            holder.bnd.tvFirstAbility.text = card.firstAbilityName
            holder.bnd.tvFirstAbilityPoints.text = card.firstAbilityPoints.toString()
            holder.bnd.tvFirstAbilityDescribtion.text = card.firstAbilityDescription
            getCosts(
                listOf(
                    holder.bnd.imgFirstCostOne,
                    holder.bnd.imgFirstCostTwo,
                    holder.bnd.imgFirstCostThree,
                    holder.bnd.imgFirstCostFour,
                    holder.bnd.imgFirstCostFive,
                    holder.bnd.imgFirstCostSix,
                ), card.firstAbilityCosts
            )


            //Second Ability
            holder.bnd.tvSecAbilityName.text = card.secAbilityName
            holder.bnd.tvSecAbilityPoints.text = card.secAbilityPoints.toString()
            holder.bnd.tvSecAbilityDescription.text = card.secAbilityDescription
            getCosts(
                listOf(
                    holder.bnd.imgSecCostOne,
                    holder.bnd.imgSecCostTwo,
                    holder.bnd.imgSecCostThree,
                    holder.bnd.imgSecCostFour,
                    holder.bnd.imgSecCostFive,
                    holder.bnd.imgSecCostSix,
                ), card.secAbilityCosts
            )
        } else if (card.cardType == "Land") {
            holder.bnd.llSecAbility.isGone = true
            holder.bnd.llFirstAbiiltyCosts.isGone = true
            holder.bnd.tvFirstAbilityPoints.isVisible = false
            holder.bnd.tvFirstAbility.text = card.firstAbilityName
            holder.bnd.tvFirstAbilityDescribtion.text = card.firstAbilityDescription
        } else if (card.cardType == "Supporter") {
            holder.bnd.llSecAbility.isGone = true
            holder.bnd.llFirstAbiiltyCosts.isGone = true
            holder.bnd.tvFirstAbilityPoints.isVisible = false
            holder.bnd.tvFirstAbility.text = card.firstAbilityName
            holder.bnd.tvFirstAbilityDescribtion.text = card.firstAbilityDescription
        }
        if (type == "selection") {
            holder.bnd.root.setOnClickListener {
                card.selected = !card.selected
                if (card.selected) it.setBackgroundResource(R.drawable.card_stroke_selected) else it.setBackgroundResource(
                    R.drawable.card_stroke_disselected
                )
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun update(list: List<Card>) {
        data = list
        notifyDataSetChanged()

    }

    fun getChip(type: String): Int {
        return when (type) {
            "air" -> R.drawable.airchip
            "plant" -> R.drawable.plantchip
            "water" -> R.drawable.waterchip
            "fire" -> R.drawable.firechip
            "supporter" -> R.drawable.supporter_chip
            else -> R.drawable.colorlesschip
        }
    }

    fun getCosts(list: List<ImageView>, costs: List<String>) {


        //Sets first all invisible and then back again from the beginning costs size long to visible and get the right type chip

        list.forEach { it.isVisible = false }
        repeat(costs.size) {
            list[it].isVisible = true
            list[it].setImageResource(getChip(costs[it]))
        }
    }

    fun update(cards: List<Card>, mode: String) {
        data = cards
        type = mode
        notifyDataSetChanged()
    }
}