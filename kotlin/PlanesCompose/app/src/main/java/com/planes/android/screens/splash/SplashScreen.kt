package com.planes.android.screens.splash

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import com.planes.android.navigation.PlanesScreens
import kotlinx.coroutines.delay
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.planes.android.R


@Composable
fun SplashScreen(navController: NavController, splashScreenState: MutableState<Boolean>) {

    LaunchedEffect(Unit) {
        delay(2000)
        splashScreenState.value = false
        navController.navigate(PlanesScreens.Login.name)
    }

    val configuration = LocalConfiguration.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Image(
                painter = painterResource(id = R.drawable.feature_graphic_vertical),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.feature_graphic_horizontal),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp),
            color = Color.White,
            strokeWidth = 6.dp
        )
    }
}
