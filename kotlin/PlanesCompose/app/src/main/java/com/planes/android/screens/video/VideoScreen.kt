package com.planes.android.screens.video

import android.content.res.Configuration
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import androidx.core.net.toUri

@Composable
fun VideoScreen(modifier: Modifier, currentTitleState: MutableState<String>,
                currentScreenState: MutableState<String>,
                showPopupState: MutableState<Boolean>,
                videoId: Int,
                viewModel: VideoViewModel = hiltViewModel()) {

    currentTitleState.value = stringResource(R.string.videos)
    currentScreenState.value = PlanesScreens.Tutorials.name
    showPopupState.value = false

    val configuration = LocalConfiguration.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val containerSize = LocalWindowInfo.current.containerSize
    val screenWidthDp = with(LocalDensity.current) { containerSize.width.toDp() }
    val buttonWidth = screenWidthDp / 3

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> viewModel.pausePlayer()
                Lifecycle.Event.ON_RESUME -> viewModel.resumePlayer()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val currentVideoState = rememberSaveable {
        mutableIntStateOf(videoId)
    }

    val context = LocalContext.current
    Log.d("Planes", "Video: Repository set ${viewModel.isVideoRepositorySet()}")
    viewModel.setPlayerState(context)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {

                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    VideoPlayer(currentVideoState.intValue, viewModel)

                    Box(
                        modifier = Modifier
                            .weight(1f)              // Take all remaining space
                            .fillMaxWidth(0.83f).
                        padding(start = buttonWidth / 2),
                        contentAlignment = Alignment.Center,
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(1.dp),
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            contentPadding = PaddingValues(1.dp)
                        ) {
                            items(items = viewModel.getVideoModelList()) { entry ->
                                VideoButton(
                                    entry,
                                    currentVideoState,
                                    modifier = Modifier.width(buttonWidth).
                                    height(100.dp)
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                Row {
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(1.dp),
                            contentPadding = PaddingValues(
                                top = 1.dp,
                                bottom = 1.dp
                            )
                        ) {
                            items(items = viewModel.getVideoModelList()) { entry ->
                                VideoButton(entry, currentVideoState, Modifier.width(200.dp).height(100.dp))
                            }
                        }
                    }

                    VideoPlayer(currentVideoState.intValue, viewModel)
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoId : Int, viewModel: VideoViewModel) {

    viewModel.setCurrentVideoId(videoId)

    AndroidView(
        factory = { ctxt ->
            PlayerView(ctxt).apply {
                player = viewModel.getPlayerState()
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        },
        update = { pview ->
            pview.apply {
                val player = this.player!!
                //player.stop()
                val uriSource = ("android.resource://"
                        + context.packageName + "/" + videoId).toUri()
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