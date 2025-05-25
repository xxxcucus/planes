package com.planes.android.video

import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.getString
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens

@Composable
fun VideoScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                navController: NavController, videoModelList: List<VideoModel>) {

    currentScreenState.value = PlanesScreens.Tutorials.name

    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        //TODO: if horizontal place a lazy list near the video
        Column() {
            VideoPlayer()
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(items = videoModelList) { entry ->
                    //TODO: use a card instead of Box
                    Box() {
                        Text(text = entry.getVideoName())
                    }
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer() {
    // This is the official way to access current context from Composable functions
    val context = LocalContext.current

    // Do not recreate the player everytime this Composable commits
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context, DefaultHttpDataSource.Factory())
            val uriSource = Uri.parse(
                "android.resource://"
                        + context.packageName + "/" + R.raw.guessing)
            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(
                    MediaItem.fromUri(uriSource))
            this.setMediaSource(source)
            this.prepare()
        }
    }

    AndroidView(
        factory = { ctxt ->
            PlayerView(ctxt).apply {
                player = exoPlayer
            }
        }
    )
}