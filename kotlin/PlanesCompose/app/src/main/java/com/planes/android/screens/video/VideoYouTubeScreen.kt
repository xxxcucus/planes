package com.planes.android.screens.video

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens

@Composable
fun VideoYouTubeScreen(modifier: Modifier, currentTitleState: MutableState<String>,
                currentScreenState: MutableState<String>,
                showPopupState: MutableState<Boolean>,
                videoModelRepository: VideoModelRepositoryInterface
) {

    currentTitleState.value = stringResource(R.string.videos)
    currentScreenState.value = PlanesScreens.Tutorials.name
    showPopupState.value = false

    val videoEntryList: List<VideoModel> = videoModelRepository.getPlayList()
    
    val configuration = LocalConfiguration.current
    val containerSize = LocalWindowInfo.current.containerSize
    val screenWidthDp = with(LocalDensity.current) { containerSize.width.toDp() }
    val buttonWidth = screenWidthDp / 3

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {

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
                        items(items = videoEntryList) { entry ->
                            VideoYouTubeButton(
                                entry,
                                modifier = Modifier.width(buttonWidth).
                                height(100.dp)
                            )
                        }
                    }
                }
            }
    }

}