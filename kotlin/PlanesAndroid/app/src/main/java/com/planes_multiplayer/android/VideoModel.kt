package com.planes_multiplayer.android

class VideoModel(videoname: String, videoId: Int, videoDuration: String, videoRatio: Float) {

    private var m_VideoName: String
    private var m_VideoId: Int
    private var m_VideoRatio: Float
    private var m_VideoDuration: String

    init {
        m_VideoName = videoname
        m_VideoId = videoId
        m_VideoRatio = videoRatio
        m_VideoDuration = videoDuration
    }

    fun getVideoName(): String {
        return m_VideoName
    }

    fun getVideoId(): Int {
        return m_VideoId
    }

    fun getVideoRatio(): Float {
        return m_VideoRatio
    }

    fun getVideoDuration(): String {
        return m_VideoDuration
    }
}