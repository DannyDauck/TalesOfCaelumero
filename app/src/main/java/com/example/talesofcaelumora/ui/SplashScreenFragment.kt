package com.example.talesofcaelumora.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenFragment : Fragment() {

    private lateinit var bnd: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rotate()

    }

    fun rotate(){
        val rotation = RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotation.duration = 10000
        val rotationBack = RotateAnimation(0f, -720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotationBack.duration = 10000

        bnd.clMain.startAnimation(rotation)
        listOf(bnd.imageView,bnd.imageView2,bnd.imageView3,bnd.imageView4).forEach { it.startAnimation(rotationBack) }
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

        bnd.clMain.startAnimation(rotation)
        listOf(bnd.imageView,bnd.imageView2,bnd.imageView3,bnd.imageView4).forEach { it.startAnimation(rotationback) }
        lifecycleScope.launch {
            delay(6000)
            bnd.imageView4.isVisible = false
            delay(500)
            bnd.imageView3.isVisible = false
            delay(500)
            bnd.imageView2.isVisible = false
            delay(500)
            bnd.imageView.isVisible = false
            delay(500)
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
        }

    }
}