package com.example.talesofcaelumora.adapter

import android.content.Context
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
import java.io.File

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
            .load(File(context.filesDir, "images/${card.id}.jpeg"))
            .placeholder(R.drawable.card_describtion_bg)
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
                "air" -> {
                    if (card.rarity == "ultra rare") R.drawable.card_air_hero_ultra_rare
                    else if (card.rarity == "rare") R.drawable.card_air_hero_rare
                    else R.drawable.card_air_hero
                }

                "plant" -> {
                    if (card.rarity == "ultra rare") R.drawable.card_plant_hero_ultra_rare_bg
                    else if (card.rarity == "rare") R.drawable.card_nature_bg_rare
                    else R.drawable.card_plant_hero
                }

                "fire" -> {
                    if(card.rarity=="ultra rare")R.drawable.card_fire_bg_ultra_rare
                    else if(card.rarity=="rare")R.drawable.card_fire_bg_rare
                    else R.drawable.card_fire_hero
                }
                "water" -> {
                    if (card.rarity == "ultra rare") R.drawable.card_hero_water_ultra_rare_bg
                    else if (card.rarity == "rare") R.drawable.card_water_bg_rare
                    else R.drawable.card_water_hero
                }

                "supporter" -> {
                    if (card.rarity == "ultra rare") R.drawable.card_supporter_ultra_rare
                    else if (card.rarity == "rare") R.drawable.card_supporter_rare
                    else R.drawable.card_supporter
                }

                else -> R.drawable.card_air_hero
            }
        )
        if (card.cardType == "Hero") {

            //First Ability
            holder.bnd.tvFirstAbility.text = card.firstAbilityName
            holder.bnd.tvFirstAbilityPoints.text = card.firstAbilityPoints.toString()
            holder.bnd.tvFirstAbilityDescribtion.text = card.firstAbilityDescription
            when(card.firstAbilityType){
                "single damage" -> holder.bnd.tvFirstType.text ="SD"
                "multi damage" -> holder.bnd.tvFirstType.text ="MD"
                "single damage and heal" -> holder.bnd.tvFirstType.text ="SDH"
                "multi damage and heal" -> holder.bnd.tvFirstType.text ="MDH"
                "single heal" -> holder.bnd.tvFirstType.text = "SH"
                "multi heal" -> holder.bnd.tvFirstType.text = "MH"
                "single damage and protect" -> holder.bnd.tvFirstType.text =  "SDP"
                "multi damage and protect" -> holder.bnd.tvFirstType.text = "MDP"
                "single heal player" -> holder.bnd.tvFirstType.text = "PH"
            }
            when(card.secAbilityType){
                "single damage" -> holder.bnd.tvSecType.text ="SD"
                "multi damage" -> holder.bnd.tvSecType.text ="MD"
                "single damage and heal" -> holder.bnd.tvSecType.text ="SDH"
                "multi damage and heal" -> holder.bnd.tvSecType.text ="MDH"
                "single heal" -> holder.bnd.tvSecType.text = "SH"
                "multi heal" -> holder.bnd.tvSecType.text = "MH"
                "single damage and protect" -> holder.bnd.tvSecType.text =  "SDP"
                "multi damage and protect" -> holder.bnd.tvSecType.text = "MDP"
                "single heal player" -> holder.bnd.tvSecType.text = "PH"
            }
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
            holder.bnd.tvFirstType.isVisible = false
            holder.bnd.tvFirstAbility.text = card.firstAbilityName
            holder.bnd.tvFirstAbilityDescribtion.text = card.firstAbilityDescription
        } else if (card.cardType == "Supporter") {
            holder.bnd.llSecAbility.isGone = true
            holder.bnd.llFirstAbiiltyCosts.isGone = true
            holder.bnd.tvFirstAbilityPoints.isVisible = false
            holder.bnd.tvFirstType.isVisible = false
            holder.bnd.tvFirstAbility.text = card.firstAbilityName
            holder.bnd.tvFirstAbilityDescribtion.text = card.firstAbilityDescription + "" +
                    "\n" +
                    "\n" + card.secAbilityDescription
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