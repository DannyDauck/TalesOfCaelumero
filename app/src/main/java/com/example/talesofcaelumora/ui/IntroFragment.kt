package com.example.talesofcaelumora.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.databinding.FragmentIntroBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class IntroFragment : Fragment() {

    private lateinit var bnd: FragmentIntroBinding
    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.close_app)
                builder.setMessage(R.string.sure_to_leave)
                builder.setPositiveButton(R.string.yes) { _, which ->
                    requireActivity().finish()
                }
                builder.setNegativeButton(R.string.no) { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        soundManager = SoundManager.getInstance(requireContext())
        bnd = FragmentIntroBinding.inflate(inflater, container, false)
        return bnd.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        soundManager.startRadio(R.raw.air_theme)
        slowPrintText(getString(R.string.on_boarding_one))

    }

    private fun slowPrintText(text: String){
        val handler = Handler(Looper.getMainLooper())
        var index = 1


        // Erstelle einen Runnable, um den Typewriter-Effekt zu implementieren
        val runnable = object : Runnable {
            override fun run() {
                bnd.btnFastTxt.setOnClickListener {
                    soundManager.playSound(R.raw.button_click)
                    index = text.length -4
                }
                if (index < text.length) {
                    // Füge den nächsten Buchstaben hinzu und poste den Runnable erneut
                    bnd.txtElara.text = text.substring(0, index + 1)
                    index++
                    handler.postDelayed(this, 50)
                }else{
                    lifecycleScope.launch {

                        delay(2000)
                        bnd.btnFastTxt.setOnClickListener(null)
                        bnd.btnFastTxt.isGone = true
                        bnd.btnOk.isVisible = true
                        bnd.itName.isVisible = true
                        soundManager.playSound(R.raw.message_in)
                        bnd.itName.requestFocus()
                        delay(1000)
                        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(bnd.itName, InputMethodManager.SHOW_IMPLICIT).apply{
                            //schließt die Tastatur bei Klick auf Enter wieder
                            bnd.itName.setOnEditorActionListener { _, actionId, event ->
                                if (actionId == EditorInfo.IME_ACTION_DONE ||
                                    (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
                                ) {
                                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.hideSoftInputFromWindow(bnd.itName.windowToken, 0)
                                    return@setOnEditorActionListener true
                                }
                                return@setOnEditorActionListener false
                            }

                        }
                        bnd.btnOk.setOnClickListener{
                            if(bnd.itName.text != null && bnd.itName.text.toString().length>2){
                                soundManager.playSound(R.raw.button_click)
                                findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToPlayerBuilderFragment(bnd.itName.text.toString()))
                            }
                        }

                    }

                }
            }
        }

        handler.postDelayed(runnable, 100)
    }


}