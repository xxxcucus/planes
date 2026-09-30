package com.planes.android.screens.video

import android.content.Context

interface VideoModelRepositoryInterface {

    fun create(context: Context)

    fun getPlayList(): List<VideoModel>

}