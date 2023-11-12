package com.example.talesofcaelumora.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.talesofcaelumora.data.datamodel.Card
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

private const val TAG = "GameDataApiDService"

class GameDataApiService() {

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
        progress = 0
        loaded = 0

    }

}