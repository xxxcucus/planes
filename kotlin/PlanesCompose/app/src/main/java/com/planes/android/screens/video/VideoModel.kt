package com.planes.android.screens.video


class VideoModel(videoName: String, videoId: Int, videoDuration: String, currentPosition: Long,
                 videoRatio: Float, youtubeLink: String) {

    private var m_VideoName: String = videoName
    private var m_VideoId: Int = videoId
    private var m_VideoRatio: Float = videoRatio
    private var m_VideoDuration: String = videoDuration
    private var m_CurrentPosition: Long = currentPosition
    private var m_YoutubeLink: String = youtubeLink

    fun getVideoName(): String {
        return m_VideoName
    }

    fun getVideoId(): Int {
        return m_VideoId
    }

    fun getVideoDuration(): String {
        return m_VideoDuration
    }

    fun getCurrentPosition(): Long {
        return m_CurrentPosition
    }

    fun getYoutubeLink(): String {
        return m_YoutubeLink
    }

    fun setCurrentPosition(position: Long) {
        m_CurrentPosition = position
    }
}