package com.planes.android.screens.video

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.planes.android.R

interface VideoModelRepositoryInterface {

    fun create(context: Context)

    fun getPlayList(): List<VideoModel>

    fun setResumePosition(videoId: Int, position: Long)

    fun getResumePosition(videoId: Int): Long
}