package com.example.talesofcaelumora.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.adapter.PlayerAdapter
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.databinding.FragmentProfilBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel

class ProfilFragment : Fragment() {

    private lateinit var bnd: FragmentProfilBinding
    private lateinit var soundManager: SoundManager
    private val vm :  MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        soundManager = SoundManager.getInstance(requireContext())
        bnd = FragmentProfilBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adapter = PlayerAdapter(listOf(), requireContext(), "")
        bnd.rvPlayer.adapter = adapter
        vm.player.observe(viewLifecycleOwner){
            adapter.update(listOf(it))
        }

        bnd.btnBackBackFromProfil.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}