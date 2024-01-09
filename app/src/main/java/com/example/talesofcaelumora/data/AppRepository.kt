package com.example.talesofcaelumora.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.talesofcaelumora.data.datamodel.Battle
import com.example.talesofcaelumora.data.datamodel.Card
import com.example.talesofcaelumora.data.datamodel.ChatItem
import com.example.talesofcaelumora.data.datamodel.GameState
import com.example.talesofcaelumora.data.datamodel.Player
import com.example.talesofcaelumora.data.datamodel.PlayerLocal
import com.example.talesofcaelumora.data.datamodel.TimeData
import com.example.talesofcaelumora.data.datamodel.convertPlayerLocalToPlayer
import com.example.talesofcaelumora.data.local.GameStateDatabase
import com.example.talesofcaelumora.data.remote.DateTimeApiService
import com.example.talesofcaelumora.data.remote.GameDataFirebaseService
import kotlinx.coroutines.delay

const val TAG = "Repro"

class AppRepository(
    //TODO Löschen nach Präsentation
    private val timeApi: DateTimeApiService.DataTimeApi,
    private val gameDataFirebaseService: GameDataFirebaseService,
    private val  application: Application
) {



    private var gameStateDataBase = GameStateDatabase.getInstance(application)

    private var _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState>
        get() = _gameState

    private var _player= MutableLiveData<Player>()
    val player : LiveData<Player>
        get() = _player

    private var _dateTime= MutableLiveData<TimeData> ()
    val dateTime: LiveData<TimeData>
        get() = _dateTime

    private val _cardLibrary = MutableLiveData<List<Card>>()
    val cardLibrary : LiveData<List<Card>>
        get() = _cardLibrary

    private val _cardLoadingProgress = MutableLiveData<List<Int>>(listOf(gameDataFirebaseService.loaded,gameDataFirebaseService.progress))
    val cardLoadingProgress: LiveData<List<Int>>
        get() = _cardLoadingProgress

    val chat: LiveData<List<ChatItem>>
        get() = gameDataFirebaseService.chat

    val battles: LiveData<List<Battle>>
        get() = gameDataFirebaseService.battles


    //TODO Löschen nach Präsentation
    suspend fun getDateTime(){
        Log.d(TAG, "Request time data from api")
        try{
            val timeData = timeApi.retrofitService.getTime()
            _dateTime.value = timeData
        }catch (e: Exception){
            Log.e(TAG, "Something went wrong while requesting the time from api:\n" + e.message.toString())

            //Falls der API-Call nicht funktioniert, verwendet die App die Einstellungen des Handys

        }
    }
    suspend fun getCardLibrary(){
        Log.d(TAG, "fetching card library data from GameDataApiService")
        val cards = gameDataFirebaseService.getAllCards(setProgress)
        _cardLibrary.value = cards
    }
    suspend fun getGameState(uid: String){
        val gameState = gameStateDataBase.gameStateDao.getGameState(uid)
       if(gameState!=null)_gameState.value =  gameStateDataBase.gameStateDao.getGameState(uid)
    }
    suspend fun getPlayer(uid: String){
        if(gameStateDataBase.gameStateDao.getPlayer(uid)!= null)_player.value = convertPlayerLocalToPlayer(gameStateDataBase.gameStateDao.getPlayer(uid)!!)
    }
    suspend fun upsertGameState(gameState: GameState){
        gameStateDataBase.gameStateDao.saveGameState(gameState)
    }
    suspend fun upsertPlayer(player: Player){
        gameStateDataBase.gameStateDao.savePlayer(PlayerLocal(player))
    }
    suspend fun upsertFirebasePlayer(player: Player){
        gameDataFirebaseService.savePlayerToFirebase(player)
    }
    suspend fun getFirebasePlayer(uid: String){
        var player = gameDataFirebaseService.getPlayer(uid)
        if(player!=null){
            upsertPlayer(player)
            delay(1000)
            getPlayer(player.uid)
        }
    }
    suspend fun deletFirebasePlayer(uid: String){
        gameDataFirebaseService.deletePlayer(uid)
    }

    var setProgress = { loaded: Int, progress: Int ->
        setProgress(loaded, progress)
    }
    fun setProgress(loaded: Int, progress: Int){
        _cardLoadingProgress.value = listOf(loaded, progress)
    }
    fun resetGameApiServiceProgress(){
        gameDataFirebaseService.resetProgress()
    }

    suspend fun setSoundmangerVolume(){
        _gameState.value!!.musicVolume = musicVolume
        _gameState.value!!.vfxVolume = vfxVolume
    }
    suspend fun sendMessageToGlobalChat(message: ChatItem){
        gameDataFirebaseService.sendMessageInGlobalChat(message)
    }
    suspend fun observeChat(language: String){
        gameDataFirebaseService.observeChat(language)
    }

    suspend fun getBattles(context: Context){
        gameDataFirebaseService.getBattles(player.value!!.uid, context)
    }
    suspend fun upsertBattle(battle: Battle){
        gameDataFirebaseService.pushBattle(battle)
    }
    suspend fun getMultibattle(context: Context): Battle?{
        return gameDataFirebaseService.getMultiBattle(context)
    }
}
