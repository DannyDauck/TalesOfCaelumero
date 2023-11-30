package com.example.talesofcaelumora.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.CharacterMiniItemBinding

class CircularMiniCharacterAdapter(
    var data: List<Int>,
    var indi: Int
) : RecyclerView.Adapter<CircularMiniCharacterAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(var bnd: CharacterMiniItemBinding) :
        RecyclerView.ViewHolder(bnd.root)
    //private var indicator = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        var bnd =
            CharacterMiniItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(bnd)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bnd.imgCharacterMini.setImageResource(data[position])

        if (data[position]==indi) {
            holder.bnd.root.scaleX = 1.3f
            holder.bnd.root.scaleY = 1.3f
        } else {
            holder.bnd.root.scaleX = 1.0f
            holder.bnd.root.scaleY = 1.0f
        }
    }
    fun tripleData(){
        var list = data.toMutableList()
        list.addAll(data)
        list.addAll(data)
        data = list.toList()
        notifyDataSetChanged()
    }
    fun setIndicator(index: Int){
        indi = if(index==0)data[0]else index
        data = data
        notifyDataSetChanged()
    }

    /* Das funktioniert aufjedenfall
    fun circleDown() {
        var list = mutableListOf(data.last())
        list.addAll(data)
        list.removeLast()
        data = list.toList()
        notifyDataSetChanged()
    }

    fun circle() {
        var list = data.toMutableList()
        list.add(data.first())
        list.removeFirst()
        data = list.toList()
        notifyDataSetChanged()
    }
     */
}