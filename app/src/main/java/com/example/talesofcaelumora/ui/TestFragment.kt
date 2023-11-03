package com.example.talesofcaelumora.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.SmallCardAdapter
import com.example.talesofcaelumora.data.heroDeck
import com.example.talesofcaelumora.databinding.FragmentTestBinding

class TestFragment : Fragment() {
    private lateinit var bnd: FragmentTestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentTestBinding.inflate(inflater, container, false)
        return bnd.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        var opponenAdapter = SmallCardAdapter(heroDeck.shuffled())
        bnd.rcvHeroTableOpponent.adapter = opponenAdapter
    }
}