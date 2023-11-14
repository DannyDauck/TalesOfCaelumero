package com.example.talesofcaelumora.data.utils

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.Executors


class ImageLoader(private val context: Context) {

    private val glide: RequestManager by lazy {
        Glide.with(context)
    }

    fun loadIfNotExist(cardId: String, path: String) {
        val imageFile = getImageFile(cardId)

        if (!imageFile.exists()) {
            val directory = File(context.filesDir, "images")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            Log.d("ImageLoader", "$cardId wird heruntergeladen")

            // Das Bild existiert nicht im internen Speicher, lade es und speichere es dort
            Executors.newSingleThreadExecutor().submit {
                try {
                    val downloadedFile = glide.asFile()
                        .load(path)
                        .submit()
                        .get()

                    copyFileAsync(downloadedFile, imageFile)
                    Log.d("ImageLoader", "Successful loaded")
                } catch (e: Exception) {
                    Log.e("ImageLoader", "Fehler beim Herunterladen oder Kopieren der Datei", e)
                }
            }
        } else {
            Log.d("ImageLoader", "Karte existiert bereits")
        }
    }

    private fun getImageFile(cardId: String): File {
        return File(context.filesDir, "images/$cardId.jpeg")
    }

    private fun copyFileAsync(sourceFile: File, destinationFile: File) {
        try {
            sourceFile.inputStream().use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            Log.e("ImageLoader", "Fehler beim Kopieren der Datei", e)
        }
    }
}