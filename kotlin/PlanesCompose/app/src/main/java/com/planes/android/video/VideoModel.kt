package com.planes.android.video


class VideoModel(videoname: String, videoId: Int, videoDuration: String, currentPosition: Int,
                 videoRatio: Float, youtubeLink: String) {

    private var m_VideoName: String
    private var m_VideoId: Int
    private var m_VideoRatio: Float
    private var m_VideoDuration: String
    private var m_CurrentPosition: Int
    private var m_YoutubeLink: String

    init {
        m_VideoName = videoname
        m_VideoId = videoId
        m_VideoRatio = videoRatio
        m_VideoDuration = videoDuration
        m_CurrentPosition = currentPosition
        m_YoutubeLink = youtubeLink
    }

    fun getVideoName(): String {
        return m_VideoName
    }

    fun getVideoId(): Int {
        return m_VideoId
    }

    fun getVideoDuration(): String {
        return m_VideoDuration
    }

    fun getCurrentPosition(): Int {
        return m_CurrentPosition
    }

    fun getYoutubeLink(): String {
        return m_YoutubeLink
    }

    fun setCurrentPosition(position: Int) {
        m_CurrentPosition = position
    }
}