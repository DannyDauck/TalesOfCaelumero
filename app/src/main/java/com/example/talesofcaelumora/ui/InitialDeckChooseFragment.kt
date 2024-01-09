package com.example.talesofcaelumora.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.OFFSET_1900_TO_1970
import com.example.talesofcaelumora.data.characters
import com.example.talesofcaelumora.data.datamodel.GameState
import com.example.talesofcaelumora.data.datamodel.Player
import com.example.talesofcaelumora.data.datamodel.battlefields
import com.example.talesofcaelumora.data.datamodel.initialDeckAir
import com.example.talesofcaelumora.data.datamodel.initialDeckFire
import com.example.talesofcaelumora.data.datamodel.initialDeckPlant
import com.example.talesofcaelumora.data.datamodel.initialDeckWater
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.databinding.FragmentInitilaDeckChooseBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import com.google.android.material.shape.CornerFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.time.LocalDateTime
import org.apache.commons.net.ntp.NTPUDPClient
import java.time.Instant
import java.time.ZoneOffset

class InitialDeckChooseFragment : Fragment() {


    private lateinit var bnd: FragmentInitilaDeckChooseBinding
    private lateinit var soundManager: SoundManager
    private lateinit var firebaseAuth: FirebaseAuth
    private var name = ""
    private var character = 0
    private lateinit var player: Player
    private var slowTextIndex = 0
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name")!!
            character = it.getInt("character")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = Firebase.auth
        soundManager = SoundManager.getInstance(requireContext())
        bnd = FragmentInitilaDeckChooseBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bnd.llDecks.isVisible = false
        bnd.txtElara.isVisible = true
        bnd.txtPlayer.isGone = true
        bnd.clConfirm.isVisible = false

        bnd.imgPlayerCharacter.setImageResource(character)

