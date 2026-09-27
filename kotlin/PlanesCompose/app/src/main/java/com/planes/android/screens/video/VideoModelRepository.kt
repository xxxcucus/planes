package com.planes.android.screens.video

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.planes.android.R

class VideoModelRepository : VideoModelRepositoryInterface {

    var m_PlayList = mutableListOf<VideoModel>()

    override fun create(context: Context) {

        val guessingDescription = "Explains how to guess the enemy plane positions"
        val positioningDescription = "Explains how to position your planes"
        val singlePlayerDescription = "Shows how to play the single player game"
        val multiPlayerDescription = "Shows how to play the multi player game"

        val guessingVideoModel = VideoModel(getString(context, R.string.guessingplanestutorial), R.raw.guessing, guessingDescription, "00:01:49",
            0,1.42f, "https://youtu.be/CAxSPp2h_Vo")
        val positioningVideoModel = VideoModel(getString(context, R.string.positioningplanestutorial), R.raw.positioning, positioningDescription, "00:01:22",
            0,1.42f, "https://youtu.be/qgL0RdwqBRY")
        val singlePlayerVideoModel = VideoModel(getString(context, R.string.singleplayertutorial), R.raw.singleplayer, singlePlayerDescription, "00:02:00",
            0, 1.36f, "https://youtu.be/N2Cg8eflCxM")
        val multiPlayerVideoModel = VideoModel(getString(context, R.string.multiplayertutorial), R.raw.multiplayer_android,
            multiPlayerDescription, "00:05:34",
            0,1.77f, "https://youtu.be/mlSvZREBTwA")

        m_PlayList = mutableListOf(guessingVideoModel, positioningVideoModel, singlePlayerVideoModel, multiPlayerVideoModel)
    }

    override fun getPlayList(): List<VideoModel> {
        return m_PlayList.toList()
    }

    override fun setResumePosition(videoId: Int, position: Long) {
        val videoModel = m_PlayList.find { it.getVideoId() == videoId }
        if (videoModel == null)
            return
        videoModel.setCurrentPosition(position)
    }

    override fun getResumePosition(videoId: Int): Long {
        val videoModel = m_PlayList.find { it.getVideoId() == videoId }
        if (videoModel == null)
            return 0L
        return videoModel.getCurrentPosition()
    }

}