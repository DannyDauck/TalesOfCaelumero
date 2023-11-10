package com.example.talesofcaelumora.data.remote

import android.util.Log
import com.example.talesofcaelumora.data.datamodel.Card
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val TAG = "GameDataApiDService"

class GameDataApiService {

    private val gameDatabase = FirebaseFirestore.getInstance()

    suspend fun getAllCards(): List<Card> {
        return try {
            val snapshot = gameDatabase.collection("cards").get().await()

            var list = mutableListOf<Card>()
            snapshot.documents.forEach { doc ->
                val cardData = doc.getData()

                if (cardData != null) try{
                    cardData["id"] = doc.id
                    list.add(Card(cardData.toMap()))
                }catch(e: java.lang.Exception){
                    Log.e(TAG, "Fehler beim Parsen der Karte ${doc.id}")
                }
            }
            list
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Abrufen aller Karten", e)
            emptyList()
        }

    }

}