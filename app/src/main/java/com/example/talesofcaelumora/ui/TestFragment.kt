package com.example.talesofcaelumora.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.FragmentTestBinding
import com.example.talesofcaelumora.ui.viewmodel.MainViewModel
import java.io.File
import javax.sql.DataSource


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
        var index = 0
        val maxIndex = vm.cardLibrary.value!!.size-1
        val TAG = "Test"

        val imagePath = "images/${vm.cardLibrary.value!![index].id}.jpeg"
        Log.d(TAG, "${vm.cardLibrary.value}")

        Glide.with(requireContext())
            .load(File(requireContext().filesDir, imagePath))
            .listener(object : RequestListener<Drawable?> {


                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("Glide", "Fehler beim laden der Datei")
                    return false
                }

            })
            .into(bnd.img)

        bnd.btnImgBack.setOnClickListener {
            if(index>0){
                index--
                Glide.with(requireContext())
                    .load(File(requireContext().filesDir,"images/${vm.cardLibrary.value!![index].id}.jpeg"))
                    .into(bnd.img)
            }
        }
        bnd.btnImgForward.setOnClickListener {
            if(index<maxIndex){
                index++
                Glide.with(requireContext())
                    .load(File(requireContext().filesDir,"images/${vm.cardLibrary.value!![index].id}.jpeg"))
                    .into(bnd.img)
            }
        }
        bnd.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}