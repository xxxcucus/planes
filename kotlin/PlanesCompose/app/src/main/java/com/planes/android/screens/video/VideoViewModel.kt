package com.planes.android.screens.video

import android.content.Context
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
import com.planes.android.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class VideoViewModel  @Inject constructor(): ViewModel() {

    val m_PlayerState = mutableStateOf<ExoPlayer?>(null)
    val m_VideoModelRepositoryState = mutableStateOf<VideoModelRepository?>(null)

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

        // Create hashmap with video items to persist current playing position when shifting between videos
        /*mediaItems.forEach {
            hashMapVideoStates[it.mediaId] = VideoItem()
        }*/

        // Create the player instance and update it to UI via stateFlow
        m_PlayerState.value =
            ExoPlayer.Builder(context)
                .setMediaSourceFactory(ProgressiveMediaSource.Factory(dataSourceFactory))
                .build().apply {
                    setMediaItems(mediaItems)
                    prepare()
                    playWhenReady = true
                }

    }

    fun getPlayerState(): ExoPlayer? {
        return m_PlayerState.value
    }

    fun setVideoRepository(context: Context) {
        if (m_VideoModelRepositoryState.value != null)
            return

        val repository = VideoModelRepository()
        repository.create(context)

        m_VideoModelRepositoryState.value = repository
    }

    fun getVideoModelList() : List<VideoModel> {
        if (m_VideoModelRepositoryState.value == null)
            return emptyList()

        return m_VideoModelRepositoryState.value!!.getPlayList()
    }

}