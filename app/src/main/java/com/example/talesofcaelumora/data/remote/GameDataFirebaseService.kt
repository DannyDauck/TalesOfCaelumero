package com.example.talesofcaelumora.data.remote

import android.util.Log
import com.example.talesofcaelumora.data.datamodel.Card
import com.example.talesofcaelumora.data.datamodel.Player
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val TAG = "GameDataApiDService"

class GameDataFirebaseService() {

    private val gameDatabase = FirebaseFirestore.getInstance()
    var progress = 0
    var loaded = 0

    suspend fun getAllCards(setProgress: (Int,Int)->Unit): List<Card> {
        return try {
            val snapshot = gameDatabase.collection("cards").get().await()

            var list = mutableListOf<Card>()
            progress = snapshot.documents.size
            snapshot.documents.forEach { doc ->
                val cardData = doc.getData()

                if (cardData != null) try{
                    cardData["id"] = doc.id
                    list.add(Card(cardData.toMap()))
                    loaded++
                    setProgress(loaded,progress)
                }catch(e: java.lang.Exception){
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
    fun resetProgress(){
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

}