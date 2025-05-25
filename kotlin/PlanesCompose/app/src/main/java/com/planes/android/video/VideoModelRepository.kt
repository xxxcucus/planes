package com.planes.android.video

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.planes.android.R
import com.planes.android.about.AboutEntryModel

class VideoModelRepository {
    companion object VideoModelRepository {
        fun create(context: Context): List<VideoModel> {
            val videoModel_guessing = VideoModel(getString(context, R.string.guessingplanestutorial), R.raw.guessing, "00:01:49",
                0,1.42f, "https://youtu.be/CAxSPp2h_Vo")
            val videoModel_positioning = VideoModel(getString(context, R.string.positioningplanestutorial), R.raw.positioning, "00:01:22",
                0,1.42f, "https://youtu.be/qgL0RdwqBRY")
            val videoModel_single = VideoModel(getString(context, R.string.singleplayertutorial), R.raw.singleplayer, "00:02:00",
                0, 1.36f, "https://youtu.be/N2Cg8eflCxM")
            val videoModel_multi = VideoModel(getString(context, R.string.multiplayertutorial), R.raw.multiplayer_android, "00:05:34",
                0,1.77f, "https://youtu.be/mlSvZREBTwA")

            return listOf(videoModel_guessing, videoModel_positioning, videoModel_single, videoModel_multi)
        }
    }
}