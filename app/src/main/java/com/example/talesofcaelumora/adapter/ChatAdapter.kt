package com.example.talesofcaelumora.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.datamodel.ChatItem
import com.example.talesofcaelumora.databinding.ChatItemBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatAdapter(var data: List<ChatItem>, var time: LocalDateTime, val context: Context, val uid: String): RecyclerView.Adapter<ChatAdapter.ChatItemHolder>(){

    inner class ChatItemHolder(var bnd: ChatItemBinding): RecyclerView.ViewHolder(bnd.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemHolder {
        var bnd = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatItemHolder(bnd)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ChatItemHolder, position: Int) {
        val message = data[position]
        val formatterTime = DateTimeFormatter.ofPattern("H:mm")
        if(time.isBefore(message.time.plusMinutes(4)))holder.bnd.txtTime.isVisible = false
        if(uid==message.uid){
            holder.bnd.messageBg.setBackgroundResource(R.drawable.chat_bg_you)
            holder.bnd.imgOwnCharacter.isVisible = true
            holder.bnd.imgPlayerCharacterChat.isGone = true
            holder.bnd.userName.text = context.getString(R.string.you)
            holder.bnd.gravity.gravity = Gravity.END
            holder.bnd.imgOwnCharacter.setImageResource(message.character)
        }else {
            holder.bnd.imgOwnCharacter.isGone = true
            holder.bnd.imgPlayerCharacterChat.isVisible = true
            holder.bnd.imgPlayerCharacterChat.setImageResource(message.character)
            holder.bnd.messageBg.setBackgroundResource(R.drawable.chat_bg)
            holder.bnd.userName.text = message.name
            holder.bnd.gravity.gravity = Gravity.START
        }
        holder.bnd.message.text = message.message
        holder.bnd.txtTimestampTime.text = message.time.format(formatterTime)

        if(position!=0){
            holder.bnd.txtTime.isGone = getDay(message.time)==getDay(data[position-1].time)
            holder.bnd.txtTimestampTime.isGone = data[position-1].time.plusMinutes(2).isAfter(message.time)
        }else {
            holder.bnd.txtTimestampDay.text = getDay(message.time)
            holder.bnd.txtTime.isVisible = true
        }

    }

    fun getDay(dateTime: LocalDateTime): String{
        if(time.isBefore(dateTime.minusDays(1))){
            if(time.isBefore(dateTime.minusDays(2))) return dateTime.dayOfWeek.name + ", " + dateTime.dayOfMonth + "." + dateTime.month.name + " " + dateTime.year
            else return context.getString(R.string.yesterday)
        }else return context.getString(R.string.today)
    }
    fun update(list: List<ChatItem>, newTime: LocalDateTime){
        time = newTime
        data = list
        notifyDataSetChanged()
    }
}