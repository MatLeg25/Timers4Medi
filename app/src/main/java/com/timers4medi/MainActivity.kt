package com.timers4medi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.hsv
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.timers4medi.ui.theme.Timers4MediTheme
import com.timers4medi.utils.countdownFormatter
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Timers4MediTheme {
                val timeInSec = 10
                var isRunning by remember { mutableStateOf(false) }
                val color = remember { Animatable(Color.Gray) }

                LaunchedEffect(key1 = isRunning ) {
                    while (isRunning) {
                        color.animateTo(
                            Color.Blue,
                            animationSpec = tween(
                                (timeInSec * 1000),500, EaseInCirc
                            )
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color.value)
                ) {
                    AnimatableCircle(isRunning)

                    CircleTimer(
                        timeInSec = timeInSec,
                        size = 132.dp,
                        isRunning = isRunning,
                        setIsRunning = {
                            isRunning = !isRunning
                        },
                        callback = { println("KONIEC COUNT") },
                    )

                }
            }
        }
    }
}

@Composable
fun CircleTimer(
    timeInSec: Int,
    size: Dp,
    isRunning: Boolean,
    setIsRunning: (Boolean) -> Unit,
    callback: () -> Unit,
) {

    val refreshFrequency = 0.1
    var progress by remember { mutableStateOf(0f) }
    var elapsedTime by remember { mutableStateOf(0f) }
    var displayedTimerText by remember { mutableStateOf(timeInSec.toString()) }

    LaunchedEffect(key1 = isRunning) {
        while (isRunning) {
            delay(refreshFrequency.seconds)
            progress += 0.01f
            elapsedTime += refreshFrequency.toFloat()
            val countdown = (timeInSec - elapsedTime)
            displayedTimerText = if (countdown <= 0) {
                setIsRunning(false)
                "00:00"
            } else {
                countdownFormatter(countdown.toLong())
            }
        }
        callback()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .wrapContentSize()
            .clip(shape = CircleShape)
            .clickable {
                if (!isRunning && elapsedTime.toInt() == timeInSec) {
                    progress = 0.0f
                    elapsedTime = 0.0f
                } else {
                    setIsRunning(!isRunning)
                }
            }
    ) {
        CircularProgressIndicator(
            progress = timeInSec.toFloat(),
            color = Color.Blue,
            strokeWidth = size/10,
            modifier = Modifier.size(size),
        )
        CircularProgressIndicator(
            progress = progress,
            color = hsv(120f - progress * 100, 1f, 1f,), //todo prevent more than 360
            strokeWidth = size/10,
            modifier =  Modifier.size(size),
        )

        Text(
            text = displayedTimerText
        )
    }

}

@Composable
fun AnimatableCircle(isRunning: Boolean) {
    var x by remember { mutableStateOf(1f) }

    LaunchedEffect(key1 = isRunning) {
        while (isRunning) {
            delay(100)
            println("timet $x")
            if (x>3f) x -= 1f
            else x += 0.1f
        }
    }

    key(x) {
        val largeRadialGradient = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val biggerDimension = maxOf(size.height, size.width)
                return RadialGradientShader(
                    colors = listOf(
                        Color(0xFF2be4dc),
                        Color(0xFF243484),
                    ),
                    center = size.center,
                    radius = biggerDimension / x,
                    colorStops = listOf(0f, 0.95f)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(400.dp)
                .aspectRatio(1f)
                .background(largeRadialGradient, shape = CircleShape)
        )


    }


}