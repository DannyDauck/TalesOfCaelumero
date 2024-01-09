package com.example.talesofcaelumora.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.datamodel.Player
import com.example.talesofcaelumora.databinding.PlayerItemBinding

class PlayerAdapter(var data: List<Player>, val context: Context, var type: String = "") :
    RecyclerView.Adapter<PlayerAdapter.PlayerItemHolder>() {

    inner class PlayerItemHolder(var bnd: PlayerItemBinding) : RecyclerView.ViewHolder(bnd.root)

    private lateinit var bnd: PlayerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerItemHolder {
        bnd = PlayerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerItemHolder(bnd)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PlayerItemHolder, position: Int) {
        val player = data[position]
        Glide.with(context)
            .load(player.homeArena.background)
            .placeholder(R.drawable.temple_of_waterbenders)
            .into(bnd.background)

        if(position%2==1){
            bnd.imgCharFlip.isVisible = true
            bnd.imgChar.isGone = true
            bnd.imgCharFlip.setImageResource(player.character)
            flipImage()
        }else {
            bnd.imgChar.setImageResource(player.character)
            bnd.imgChar.isVisible = true
        }

        bnd.txtPlayerName.setGradientColors(
            R.color.white,
            when (player.homeArena.navBar) {
                R.drawable.navbar_bg_water -> R.color.water_sec
                R.drawable.navbar_fire -> R.color.fire_first
                R.drawable.navbar_bg_air -> R.color.air_sec
                R.drawable.navbar_bg_nature -> R.color.plant_sec
                else -> R.color.smooth_gold
            }, R.color.white
        )



        bnd.txtPlayerName.text = player.name
        bnd.txtPlayerNameShadow.text = player.name
        bnd.txtPlayerTitle.text = player.currentTitle
        bnd.txtPlayerTitleShadow.text = player.currentTitle
        bnd.txtPlayerLevel.text = "Level ${player.level}"
        bnd.txtPlayerLevelShadow.text = "Level ${player.level}"

        if(type=="select all"){
            bnd.imgSelectLayer.isVisible = false
        }



    }

    private fun flipImage() {
        listOf(
            bnd.txtPlayerName,
            bnd.txtPlayerNameShadow,
            bnd.txtPlayerTitle,
            bnd.txtPlayerTitleShadow,
            bnd.txtPlayerLevel,
            bnd.txtPlayerLevelShadow
        ).forEach {
            it.translationX = bnd.imgCharFlip.width.toFloat()
            it.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        }
    }

    fun update(list: List<Player>){
        data = list
        notifyDataSetChanged()
    }
}