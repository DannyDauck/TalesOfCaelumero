package com.example.talesofcaelumora.data.datamodel

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.room.TypeConverters
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.local.Converters
import com.example.talesofcaelumora.databinding.FragmentBattleBinding
import java.io.File

@TypeConverters(Converters::class)
class Battlefield(
    val name: String,
    val background: String,
    val battleground: Int,
    val navBar: Int,
    val music: Int,
    val song: String
) {

    constructor(map: Map<String, Any>) : this(
        name = map["name"] as String,
        background = map["background"] as String,
        battleground = (map["battleground"] as Long).toInt(),
        navBar = (map["navBar"] as Long).toInt(),
        music = (map["music"] as Long).toInt(),
        song = map["song"] as String
    )


    fun setBattlefield(context: Context,bnd: FragmentBattleBinding){
        bnd.clBattleground.setBackgroundResource(battleground)
        val path = File(
            context.filesDir,
            "images/${background}.jpeg"
        ).absolutePath
        val drawable = Drawable.createFromPath(path)
        bnd.root.background = drawable
        bnd.navbar.setBackgroundResource(navBar)
        bnd.marqueeText.text = "battlefield: "+name+" song: "+song+" "
        bnd.marqueeText.isSelected = true
    }
}

val battlefields = listOf(
    Battlefield("Silverforest", "CdoYqWZZ9d93iIUkwU9h", R.drawable.battleground_one, R.drawable.navbar_bg_nature, R.raw.forest_battle, "Secret Of The Silverforest - Danny D. "),
    Battlefield("Temple Of Water", "temple_of_the_waterbenders", R.drawable.battleground_two, R.drawable.navbar_bg_water, R.raw.watertheme, "The Spirit Of Water - DarT music "),
    Battlefield("Ignisaria", "land_fire_desert_w4yVJjEWCtuMhNu6JVTs", R.drawable.battleground_three, R.drawable.navbar_fire, R.raw.fire_theme, "Fire Is Burning Inside Me - Danny D. "),
    Battlefield("Caelumero", "land_air_city_WjW0scCoqxkirUNr8BVb", R.drawable.battleground_two, R.drawable.navbar_bg_air, R.raw.air_theme, "Whisper Of The Wind - Danny D. ")
)