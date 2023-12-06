package com.example.talesofcaelumora.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.AppRepository
import com.example.talesofcaelumora.data.OFFSET_1900_TO_1970
import com.example.talesofcaelumora.data.datamodel.Battle
import com.example.talesofcaelumora.data.datamodel.Card
import com.example.talesofcaelumora.data.datamodel.GameState
import com.example.talesofcaelumora.data.datamodel.Player
import com.example.talesofcaelumora.data.datamodel.PlayerLocal
import com.example.talesofcaelumora.data.datamodel.convertPlayerLocalToPlayer
import com.example.talesofcaelumora.data.remote.DateTimeApiService
import com.example.talesofcaelumora.data.remote.GameDataFirebaseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.apache.commons.net.ntp.NTPUDPClient
import java.net.InetAddress
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repo = AppRepository(DateTimeApiService.DataTimeApi, GameDataFirebaseService(), application)

    //TODO löschen nach Präsentation
    val dateTime = repo.dateTime
    val cardLibrary = repo.cardLibrary


    private var job: Job? = null

    val gameState: LiveData<GameState>
        get() = repo.gameState


    val player : LiveData<Player>
        get() = repo.player

    private val _countdownText = MutableLiveData<String>()
    val countdownText: LiveData<String> get() = _countdownText

    private val _dayOfTheWeek = MutableLiveData<String>()
    val dayOfTheWeek: LiveData<String> get() = _dayOfTheWeek


    private var _currentBattle = MutableLiveData<Battle>()
    val currentBattle: LiveData<Battle>
        get() = _currentBattle

    private var _currentCard = MutableLiveData<Card>()
    val currentCard : LiveData<Card>
        get() = _currentCard

    // Ladestatusvariablen
    //TODO löschen nach Präsentation
    val dateTimeLoadingStatus = MutableLiveData<LoadingStatus>()
    val cardLibraryLoadingStatus = MutableLiveData<LoadingStatus>()

    // Ladezeitvariablen
    //TODO löschen nach Präsentation
    var dateTimeLoadingStartTime: Long = 0
    var cardLibraryLoadingStartTime: Long = 0

    //Ladefortschritt
    val cardLoadingProgress = repo.cardLoadingProgress
    var newCards = mutableListOf<Card>()


    //enum Klasse für Ladestaten
    enum class LoadingStatus {
        LOADING,
        SUCCESS,
        ERROR
    }

    init {
        // Ladevorgang für DateTime starten
        //TODO Löschen nach Präsentation
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


    fun setCurrentCard(card: Card){
        _currentCard.value = card
    }
    //TODO löschen nach Präsentation
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

    fun setCurrentBattle(battle: Battle){
        _currentBattle.value = battle
    }

    //Locale Database
    fun getGameState(uid: String){
        viewModelScope.launch {
            repo.getGameState(uid)
        }

    }
    fun getPlayer(uid: String){
        viewModelScope.launch {
            repo.getPlayer(uid)
        }
    }
    fun upsertFirebasePLayer(player: Player){
        viewModelScope.launch {
            repo.upsertFirebasePlayer(player)
        }
    }
    fun getFirebasePlayer(uid: String){
        viewModelScope.launch {
            repo.getFirebasePlayer(uid)
        }
    }
    fun deleteFirebasePlayer(uid: String){
        viewModelScope.launch{
            repo.deletFirebasePlayer(uid)
        }
    }

    fun upsertGameState(gameState: GameState){
        viewModelScope.launch {
            repo.upsertGameState(gameState)
            delay(2000)
            repo.getGameState(gameState.id)
        }

    }
    fun upsertPlayer(player: Player){
        viewModelScope.launch {
            repo.upsertPlayer(player)
            delay(5000)
            Log.d("MainViewModel", player.uid)
            repo.getPlayer(player.uid)
        }
    }
    fun setVolume(){
        //diese Funktion braucht nur einmal beim schließen der App aufgerufen werden, da
        //sich die App im Normalfall der Globals bedient, die im SplashScreen auf entsprechende Werte
        //gesetzt werden
        viewModelScope.launch {
            repo.setSoundmangerVolume()
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            // Hier können Sie Ihre Funktion beim Schließen der App aufrufen
            setVolume()
            upsertFirebasePLayer(player.value!!)
        }
        super.onCleared()
    }
    fun startCountdown() {
        viewModelScope.launch(Dispatchers.IO) {
            if(!getNetworkTime().isAfter(LocalDateTime.parse(player.value!!.lastCard))){
                Log.d("ViewModel", getNetworkTime().toString() )
                job?.cancel() // Stoppe einen laufenden Countdown, wenn vorhanden
                job = viewModelScope.launch(Dispatchers.IO) {
                    while (isActive) {
                        val currentTime = getNetworkTime()
                        val duration = Duration.between(currentTime, LocalDateTime.parse(player.value!!.lastCard))

                        if (duration.isNegative) {
                            _countdownText.postValue(getApplication<Application>().getString(R.string.now_available))
                            break
                        }

                        val hours = duration.toHours()
                        val minutes = (duration.toMinutes() % 60).toString().padStart(2, '0')
                        val seconds = (duration.seconds % 60).toString().padStart(2, '0')

                        _countdownText.postValue("$hours:$minutes:$seconds")

                        delay(1000)
                    }
                }
            }else _countdownText.postValue(getApplication<Application>().getString(R.string.now_available))
        }
    }
    fun getNetworkTime(): LocalDateTime {

        val timeServerAddress = InetAddress.getByName("time.google.com")
        val client = NTPUDPClient()
        client.defaultTimeout = 1000
        val timeInMillisSince1900 = client.getTime(timeServerAddress).returnTime
        //EpochMilli zählt von 1970
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeInMillisSince1900),
            ZoneOffset.UTC
        )
    }
    fun setNewLastCard(){
        viewModelScope.launch(Dispatchers.IO) {
            var newPlayer = player.value
            newPlayer!!.lastCard = getNetworkTime().plusDays(1).toString()
            upsertPlayer(newPlayer)
            startCountdown()
        }
    }
    fun getFreeCards(count: Int, balance: Int? = null){
        var cardsToAdd = mutableListOf<String>()
        repeat(count){
            val card = getFreeCard()
            cardsToAdd.add(card.id)
            newCards.add(card)
        }

        val newPlayer = player.value!!
        if(balance!=null)newPlayer.balance = balance
        newPlayer.bag = newPlayer.bag.plus(cardsToAdd)
        viewModelScope.launch {
            upsertPlayer(newPlayer!!)
        }
    }

    private fun getFreeCard(): Card{

        /*returned eine Karte nach einem Auswahlverfahren:
                                                                                                                                          9 von 10 rare
                                                                                                    1 von 10 rare oder ultra rare  ---->
                          4 von 5  Landkarte                        2 von 3  Supporterkarte --->                                          1 von 10 ultra rare
        cardLibrary ---->                                                                           9 von 10 normal
                          1 von 5  Hero oder Supporter Karte ---->                                                                 9 von 10 rare
                                                                                               1 von 10 rare oder ultra rare --->
                                                                    1 von 3 Herokarte ----->                                       1 von 10 ultra rare
                                                                                               9 von 10 normal
        80% Landkarten
        13,33% Supporterkarten
            -> 12% normal
            -> 1,2% rare
            -> 0,13% ultra rare
        6,66% Heldenkarten
            -> 6% normal
            -> 0,6% rare
            -> 0,06% ultra rare
         */

        var random = (1..5).random()
        var listToGetFrom = if(random!=5)cardLibrary.value!!.filter { it.cardType=="Land" } else cardLibrary.value!!.filter { it.cardType!="Land" }
        if(random==5){
            Log.d("ViewModel", "random: $random")
                random = (1..3).random()

                Log.d("ViewModel", "random: $random")
                if (random==3)listToGetFrom=listToGetFrom.filter { card -> card.cardType=="Hero" } else listToGetFrom=listToGetFrom.filter { card -> card.cardType=="Supporter" }
                random = (1..10).random()
                Log.d("ViewModel", "random: $random")
                listToGetFrom = if(random!=10)listToGetFrom.filter { it.rarity=="normal" } else listToGetFrom.filter { it.rarity!="normal" }
                if(random==10){
                    random = (1..10).random()
                    Log.d("ViewModel", "random: $random last random")
                    if(random==10){
                        listToGetFrom = listToGetFrom.filter { it.rarity=="ultra rare" }
                        return listToGetFrom.random().toCard()
                    }
                    else{
                        listToGetFrom = listToGetFrom.filter { it.rarity=="rare" }
                        return listToGetFrom.random().toCard()
                    }
                }else return listToGetFrom.random().toCard()
            }else return listToGetFrom.random().toCard()
    }
    fun clearNewCards(){
        newCards = mutableListOf()
    }
    fun addCards(list: List<Card>){
        val newPlayer = player.value!!
        var cardsToAdd = mutableListOf<String>()
        repeat(list.size){
            cardsToAdd.add(list[it].id)
        }
        newCards.addAll(list)
        newPlayer.bag = newPlayer.bag.plus(cardsToAdd)
        viewModelScope.launch {
            repo.upsertPlayer(newPlayer)
        }
    }
    fun addRandomHero(costs: Int = 0, type: String?){
        var cardList = cardLibrary.value!!.filter { it.cardType=="Hero" }.shuffled()
        if(type!=null) cardList = cardList.filter { it.type == type }
        var random = (1..10).random()
        if(random==10) {
            cardList = cardList.filter { it.rarity!="normal" }
            random = (0..10).random()
            if (random==10){
                cardList = cardList.filter { it.rarity=="ultra rare" }
            }else cardList = cardList.filter { it.rarity=="rare" }
        }else cardList = cardList.filter { it.rarity=="normal" }
        val card = cardList.random()
        newCards.add(card)
        val newPlayer = player.value!!
        newPlayer.bag = newPlayer.bag.plus(card.id)
        newPlayer.balance -= costs
        viewModelScope.launch{
            repo.upsertPlayer(newPlayer)
            repo.upsertFirebasePlayer(newPlayer)
        }
    }
    fun addRandomSupporter(costs: Int){
        var cardList = cardLibrary.value!!.filter { it.cardType=="Supporter" }.shuffled()
        var random = (1..10).random()
        if(random==10) {
            cardList = cardList.filter { it.rarity!="normal" }
            random = (1..10).random()
            if (random==10){
                cardList = cardList.filter { it.rarity=="ultra rare" }
            }else cardList = cardList.filter { it.rarity=="rare" }
        }else cardList = cardList.filter { it.rarity=="normal" }
        val card = cardList.random().toCard()
        newCards.add(card)
        val newPlayer = player.value!!
        newPlayer.balance -= costs
        newPlayer.bag = newPlayer.bag.plus(card.id)
        viewModelScope.launch{
            repo.upsertPlayer(newPlayer)
            repo.upsertFirebasePlayer(newPlayer)
        }
    }
    fun addRandomRareOrUltraRare(costs: Int = 0){
        var random = (1..3).random()
        var cardList = cardLibrary.value!!
        if(random==3) cardList = cardList.filter { it.cardType=="Hero" }
        else cardList = cardList.filter { it.cardType=="Supporter" }
        random = (1..10).random()
        if(random==10) cardList = cardList.filter { it.rarity=="ultra rare" }
        else cardList = cardList.filter { it.rarity=="rare" }
        val card = cardList.random().toCard()
        newCards.add(card)
        val newPlayer = player.value!!
        newPlayer.balance -= costs
        newPlayer.bag = newPlayer.bag.plus(card.id)
        viewModelScope.launch{
            repo.upsertPlayer(newPlayer)
            repo.upsertFirebasePlayer(newPlayer)
        }
    }
    fun getRareBooster(costs: Int = 0){
        getFreeCards(4, player.value!!.balance-costs)
        viewModelScope.launch {
            //um sicher zu stellen das die upsertMethode von getFreeCards abgeschlossen ist
            delay(1500)
            addRandomRareOrUltraRare()
        }
    }
    fun getDayOfTheWeek(){

        job?.cancel() // Stoppt einen laufenden Countdown, wenn vorhanden
        job = viewModelScope.launch(Dispatchers.IO) {
            while (isActive){
                var date = getNetworkTime()
                var duration = Duration.between(date.toLocalTime(), LocalTime.MAX)
                if (duration.isNegative) {
                    _countdownText.postValue(getApplication<Application>().getString(R.string.now_available))
                    _dayOfTheWeek.postValue(date.dayOfWeek.name)
                    break
                }

                val hours = duration.toHours()
                val minutes = (duration.toMinutes() % 60).toString().padStart(2, '0')
                val seconds = (duration.seconds % 60).toString().padStart(2, '0')

                _countdownText.postValue("$hours:$minutes:$seconds")
                delay(1000)
                _dayOfTheWeek.postValue(date.dayOfWeek.name)
            }
        }
    }
    fun getBoosterOfTheDay(type:String){
        var list = mutableListOf<Card>()
        repeat(4){
            list.add(cardLibrary.value!!.filter { it.cardType=="Land" && it.type==type}.random().toCard())
        }
        addCards(list)
        addRandomHero(0, type)
    }

}