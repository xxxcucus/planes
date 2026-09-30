package com.planes.android.screens.video


class VideoModel(videoName: String, videoDescription: String, youtubeLink: String) {

    private var m_VideoName: String = videoName
    //private var m_VideoId: Int = videoId
    //private var m_VideoRatio: Float = videoRatio
    //private var m_VideoDuration: String = videoDuration
    //private var m_CurrentPosition: Long = currentPosition
    private var m_YoutubeLink: String = youtubeLink

    private var m_VideoDescription: String = videoDescription

    fun getVideoName(): String {
        return m_VideoName
    }

    fun getVideoDescription(): String {
        return m_VideoDescription
    }

    fun getYoutubeLink(): String {
        return m_YoutubeLink
    }
}