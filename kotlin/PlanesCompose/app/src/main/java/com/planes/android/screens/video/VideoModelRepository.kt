package com.planes.android.screens.video

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.planes.android.R

class VideoModelRepository : VideoModelRepositoryInterface {

    var m_PlayList = mutableListOf<VideoModel>()

    override fun create(context: Context) {

        //TODO: translations
        val guessingDescription = "Explains how to guess the enemy plane positions"
        val positioningDescription = "Explains how to position your planes"
        val singlePlayerDescription = "Shows how to play the single player game"
        val multiPlayerDescription = "Shows how to play the multi player game"

        val guessingVideoModel = VideoModel(getString(context, R.string.guessingplanestutorial), guessingDescription,"https://youtu.be/CAxSPp2h_Vo")
        val positioningVideoModel = VideoModel(getString(context, R.string.positioningplanestutorial), positioningDescription, "https://youtu.be/qgL0RdwqBRY")
        val singlePlayerVideoModel = VideoModel(getString(context, R.string.singleplayertutorial), singlePlayerDescription, "https://youtu.be/N2Cg8eflCxM")
        val multiPlayerVideoModel = VideoModel(getString(context, R.string.multiplayertutorial),multiPlayerDescription, "https://youtu.be/mlSvZREBTwA")

        m_PlayList = mutableListOf(guessingVideoModel, positioningVideoModel, singlePlayerVideoModel, multiPlayerVideoModel)
    }

    override fun getPlayList(): List<VideoModel> {
        return m_PlayList.toList()
    }


}