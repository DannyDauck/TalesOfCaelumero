package com.example.talesofcaelumora.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.FragmentTestBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel

//TODO löschen nach Präsentation
class TestFragment : Fragment() {
    private lateinit var bnd: FragmentTestBinding
    private val vm: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentTestBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        vm.dateTime.observe(viewLifecycleOwner){
            bnd.txtTimeApi.text = it.datetime.toString()
        }
        vm.getDateTime()
        bnd.btnUpdateTime.setOnClickListener {
            vm.getDateTime()
        }
        bnd.btnBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }
}