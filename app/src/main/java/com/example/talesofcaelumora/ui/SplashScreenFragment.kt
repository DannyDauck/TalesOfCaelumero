package com.example.talesofcaelumora.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.datamodel.Card
import com.example.talesofcaelumora.data.utils.ImageLoader
import com.example.talesofcaelumora.databinding.FragmentSplashScreenBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenFragment : Fragment() {

    private lateinit var bnd: FragmentSplashScreenBinding
    private lateinit var imageLoader: ImageLoader
    val vm: MainViewModel by activityViewModels()
    var libraryLoaded = false
    var imagesLoaded = false
    var animationPlayed = false
    val TAG = "Splash Screen"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentSplashScreenBinding.inflate(inflater, container, false)
        imageLoader  = ImageLoader(requireContext())
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.getDateTime()
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
                            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
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
        listOf(bnd.imgAirchip, bnd.imgFirechip, bnd.imgWaterchip, bnd.imgPlantchip).forEach { it.startAnimation(rotationback) }
        lifecycleScope.launch {
            delay(8000)
            bnd.imgPlantchip.isVisible = false
            delay(500)
            bnd.imgFirechip.isVisible = false
            delay(500)
            bnd.imgWaterchip.isVisible = false
            delay(500)
            bnd.imgAirchip.isVisible = false
            delay(700)
            bnd.imgShield.isVisible = false
            delay(800)
            animationPlayed = true
            if(imagesLoaded && libraryLoaded && animationPlayed){
                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
            }

        }
    }

}