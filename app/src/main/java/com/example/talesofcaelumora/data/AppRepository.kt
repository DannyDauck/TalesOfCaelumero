package com.example.talesofcaelumora.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.talesofcaelumora.data.datamodel.Card
import com.example.talesofcaelumora.data.datamodel.TimeData
import com.example.talesofcaelumora.data.remote.DateTimeApiService
import com.example.talesofcaelumora.data.remote.GameDataApiService
import java.lang.Exception
import java.time.LocalDateTime

const val TAG = "Repro"

class AppRepository(
    private val timeApi: DateTimeApiService.DataTimeApi,
    private val gameDataApiService: GameDataApiService
) {
    private val _dateTime = MutableLiveData<TimeData>()
    val dateTime: LiveData<TimeData>
        get() = _dateTime

    private val _cardLibrary = MutableLiveData<List<Card>>()
    val cardLibrary : LiveData<List<Card>>
        get() = _cardLibrary

    suspend fun getDateTime(){
        Log.d(TAG, "Request time data from api")
        try{
            val timeData = timeApi.retrofitService.getTime("Europe","London")
            _dateTime.value = timeData
        }catch (e: Exception){
            Log.e(TAG, "Something went wrong while requesting the time from api:\n" + e.message.toString())

            //Falls der API-Call nicht funktioniert, verwendet die App die Einstellungen des Handys
            _dateTime.value = TimeData(LocalDateTime.now().toString()+" not from API!!")

        }
    }
    suspend fun getCardLibrary(){
        Log.d(TAG, "fetching card library data from GameDataApiService")
        val cards = gameDataApiService.getAllCards()
        _cardLibrary.value = cards
    }
}