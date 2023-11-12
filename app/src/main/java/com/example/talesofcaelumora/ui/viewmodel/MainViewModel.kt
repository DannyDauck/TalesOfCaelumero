package com.example.talesofcaelumora.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.talesofcaelumora.data.AppRepository
import com.example.talesofcaelumora.data.remote.DateTimeApiService
import com.example.talesofcaelumora.data.remote.GameDataApiService
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repo = AppRepository(DateTimeApiService.DataTimeApi, GameDataApiService())

    val dateTime = repo.dateTime
    val cardLibrary = repo.cardLibrary

    // Ladestatusvariablen
    val dateTimeLoadingStatus = MutableLiveData<LoadingStatus>()
    val cardLibraryLoadingStatus = MutableLiveData<LoadingStatus>()

    // Ladezeitvariablen
    var dateTimeLoadingStartTime: Long = 0
    var cardLibraryLoadingStartTime: Long = 0

    //Ladefortschritt
    val cardLoadingProgress = repo.cardLoadingProgress


    //enum Klasse für Ladestaten
    enum class LoadingStatus {
        LOADING,
        SUCCESS,
        ERROR
    }

    init {
        // Ladevorgang für DateTime starten
        dateTimeLoadingStatus.value = LoadingStatus.LOADING
        dateTimeLoadingStartTime = System.currentTimeMillis()
        getDateTime()

        // Ladevorgang für CardLibrary starten
        if(cardLibrary.value.isNullOrEmpty()){
            cardLibraryLoadingStatus.value = LoadingStatus.LOADING
            cardLibraryLoadingStartTime = System.currentTimeMillis()
            getCardLibrary()
        }

    }




    fun getDateTime(){
        viewModelScope.launch {
            repo.getDateTime()
            dateTimeLoadingStatus.value = LoadingStatus.SUCCESS
        }
    }
    fun getCardLibrary(){
        viewModelScope.launch{
            repo.getCardLibrary()
            cardLibraryLoadingStatus.value = LoadingStatus.SUCCESS
        }
    }

}