        slowPrintText(getString(R.string.choose_deck_one))


    }

    private fun slowPrintText(text: String, string: String? = "") {
        val handler = Handler(Looper.getMainLooper())
        var index = 1


        val runnable = object : Runnable {
            override fun run() {
                bnd.btnFastTxt.setOnClickListener {
                    index = text.length - 4
                }
                if (index < text.length) {
                    bnd.txtElara.text = text.substring(0, index + 1)
                    index++
                    handler.postDelayed(this, 50)
                } else {
                    if (slowTextIndex == 0) {
                        lifecycleScope.launch {

                            delay(2000)
                            bnd.btnFastTxt.setOnClickListener(null)
                            // da das Layout an Englisch angepasst korrigiere ich hier die Textlänge um keine Überlappungen zu erhalten
                            val first = if (bnd.txtElara.text.length <= 168) 0 else maxOf(
                                bnd.txtElara.text.length - 171,
                                0
                            )
                            bnd.txtElara.text =
                                "..." + getString(R.string.choose_deck_one).substring(
                                    first,
                                    bnd.txtElara.text.length
                                )
                            bnd.llDecks.isVisible = true
                            soundManager.playSound(R.raw.message_in)

                            bnd.airDeck.setOnClickListener {
                                bnd.txtConfirmText.text =
                                    getString(R.string.choose_deck_confirm, getString(R.string.air))
                                bnd.clConfirm.isVisible = true
                                bnd.llDecks.isVisible = false
                                bnd.btnNo.setOnClickListener {
                                    bnd.clConfirm.isVisible = false
                                    bnd.llDecks.isVisible = true
                                }
                                bnd.btnYes.setOnClickListener {
                                    bnd.clConfirm.isVisible = false
                                    playerText(getString(R.string.air))
                                }

                            }
                            bnd.fireDeck.setOnClickListener {
                                bnd.txtConfirmText.text = getString(
                                    R.string.choose_deck_confirm,
                                    getString(R.string.fire)
                                )
                                bnd.clConfirm.isVisible = true
                                bnd.llDecks.isVisible = false
                                bnd.btnNo.setOnClickListener {
                                    bnd.clConfirm.isVisible = false
                                    bnd.llDecks.isVisible = true
                                }
                                bnd.btnYes.setOnClickListener {
                                    bnd.clConfirm.isVisible = false
                                    playerText(getString(R.string.fire))
                                }

                            }
                            bnd.waterDeck.setOnClickListener {
                                bnd.txtConfirmText.text = getString(
                                    R.string.choose_deck_confirm,
                                    getString(R.string.water)
                                )
                                bnd.clConfirm.isVisible = true
                                bnd.llDecks.isVisible = false
                                bnd.btnNo.setOnClickListener {
                                    bnd.clConfirm.isVisible = false
                                    bnd.llDecks.isVisible = true
                                }
                                bnd.btnYes.setOnClickListener {
                                    bnd.clConfirm.isVisible = false
                                    playerText(getString(R.string.water))
                                }
                            }
                            bnd.plantDeck.setOnClickListener {
                                bnd.txtConfirmText.text = getString(
                                    R.string.choose_deck_confirm,
                                    getString(R.string.nature)
                                )
                                bnd.clConfirm.isVisible = true
                                bnd.llDecks.isVisible = false
                                bnd.btnNo.setOnClickListener {
                                    bnd.clConfirm.isVisible = false
                                    bnd.llDecks.isVisible = true
                                }
                                bnd.btnYes.setOnClickListener {
                                    bnd.clConfirm.isVisible = false
                                    playerText(getString(R.string.nature))
                                }
                            }

                        }
                    } else {
                        val battlefield = when (string) {
                            getString(R.string.air) -> battlefields[3]
                            getString(R.string.fire) -> battlefields[2]
                            getString(R.string.water) -> battlefields[1]
                            else -> battlefields[0]
                        }
                        if (firebaseAuth.currentUser != null) {
                            var gameState = GameState(
                                firebaseAuth.currentUser!!.uid,
                                true
                            )
                            gameState.achievements =
                                gameState.achievements.plus(getString(R.string.first_day_in_Caelumero))
                            viewModel.upsertGameState(gameState)

                            var player = Player(
                                firebaseAuth.currentUser!!.uid,
                                name,
                                1,
                                character,
                                when (string) {
                                    getString(R.string.air) -> initialDeckAir
                                    getString(R.string.fire) -> initialDeckFire
                                    getString(R.string.water) -> initialDeckWater
                                    else -> initialDeckPlant
                                },
                                listOf("Rooky"),
                                "Rooky",
                                "2023-12-01T16:27:38.044",
                                150,
                                0,
                                when (string) {
                                    getString(R.string.air) -> initialDeckAir
                                    getString(R.string.fire) -> initialDeckFire
                                    getString(R.string.water) -> initialDeckWater
                                    else -> initialDeckPlant
                                },
                                70,
                                5,
                                3,
                                40,
                                battlefield,
                                100
                            )
                            viewModel.upsertPlayer(player)
                            viewModel.upsertFirebasePLayer(player)
                            lifecycleScope.launch {
                                delay(1000)
                                viewModel.getPlayer(firebaseAuth.currentUser!!.uid)
                                delay(2000)
                                findNavController().navigate(InitialDeckChooseFragmentDirections.actionInitilaDeckChooseFragmentToHomeFragment())
                            }

                        }
                    }
                }
            }
        }


        handler.postDelayed(runnable, 100)
    }

    private fun playerText(string: String) {
        val translateElara = TranslateAnimation(
            0f,
            resources.displayMetrics.density * 100f,
            0f,
            0f
        )
        val translatePlayer =
            TranslateAnimation(-120f * resources.displayMetrics.density, 0f, 0f, 0f)
        translateElara.duration = 1000
        translatePlayer.duration = 1000
        bnd.imgElaraHead.startAnimation(translateElara)
        lifecycleScope.launch {
            delay(950)
            bnd.imgElaraHead.translationX = resources.displayMetrics.density * 200f
            //set the text to an single return so the speaking bubble will stay visible
            bnd.txtPlayer.text = "\n"
            bnd.txtPlayer.isVisible = true
            bnd.txtElara.isGone = true
            delay(1500)
            //setzt erst die Visibility auf false, dann die Zielposition,
            // startet danach die Animation von der neuen Position aus umgekehrt.
            // Das verhindert das ein Glitsch entsteht, weil nun die aktuelle Position ja die Zielposition ist
            bnd.imgPlayerCharacter.isVisible = false
            bnd.imgPlayerCharacter.translationX = resources.displayMetrics.density * 120f
            bnd.imgPlayerCharacter.startAnimation(translatePlayer)
            bnd.imgPlayerCharacter.isVisible = true
            delay(950)
            val handler = Handler(Looper.getMainLooper())
            var index = 1


            val text = getString(R.string.player_confirm_deck, string)


            val runnable = object : Runnable {
                override fun run() {
                    bnd.btnFastTxt.setOnClickListener {
                        index = text.length - 4
                    }
                    if (index < text.length) {
                        bnd.txtPlayer.text = text.substring(0, index + 1)
                        index++
                        handler.postDelayed(this, 50)
                    } else {
                        var density = resources.displayMetrics.density
                        val playerAnimation = TranslateAnimation(0f, -120f * density, 0f, 0f)
                        playerAnimation.duration = 1000
                        lifecycleScope.launch {
                            delay(1500)
                            bnd.imgPlayerCharacter.startAnimation(playerAnimation)
                            delay(950)
                            bnd.imgPlayerCharacter.translationX = 0f
                            bnd.txtElara.text = "\n"
                            bnd.txtElara.isVisible = true
                            bnd.txtPlayer.isGone = true
                            bnd.imgElaraHead.isVisible = false
                            bnd.imgElaraHead.translationX = density * 100f
                            var animationElara = TranslateAnimation(density * 100f, 0f, 0f, 0f)
                            animationElara.duration = 1000
                            bnd.imgElaraHead.startAnimation(animationElara)
                            bnd.imgElaraHead.isVisible = true
                            slowTextIndex++
                            delay(1200)
                            slowPrintText(getString(R.string.choose_deck_finish, name), string)

                        }

                    }
                }
            }
            handler.postDelayed(runnable, 100)
        }
    }

}