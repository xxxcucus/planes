package com.planes.android.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.video.VideoModelRepository
import com.planes.android.screens.video.VideoModelRepositoryInterface
import com.planes.android.utils.YouTubeUtils

@Composable
fun TextPopupWithButton(title: String, description: String, buttonText: String,
                        videoId: Int, navController: NavHostController,
                        videoModelRepository: VideoModelRepositoryInterface
) {
    val context = LocalContext.current

    Column( modifier = Modifier.wrapContentHeight().
    fillMaxWidth().padding(15.dp)) {
        Text(text = title,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(15.dp),
            style = MaterialTheme.typography.titleMedium)
        Text(text = description,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(15.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = {
                val videos = videoModelRepository.getPlayList()
                if (videoId < videos.size)
                    YouTubeUtils.playLinkInYoutube(videos[videoId].getYoutubeLink(), context)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = buttonText)
        }
    }
}