package com.example.talesofcaelumora.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.talesofcaelumora.data.AppRepository
import com.example.talesofcaelumora.data.remote.DateTimeApiService
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repro = AppRepository(DateTimeApiService.DataTimeApi)

    val dateTime = repro.dateTime

    fun getDateTime(){
        viewModelScope.launch {
            repro.getDateTime()
        }
    }

}