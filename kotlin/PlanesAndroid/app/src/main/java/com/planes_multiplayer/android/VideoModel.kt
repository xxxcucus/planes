package com.planes_multiplayer.android

class VideoModel(videoname: String, videoId: Int) {

    private var m_VideoName: String
    private var m_VideoId: Int

    init {
        m_VideoName = videoname
        m_VideoId = videoId
    }

    fun getVideoName(): String {
        return m_VideoName
    }

    fun getVideoId(): Int {
        return m_VideoId
    }
}