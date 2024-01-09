package com.example.talesofcaelumora.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.datamodel.GameState
import com.example.talesofcaelumora.data.musicVolume
import com.example.talesofcaelumora.data.utils.ImageLoader
import com.example.talesofcaelumora.data.utils.SoundManager
import com.example.talesofcaelumora.data.vfxVolume
import com.example.talesofcaelumora.databinding.FragmentSplashScreenBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenFragment : Fragment() {

    private lateinit var bnd: FragmentSplashScreenBinding
    private lateinit var imageLoader: ImageLoader
    private lateinit var soundManager: SoundManager
    val vm: MainViewModel by activityViewModels()
    private lateinit var firebaseAuth: FirebaseAuth
    var libraryLoaded = false
    var imagesLoaded = false
    var animationPlayed = false
    val TAG = "Splash Screen"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = com.google.firebase.ktx.Firebase.auth
        bnd = FragmentSplashScreenBinding.inflate(inflater, container, false)
        imageLoader  = ImageLoader(requireContext())

        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.getGameState(firebaseAuth.currentUser?.uid.toString())
        vm.getPlayer(firebaseAuth.currentUser?.uid.toString())

        Log.d("SplashScreen", "current Firebase UID: ${firebaseAuth.uid}")
        soundManager = SoundManager.getInstance(requireContext())
        vm.cardLoadingProgress.observe(viewLifecycleOwner) { progress ->
            //Aktualisiert die Progressbar je nach Fortschritt der zu ladenden CardLibrary,
            // indem Fall nur durch 50 weil der nächste Schritt die Bilder lädt, die genauso viel sind wie Cards
            // in der library
            if(progress[0] !=0 && progress[1] != 0) bnd.pbLoadingProgress.progress = ((progress[0] * 50)/progress[1])
            bnd.txtProgress.text = progress[0].toString() + "von " + progress[1] + " geladen"
            Log.d("Progress", "$progress")
            if(progress[0]==progress[1]&&progress[1]!=0){
                Log.e(TAG, "Cards loaded, start getting images")
                Log.d(TAG, vm.cardLibrary.value.toString())

            }
        }
        vm.cardLibrary.observe(viewLifecycleOwner){ library ->
            if(!library.isNullOrEmpty()&&library.size== vm.cardLoadingProgress.value!!.get(1)){
                Log.d("Library", library.size.toString())
                libraryLoaded = true
                var progressCounter = 0
                imageLoader.loadIfNotExist("temple_of_the_waterbenders", "https://firebasestorage.googleapis.com/v0/b/tales-of-caelumero.appspot.com/o/card%2Fimages%2Ftemple_of_waterbenders.jpeg?alt=media&token=8e5ebff9-b7b1-4fd9-a8a3-80ab473d8e01")
                library.forEach {
                    imageLoader.loadIfNotExist(it.id,it.imgSrc)
                    progressCounter++
                    bnd.pbLoadingProgress.progress = 50 + (progressCounter*50/library.size*50)
                    bnd.txtProgress.text = progressCounter.toString() + " " +getString(R.string.from) + " " + library.size + " " +  getString(R.string.image_resources_loaded)
                    Log.d(TAG, "${it.cardName} loaded to images")
                    if(progressCounter == library.size){
                        imagesLoaded = true
                    }
                    if(imagesLoaded&&libraryLoaded&&animationPlayed){
                        //delay um sicherzustellen das er das Bild fertig lädt
                        lifecycleScope.launch {
                            delay(500)
                            if(vm.player.value != null && firebaseAuth.currentUser?.uid == vm.player.value!!.uid){
                                vm.observeBattles(requireContext())
                                musicVolume = vm.gameState.value!!.musicVolume
                                vfxVolume = vm.gameState.value!!.vfxVolume
                                soundManager.setRadioVolume()
                                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
                            }
                            else findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToIntroFragment())
                        }

                    }
                }
            }
        }

        rotate()

    }

    fun rotate(){
        val rotation = RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotation.duration = 10000
        val rotationBack = RotateAnimation(0f, -720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotationBack.duration = 10000
        val shieldrotation = RotateAnimation(0f, -720f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF ,0.5f)
        shieldrotation.duration = 10000

        bnd.clMain.startAnimation(rotation)

        bnd.imgShield.startAnimation(shieldrotation)
        listOf(bnd.imgAirchip, bnd.imgFirechip, bnd.imgWaterchip, bnd.imgPlantchip).forEach { it.startAnimation(rotationBack) }
        lifecycleScope.launch {
            delay(10000)
            rotateback()
        }
    }
    fun rotateback(){
        val rotation = RotateAnimation(0f, -720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotation.duration = 5000
        val rotationback = RotateAnimation(0f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotationback.duration = 5000
        val shieldrotation = RotateAnimation(0f, -1080f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF ,0.5f)
        shieldrotation.duration = 7000

        bnd.clMain.startAnimation(rotation)
        bnd.imgShield.startAnimation(shieldrotation)
        soundManager.playSound(R.raw.rusty_wheel)
        listOf(bnd.imgAirchip, bnd.imgFirechip, bnd.imgWaterchip, bnd.imgPlantchip).forEach { it.startAnimation(rotationback) }
        lifecycleScope.launch {
            delay(6705)
            soundManager.playSound(R.raw.wooden_wheel)
            delay(1000)
            soundManager.playSound(R.raw.snap_in)
            bnd.imgPlantchip.isVisible = false
            delay(500)
            soundManager.playSound(R.raw.snap_in)
            bnd.imgFirechip.isVisible = false
            delay(500)
            soundManager.playSound(R.raw.snap_in)
            bnd.imgWaterchip.isVisible = false
            delay(500)
            soundManager.playSound(R.raw.snap_in)
            bnd.imgAirchip.isVisible = false
            delay(700)
            soundManager.playSound(R.raw.snap_in)
            bnd.imgShield.isVisible = false
            delay(800)
            animationPlayed = true
            if(imagesLoaded && libraryLoaded && animationPlayed){
                lifecycleScope.launch {
                    delay(500)
                    if(vm.player.value != null && firebaseAuth.currentUser?.uid == vm.player.value!!.uid){

                        musicVolume = vm.gameState.value!!.musicVolume
                        vfxVolume = vm.gameState.value!!.vfxVolume
                        soundManager.setRadioVolume()
                        vm.observeBattles(requireContext())
                        findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
                    }
                    else{
                        Log.d("SplashScreen", firebaseAuth.uid.toString())
                        vm.getFirebasePlayer(firebaseAuth.uid!!)
                        lifecycleScope.launch{
                            //Überprüfen ob in der Lokalen Datenbank ein Player
                            //besteht wenn nicht abfragen ob bereits ein FirebasePlayer besteht
                            //,wenn ja erstellt es einen Lokalen Spieler mit leerem Gamestate
                            //, wenn nicht beginnt das OnBoarding
                            delay(1500)
                            if(vm.player.value==null||vm.player.value!!.uid!=firebaseAuth.currentUser!!.uid){
                                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToIntroFragment())
                            }
                            else if(vm.gameState.value==null){
                                val gS = GameState(
                                    firebaseAuth.currentUser!!.uid,
                                    true
                                )
                                gS.achievements =
                                    gS.achievements.plus(getString(R.string.first_day_in_Caelumero))
                                vm.upsertGameState(gS)
                                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
                            }
                        }
                    }
                }
            }

        }
    }

}