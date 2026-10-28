package com.planes.android.screens.singleplayergame

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import com.planes.singleplayerengine.GuessPoint
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GridSquareGame(
    isComputer: Boolean,
    annotation: Int,
    guess: GuessPoint?,
    widthDp: Dp,
    backgroundColor: Color,
    index: Int,
    onClick: (Int) -> Unit
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
        .scale(scale.value).clickable {
        onClick.invoke(index)
    }) {

        val planeOverlapColor = Color.Red
        var squareColor = backgroundColor
        val cockpitColor = Color.Blue
        val firstPlaneColor = Color(80, 80, 80)
        val secondPlaneColor = Color(120, 120, 120)
        val thirdPlaneColor = Color(160, 160, 160)

        //Log.d("Planes", "Annotation $annotation")
        if (!isComputer) {
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
        } else {
            if (guess != null && annotation == -2) {
                squareColor = cockpitColor
            }
        }

        if (isComputer) {
            drawNonPlaneBoardSquareBackground(size.width, size.height, squareColor, this)
        } else {
            if (annotation != 0) {
                drawPlaneBoardSquareBackground(size.width, size.height, squareColor, this)
            } else {
                drawNonPlaneBoardSquareBackground(size.width, size.height, squareColor, this)
            }
        }

        if (guess != null) {
            drawPlaneBoardSquareGuess(size.width, size.height, guess, this)
        }
    }


}

fun drawPlaneBoardSquareBackground(dimX: Float, dimY: Float,
                                   squareColor: Color,
                                   drawScope: DrawScope) {
    drawScope.drawRect(brush = Brush.linearGradient(
        colors = listOf(squareColor, Color(0xFF81C784)), // dark to light green
        start = Offset.Zero,
        end = Offset(dimX, dimY)
    ), style = Fill)
}

fun drawNonPlaneBoardSquareBackground(dimX: Float, dimY: Float,
                                   squareColor: Color,
                                   drawScope: DrawScope) {
    drawScope.drawRect(color = squareColor,
        topLeft = Offset.Zero,
        size = Size(dimX, dimY),
        style = Fill
    )

    drawScope.drawRect(color = Color.Red,
        topLeft = Offset.Zero,
        size = Size(dimX, dimY),
        style = Stroke(width = 4f)
    )
}

fun drawPlaneBoardSquareGuess(dimX: Float, dimY: Float, guess: GuessPoint, drawScope: DrawScope) {
    if (guess.isMiss) {
        drawScope.drawOval(color = Color.Red,
            topLeft = Offset(dimX / 4, dimY / 4),
            size = Size(dimX / 2, dimY / 2),
            style = Fill)
    }

    if (guess.isHit) {
        val path = Path().apply {
            moveTo(0f, dimY / 2)
            lineTo(dimX / 2, 0f)
            lineTo(dimX, dimY / 2)
            lineTo(dimX / 2, dimY)
            lineTo(0f, dimY / 2)
            close()
        }
        drawScope.drawPath(
            path = path,
            color = Color.Red,
            style = Fill
        )
    }

    if (guess.isDead) {
        drawScope.drawLine(color = Color.Red,
            start = Offset (0f, 0f),
            end = Offset(dimX, dimY),
            strokeWidth = 4f)
        drawScope.drawLine(color = Color.Red,
            start = Offset(0f, dimY),
            end = Offset(dimX, 0f),
            strokeWidth = 4f)
    }
}

