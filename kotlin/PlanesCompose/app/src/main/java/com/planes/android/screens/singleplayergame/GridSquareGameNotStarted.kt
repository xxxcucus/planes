package com.planes.android.screens.singleplayergame

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.planes.singleplayerengine.GuessPoint
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GridSquareGameNotStarted(
    annotation: Int,
    guess: GuessPoint?,
    widthDp: Dp,
    backgroundColor: Color
) {

    val scale = remember {
        Animatable(1f)
    }

    LaunchedEffect(key1 = guess == null || !guess.isDead ) {
        if (guess != null && guess.isDead) {
            scale.animateTo(
                targetValue = (1.0 - scale.value).toFloat(),
                animationSpec = tween(
                    durationMillis = 800,
                    easing = {
                        OvershootInterpolator(1f).getInterpolation(it)
                    })
            )
            scale.animateTo(
                targetValue = (1.0 - scale.value).toFloat(),
                animationSpec = tween(
                    durationMillis = 800,
                    easing = {
                        OvershootInterpolator(1f).getInterpolation(it)
                    })
            )
        }

        delay(2000L.milliseconds)
    }
    Canvas(modifier = Modifier.width(widthDp).height(widthDp)
        .background(backgroundColor)
        .scale(scale.value)) {

        val planeOverlapColor = Color.Red
        var squareColor = backgroundColor
        val cockpitColor = Color.Blue
        val firstPlaneColor = Color(80, 80, 80)
        val secondPlaneColor = Color(120, 120, 120)
        val thirdPlaneColor = Color(160, 160, 160)

        when (annotation) {
            -1 -> {
                squareColor = planeOverlapColor
            }
            -2 -> {
                squareColor = cockpitColor
            }
            1 -> {
                squareColor = firstPlaneColor
            }
            2 -> {
                squareColor = secondPlaneColor
            }
            3 -> {
                squareColor = thirdPlaneColor
            }
        }

        if (annotation != 0)
            drawPlaneBoardSquareBackground(size.width, squareColor, this)
        else {
            drawNonPlaneBoardSquareBackground(size.width, squareColor, this)
        }

        if (guess != null) {
            drawPlaneBoardSquareGuess(size.width, guess, this)
        }
    }
}