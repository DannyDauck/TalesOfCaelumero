package com.example.talesofcaelumora.data.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.talesofcaelumora.data.datamodel.Battle
import com.example.talesofcaelumora.data.datamodel.Card
import com.example.talesofcaelumora.data.datamodel.ChatItem
import com.example.talesofcaelumora.data.datamodel.Player
import com.example.talesofcaelumora.data.datamodel.battleToMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

private const val TAG = "GameDataApiDService"

class GameDataFirebaseService() {

    private val gameDatabase = FirebaseFirestore.getInstance()
    var progress = 0
    var loaded = 0

    private var currentPlayer = FirebaseAuth.getInstance().currentUser

    private var _chat = MutableLiveData<List<ChatItem>>(listOf())
    val chat: LiveData<List<ChatItem>>
        get() = _chat

    private var _battles = MutableLiveData<List<Battle>>(listOf())
    val battles: LiveData<List<Battle>>
        get() = _battles

    suspend fun getAllCards(setProgress: (Int, Int) -> Unit): List<Card> {
        return try {
            val snapshot = gameDatabase.collection("cards").get().await()

            var list = mutableListOf<Card>()
            progress = snapshot.documents.size
            snapshot.documents.forEach { doc ->
                val cardData = doc.getData()

                if (cardData != null) try {
                    cardData["id"] = doc.id
                    list.add(Card(cardData.toMap()))
                    loaded++
                    setProgress(loaded, progress)
                } catch (e: java.lang.Exception) {
                    Log.e(TAG, "Fehler beim Parsen der Karte ${doc.id}")
                }
            }
            list
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Abrufen aller Karten", e)
            emptyList()
        }
        //Werte wieder zurücksetzen weil sonst beim aktualisieren wieder aufgezählt wird
    }

    fun resetProgress() {
        progress = 0
        loaded = 0
    }

    suspend fun savePlayerToFirebase(player: Player) {
        try {
            gameDatabase.collection("players").document(player.uid).set(player).await()
            Log.d(TAG, "Spieler erfolgreich in die Firestore-Datenbank geschrieben")
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Schreiben des Spielers in die Firestore-Datenbank", e)
        }
    }

    suspend fun deletePlayer(uid: String) {
        try {
            gameDatabase.collection("players").document(uid).delete().await()
            Log.d(TAG, "Spieler erfolgreich aus der Firestore-Datenbank gelöscht")
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Löschen des Spielers aus der Firestore-Datenbank", e)
        }
    }

    suspend fun getPlayer(uid: String): Player? {
        return try {
            val snapshot = gameDatabase.collection("players").document(uid).get().await()

            if (snapshot.exists()) {
                val playerData = snapshot.getData()
                if (playerData != null) {
                    // Hier musst du die Logik implementieren, um ein Player-Objekt aus den Daten zu erstellen
                    // Du musst die entsprechende Spielerklasse implementieren und hier verwenden
                    return Player(playerData.toMap())
                }
            }

            null // Rückgabe, wenn der Spieler nicht gefunden wurde
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Abrufen des Spielers aus der Firestore-Datenbank", e)
            null
        }
    }

    suspend fun sendMessageInGlobalChat(message: ChatItem) {
        try {
            gameDatabase.collection("chat").document("global-${message.language}").set(message)
                .await()
            Log.d("repo", "Chatnachricht erfolgreich versandt")
        } catch (e: Exception) {
            Log.d("repo", "Beim Hochladen der Nachricht ist etwas schief gelaufen: $e")
        }
    }

    suspend fun getBattles(playerUid: String, context: Context) {
        gameDatabase.collection("battles").document(playerUid).collection("battlelist")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.d("repo", "could not load battlelist or battlelist is empty")
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val battlesFromFirebase = mutableListOf<Battle>()
                    snapshot.documents.forEach { battle ->
                        battlesFromFirebase.add(Battle(battle.data as Map<String, Any>, context))
                        _battles.value = battlesFromFirebase
                    }
                }
            }
    }

    suspend fun pushBattle(battle: Battle) {
        try {
            gameDatabase.collection("battles").document(battle.playerOne).collection("battlelist")
                .document(battle.id).set(battle.battleToMap()).await()
            if(battle.playerTwo!="multibattle")gameDatabase.collection("battles").document(battle.playerTwo).collection("battlelist")
                .document(battle.id).set(battle.battleToMap()).await()
            Log.d(TAG, "Spieler erfolgreich in die Firestore-Datenbank geschrieben")
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Schreiben des Battles in die Firestore-Datenbank", e)
        }
    }

    suspend fun observeChat(language: String) {
        gameDatabase.collection("chat").document("global-$language")
            .addSnapshotListener { newestMessage, error ->
                if (newestMessage != null && newestMessage.exists()) {

                    val chatItem = ChatItem(newestMessage.getData()!!.toMap())
                    _chat.value = _chat.value!!.plus(chatItem!!)
                }
            }
    }

    suspend fun getMultiBattle(context: Context): Battle? {

        var query =
            gameDatabase.collection("battles")
                .document("multibattle")
                .collection("battlelist")
                .orderBy("lastMove", Query.Direction.ASCENDING).limit(1)

        return try {
            var snapshot = query.get().await()
            if (snapshot != null && !snapshot.isEmpty) {
                var oldestBattle = snapshot.documents[0].data
                //TODO nach der Präsi noch die UID abfrgae einschieben und das Document nur löschen wenn es nicht der Uid des Spielers entspricht
                //im BattleList Fragment dann nur das weiterleiten wenn die PlayerTwoUid nicht der SpilerUId entspricht
                val battle = Battle(oldestBattle!!.toMap(), context)
                snapshot.documents[0].reference.delete().await()
                return battle
            } else null
        } catch (e: Exception) {
            Log.d("gdfs", "something went wrong while loading down multibattle: ${e.toString()}")
            null
        }
    }


}