package com.example.talesofcaelumora.data.utils

import com.example.talesofcaelumora.data.datamodel.Card

interface BattleCallback {
    fun getPlayerOneHand()
    fun updateUI()
    fun throwToast(string: String)
    fun updateLifebars()
    fun doActionReset()
    fun startRecyclerAnimation(targets: List<Card>, type: Int?)


}

