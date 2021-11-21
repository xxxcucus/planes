package com.planes_multiplayer.android

import android.content.Context
import android.os.Bundle
import com.google.gson.Gson

class VideoSettingsService internal constructor(private val m_Context: Context) {
    var currentVideo: Int = 0
    lateinit var videoPlaybackPositions: IntArray

    fun readPreferences() {
        val gson = Gson()
        val sp = m_Context.getSharedPreferences("videosettings", Context.MODE_PRIVATE)
        currentVideo = sp.getInt("currentVideo", 0)

        var videoPlaybackPositionsJson = sp.getString("videoPlaybackPositions", "")
        if (!videoPlaybackPositionsJson.isNullOrEmpty())
            videoPlaybackPositions = gson.fromJson(videoPlaybackPositionsJson, IntArray::class.java)
        else
            videoPlaybackPositions = IntArray(4){ 0 }
    }

    fun readFromSavedInstanceState(savedInstanceState: Bundle) {
        currentVideo = savedInstanceState.getInt("videosettings/currentVideo")
        videoPlaybackPositions = savedInstanceState.getSerializable("videosettings/videoPlaybackPositions") as IntArray
    }

    fun writePreferences() {
        val gson = Gson()
        val sp = m_Context.getSharedPreferences("videosettings", Context.MODE_PRIVATE).edit()
        sp.putInt("currentVideo", currentVideo)
        sp.putString("videoPlaybackPositions", gson.toJson(videoPlaybackPositions))
        sp.commit()
    }

    fun writeToSavedInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt("videosettings/currentVideo", currentVideo)
        savedInstanceState.putSerializable("videosettings/videoPlaybackPositions", videoPlaybackPositions)
    }
}