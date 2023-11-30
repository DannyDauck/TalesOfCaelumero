package com.example.talesofcaelumora.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.FragmentBattleListBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel


class BattleListFragment : Fragment() {

    private lateinit var bnd: FragmentBattleListBinding
    private val vm : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        bnd = FragmentBattleListBinding.inflate(inflater, container,false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}