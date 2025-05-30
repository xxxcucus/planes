package com.planes.android.video

import android.content.res.Configuration
import android.net.Uri
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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

//TODO: stop video player when leaving screen. save state in view model
//TODO: when rotating screen stop player/ do not create another player

@Composable
fun VideoScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                navController: NavController, videoModelList: List<VideoModel>) {

    currentScreenState.value = PlanesScreens.Tutorials.name
    val configuration = LocalConfiguration.current
    val currentVideoState = remember {
        mutableStateOf(R.raw.guessing)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {

                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    VideoPlayer(currentVideoState.value)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp),
                        contentPadding = PaddingValues(1.dp)
                    ) {
                        items(items = videoModelList) { entry ->
                            VideoButton(entry, currentVideoState, Modifier.width(200.dp).height(100.dp))
                        }
                    }
                }
            }
            else -> {
                Row() {
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Spacer(modifier = Modifier)
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(1.dp),
                            contentPadding = PaddingValues(
                                top = 1.dp,
                                bottom = 1.dp
                            )
                        ) {
                            items(items = videoModelList) { entry ->
                                VideoButton(entry, currentVideoState, Modifier.width(200.dp).height(100.dp))
                            }
                        }
                    }

                    VideoPlayer(currentVideoState.value)
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoId : Int) {
    // This is the official way to access current context from Composable functions
    val context = LocalContext.current

    // Do not recreate the player everytime this Composable commits
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context, DefaultHttpDataSource.Factory())
            val uriSource = Uri.parse(
                "android.resource://"
                        + context.packageName + "/" + videoId)
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
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        },
        update = { pview ->
            pview.apply {
                val player = this.player!!
                val uriSource = Uri.parse(
                    "android.resource://"
                            + context.packageName + "/" + videoId)
                player.setMediaItem(MediaItem.fromUri(uriSource))
                player.prepare()
                //player.playWhenReady = true

            }
        },
        onReset = { pview ->
            pview.player = null
        }
    )
}