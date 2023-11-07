package com.example.talesofcaelumora.data.datamodel

import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.FragmentBattleBinding

class Battlefield(
    val name: String,
    val background: Int,
    val battleground: Int,
    val navBar: Int,
    val music: Int,
    val song: String
) {
    fun setBattlefield(bnd: FragmentBattleBinding){
        bnd.clBattleground.setBackgroundResource(battleground)
        bnd.root.setBackgroundResource(background)
        bnd.navbar.setBackgroundResource(navBar)
        bnd.marqueeText.text = "battlefield: "+name+" song: "+song+" "
        bnd.marqueeText.isSelected = true
    }
}

val battlefields = listOf(
    Battlefield("Silverforest", R.drawable.land_forest_two, R.drawable.battleground_one, R.drawable.navbar_bg_nature, R.raw.forest_battle, "Secret Of The Silverforest - Danny D. "),
    Battlefield("Temple Of Water", R.drawable.temple_of_waterbenders, R.drawable.battleground_two, R.drawable.navbar_bg_water, R.raw.watertheme, "The Spirit Of Water - DarT music ")
)