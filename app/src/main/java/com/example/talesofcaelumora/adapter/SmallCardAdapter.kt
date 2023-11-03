package com.example.talesofcaelumora.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.talesofcaelumora.data.Card
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.CardItemBinding

class SmallCardAdapter(
    private var data: List<Card>,
) :
    RecyclerView.Adapter<SmallCardAdapter.ListHolder>() {

    inner class ListHolder(val bnd: CardItemBinding) : RecyclerView.ViewHolder(bnd.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHolder(binding)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val card = data[position]
        holder.bnd.tvCardname.text = card.cardName
        holder.bnd.imgCard.setImageResource(card.imgSrc)
        holder.bnd.tvHp.text = when (card.cardType) {
            "Hero" -> card.hp.toString() + "HP"
            "Land" -> "Land"
            else -> ""
        }
        holder.bnd.imgCardType.setImageResource(getChip(card.type))

        //Set card background
        holder.bnd.root.setBackgroundResource(
            when (card.type) {
                "air" -> R.drawable.card_air_hero
                "plant" -> R.drawable.card_plant_hero
                "fire" -> R.drawable.card_fire_hero
                "water" -> R.drawable.card_water_hero
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
        }
        else if (card.cardType=="Land"){
            holder.bnd.llSecAbility.isGone = true
            holder.bnd.llFirstAbiiltyCosts.isGone = true
            holder.bnd.tvFirstAbilityPoints.isVisible = false
            holder.bnd.tvFirstAbility.text = card.firstAbilityName
            holder.bnd.tvFirstAbilityDescribtion.text = card.firstAbilityDescription
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
}