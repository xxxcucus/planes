package com.planes.android.screens.video

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoViewModel  @Inject constructor(videoRepository: VideoModelRepositoryInterface): ViewModel() {

    val m_PlayerState = mutableStateOf<ExoPlayer?>(null)
    val m_VideoModelRepositoryState = mutableStateOf<VideoModelRepositoryInterface?>(videoRepository)
    val m_CurrentVideoIdState = mutableStateOf<Int?>(null)

    @OptIn(UnstableApi::class)
    fun setPlayerState(context: Context) {
        if (m_PlayerState.value != null)
           return

        if (m_VideoModelRepositoryState.value == null)
            return

        val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context, DefaultHttpDataSource.Factory())

        val mediaItems = m_VideoModelRepositoryState.value!!.getPlayList().mapIndexed { idx, videoItem ->
            MediaItem.Builder().setUri(("android.resource://" + context.packageName + "/" + videoItem.getVideoId()).toUri()).setMediaId("Video_$idx").setTag("Video_$idx").build()
        }

        m_PlayerState.value =
            ExoPlayer.Builder(context)
                .setMediaSourceFactory(ProgressiveMediaSource.Factory(dataSourceFactory))
                .build().apply {
                    setMediaItems(mediaItems)
                    prepare()
                    playWhenReady = true
                }

    }

    fun pausePlayer() {
        m_VideoModelRepositoryState.value?.setResumePosition(m_CurrentVideoIdState.value!!, m_PlayerState.value!!.currentPosition)
        m_PlayerState.value?.stop()
    }

    fun resumePlayer() {
        m_PlayerState.value?.play()
        val resumePosition = m_VideoModelRepositoryState.value!!.getResumePosition(m_CurrentVideoIdState.value!!)
        Log.d("Planes", "Tutorials go to $resumePosition")
        m_PlayerState.value?.seekTo(resumePosition)
    }

    fun getPlayerState(): ExoPlayer? {
        return m_PlayerState.value
    }

    fun isVideoRepositorySet(): Boolean {
        return m_VideoModelRepositoryState.value != null
    }

    fun getVideoModelList() : List<VideoModel> {
        if (m_VideoModelRepositoryState.value == null)
            return emptyList()

        return m_VideoModelRepositoryState.value!!.getPlayList()
    }

    fun setCurrentVideoId(videoId: Int) {
        m_CurrentVideoIdState.value = videoId
    }

}