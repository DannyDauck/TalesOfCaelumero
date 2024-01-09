package com.example.talesofcaelumora.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.adapter.ChatAdapter
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.databinding.FragmentGlobalChatBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalTime


class GlobalChatFragment : Fragment() {

    private lateinit var bnd: FragmentGlobalChatBinding
    private val vm: MainViewModel by activityViewModels()
    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        soundManager = SoundManager.getInstance(requireContext())
        bnd = FragmentGlobalChatBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val night = (LocalTime.now().hour < 8 || LocalTime.now().hour > 19)
        if (night) startTurning(bnd.imgBgNigth) else {
            startTurning(bnd.imgBg)
            bnd.imgBgNigth.isVisible = false
            bnd.imgBgNigthForeground.isVisible = false
        }
        var adapter = ChatAdapter(listOf(), LocalDateTime.now(), requireContext(), vm.player.value!!.uid)

        bnd.rvChat.adapter = adapter
        bnd.rvChat.smoothScrollToPosition(adapter.itemCount)



        bnd.btnSend.setOnClickListener {
            if(bnd.itMessage.text.toString()!=""&&bnd.itMessage.text!=null){
                soundManager.playSound(R.raw.button_click)
                vm.sendChatMessageToGlobalChat(bnd.itMessage.text.toString())
                bnd.itMessage.setText("")
            }
        }
        vm.chatMessages.observe(viewLifecycleOwner){
            lifecycleScope.launch(Dispatchers.IO) {
                val time = vm.getNetworkTime()
                withContext(Dispatchers.Main){
                    adapter.update(it, time)
                    if(bottomCheck()&&it.isNotEmpty())bnd.rvChat.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }
        }
        bnd.btnBackFromglobalChat.setOnClickListener {
            soundManager.playSound(R.raw.button_click)
            vm.setLastChatSize()
            findNavController().popBackStack()
        }
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(bnd.itMessage, InputMethodManager.SHOW_IMPLICIT).apply{
            //schließt die Tastatur bei Klick auf Enter wieder
            bnd.itMessage.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
                ) {
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(bnd.itMessage.windowToken, 0)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        }
    }

    fun startTurning(view: View) {
        val translation = TranslateAnimation(0f, 200f, 0f, 0f)
        translation.duration = 10000

        lifecycleScope.launch {
            view.startAnimation(translation)
            delay(10000)
            view.translationX += 200f

            turnBack(view)

        }


    }

    private fun turnBack(view: View) {
        val translation = TranslateAnimation(0f, -200f, 0f, 0f)
        translation.duration = 10000

        lifecycleScope.launch {

            view.startAnimation(translation)
            delay(10000)
            view.translationX -= 200f

            startTurning(view)
        }
    }
    private fun bottomCheck(): Boolean {
        val layoutManager = bnd.rvChat.layoutManager
        val lastVisibleItemPosition = (layoutManager as? LinearLayoutManager)?.findLastCompletelyVisibleItemPosition() ?: RecyclerView.NO_POSITION
        val itemCount = layoutManager?.itemCount ?: 0
        //die letzte Position wird nicht immer erkannt bzw scrolled es auch dann wenn die letzte Position nur halb sichtbar ist
        return lastVisibleItemPosition >= itemCount - 2
    }
    private fun smoothScrollToLast(){

    }

}