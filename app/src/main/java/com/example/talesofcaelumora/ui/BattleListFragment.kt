package com.example.talesofcaelumora.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.adapter.BattleAdapter
import com.example.talesofcaelumora.data.datamodel.Battle
import com.example.talesofcaelumora.data.datamodel.Player
import com.example.talesofcaelumora.data.datamodel.examplePlayerDanny
import com.example.talesofcaelumora.data.datamodel.examplePlayerElara
import com.example.talesofcaelumora.databinding.FragmentBattleListBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BattleListFragment : Fragment() {

    private lateinit var bnd: FragmentBattleListBinding
    private val vm: MainViewModel by activityViewModels()
    private var adapter: BattleAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        bnd = FragmentBattleListBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //muss im GlobalScope gelauncht werden weil sonst eine NetWorkOnMainThreadException auftritt
        GlobalScope.launch(Dispatchers.Main) {
            adapter = BattleAdapter(vm.battles.value!!, vm.player.value!!.uid, requireContext(), vm.getNetworkTime().minusDays(1))
            bnd.rvBattles.adapter = adapter
        }


        vm.battles.observe(viewLifecycleOwner) {
            adapter?.update(it.sortedBy { battle -> battle.playerOne==vm.player.value!!.uid })
        }
        bnd.btnAdd.setOnClickListener {

        }
        bnd.btnAddMulti.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val opponent = listOf<Player>(examplePlayerDanny, examplePlayerElara).random()
                val battle = Battle(vm.player.value!!, opponent, opponent.homeArena, vm.getNetworkTime().toString(), requireContext(), vm.cardLibrary.value!!)
                vm.pushBattle(battle, requireContext())
            }
        }
        bnd.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        bnd.btnMultibattleAsOpponent.setOnClickListener {
            lifecycleScope.launch{
                vm.getMultiBattle(requireContext())
                delay(1000)
                if(vm.currentBattle.value!=null){
                    findNavController().navigate(BattleListFragmentDirections.actionBattleListFragmentToBattleFragment("multibattle"))
                }else Toast.makeText(requireContext(), "No battles available", Toast.LENGTH_SHORT).show()
            }
        }
    }

}