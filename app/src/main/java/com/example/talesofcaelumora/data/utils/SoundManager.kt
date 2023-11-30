package com.example.talesofcaelumora.data.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.data.musicVolume
import com.example.talesofcaelumora.data.songList
import com.example.talesofcaelumora.data.songTitle
import com.example.talesofcaelumora.data.vfxVolume

class SoundManager private constructor(val context: Context) {

    private lateinit var soundPool: SoundPool
    private val soundMap: MutableMap<Int, Int> = HashMap()
    lateinit var radio: MediaPlayer
    var song = ""

    init {

        createNewSoundPool()
        createMediaPlayer()
        loadSound(R.raw.hit)
        loadSound(R.raw.card_flip)
        loadSound(R.raw.sort_cards)
        loadSound(R.raw.wooden_wheel)
        loadSound(R.raw.snap_in)
        loadSound(R.raw.rusty_wheel)
        loadSound(R.raw.waterdrop)
        loadSound(R.raw.button_click)
        loadSound(R.raw.message_in)
        loadSound(R.raw.wooden_wheel_reversd)
        loadSound(R.raw.typewriter)
    }



    companion object {
        private var instance: SoundManager? = null

        fun getInstance(context: Context): SoundManager {
            if (instance == null) {
                instance = SoundManager(context.applicationContext)
            }
            return instance!!
        }
    }

    private fun createMediaPlayer() {
        //instanziiert den ersten MediaPLayer
        radio = MediaPlayer.create(context.applicationContext, R.raw.main_theme)
        radio.setVolume(musicVolume, musicVolume)
    }

    private fun createNewSoundPool() {
        //instanziiert den ersten SoundPool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    private fun loadSound(soundResource: Int) {
        val soundId = soundPool.load(context, soundResource, 1)
        soundMap[soundResource] = soundId
    }

    fun playSound(soundResource: Int) {
        val soundId = soundMap[soundResource] ?: return
        soundPool.play(soundId, vfxVolume, vfxVolume, 1, 0, 1.0f)
    }


    fun setVolume(){
        soundPool.setVolume(0,vfxVolume, vfxVolume)
    }
    fun setRadioVolume(){
        radio.setVolume(musicVolume, musicVolume)
    }
    fun pause(){
        radio.pause()
    }
    fun resume(){
        radio.start()
    }
    fun changeSong(){

        radio.release()
        var random = songList.indices.random()
        radio = MediaPlayer.create(context, songList[random])
        radio.setVolume(musicVolume, musicVolume)
        radio.setOnCompletionListener {
            changeSong()
        }
        radio.start()
        song = songTitle[random]
    }
    fun startRadio(songId:Int){
        radio.stop()
        radio.release()
        radio = MediaPlayer.create(context.applicationContext, songId)
        radio.setVolume(musicVolume, musicVolume)
        radio.setOnCompletionListener {
            changeSong()
        }
        radio.start()
    }

    fun release() {
        soundPool.release()
        radio.stop()
        radio.release()
        instance = null
    }
}
