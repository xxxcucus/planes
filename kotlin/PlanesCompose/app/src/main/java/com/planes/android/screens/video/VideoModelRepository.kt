package com.planes.android.screens.video

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.planes.android.R

class VideoModelRepository : VideoModelRepositoryInterface {

    var m_PlayList = mutableListOf<VideoModel>()

    override fun create(context: Context) {
        val videoModel_guessing = VideoModel(getString(context, R.string.guessingplanestutorial), R.raw.guessing, "00:01:49",
            0,1.42f, "https://youtu.be/CAxSPp2h_Vo")
        val videoModel_positioning = VideoModel(getString(context, R.string.positioningplanestutorial), R.raw.positioning, "00:01:22",
            0,1.42f, "https://youtu.be/qgL0RdwqBRY")
        val videoModel_single = VideoModel(getString(context, R.string.singleplayertutorial), R.raw.singleplayer, "00:02:00",
            0, 1.36f, "https://youtu.be/N2Cg8eflCxM")
        val videoModel_multi = VideoModel(getString(context, R.string.multiplayertutorial), R.raw.multiplayer_android, "00:05:34",
            0,1.77f, "https://youtu.be/mlSvZREBTwA")

        m_PlayList = mutableListOf(videoModel_guessing, videoModel_positioning, videoModel_single, videoModel_multi)
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