package com.example.talesofcaelumora.data.remote

import android.util.Log
import com.example.talesofcaelumora.data.datamodel.Card
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val TAG = "GameDataApiDService"
class GameDataApiService {

    private val gameDatabase = FirebaseFirestore.getInstance()

    suspend fun getAllCards(): List<Card> {
        try {
            val snapshot = gameDatabase.collection("/cards").get().await()

            //mapNotNull überspringt im Gegensatz zu map alle Karten bei denen das parsen nicht funktioniert

            return snapshot.documents.mapNotNull {
                //Beim abspeichern vorher kann ich ein Log ausgeben,
                //welche Karte nicht geparsed werden konnte
                val card = it.toObject(Card::class.java)
                if (card != null) {
                    card.copy(id = it.id)
                } else {
                    Log.e(TAG, "Karte konnte nicht geparsed werden: ${it.id}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Abrufen aller Karten", e)
            return emptyList()
        }
    }

}