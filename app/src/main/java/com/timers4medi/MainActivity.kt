package com.timers4medi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.timers4medi.extensions.round
import com.timers4medi.ui.theme.Timers4MediTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Timers4MediTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CircleTimer(
                        time = 1f,
                        size = 132.dp,
                        callback = { println("KONIEC COUNT") },
                    )
                }
            }
        }
    }
}

@Composable
fun CircleTimer(
    time: Float,
    size: Dp,
    callback: () -> Unit,
) {

    var isRunning by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0.0f) }
    var displayedTimerText by remember { mutableStateOf(time.toString()) }
    val color = remember { Animatable(Color.Gray) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    ).value

    LaunchedEffect(key1 = isRunning) {
        while (progress < time && isRunning) {
            delay(0.1.seconds)
            progress += 0.01f
            displayedTimerText = (time - progress).round(2).toString()
        }
        callback()
    }

    //todo sync color animation with timer
    LaunchedEffect(key1 = isRunning ) {
        while (isRunning) {
            color.animateTo(Color.Red, animationSpec = tween((time * 10000).roundToInt()))
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .wrapContentSize()
            .clip(shape = CircleShape)
            .background(color.value)
            .clickable { isRunning = !isRunning }
    ) {
        CircularProgressIndicator(
            progress = time,
            color = Color.Blue,
            strokeWidth = size/20,
            modifier = Modifier.size(size),
        )
        CircularProgressIndicator(
            progress = animatedProgress,
            color = Color.Red,
            strokeWidth = size/20,
            modifier =  Modifier.size(size),
        )

        Text(
            text = displayedTimerText
        )
    }

//    Box {
//        CircularProgressIndicator(progress = 1f, color = Color.Blue, strokeWidth = 2.dp)
//        CircularProgressIndicator(progress = 0.2f, color = Color.Red, strokeWidth = 2.dp)
//
//        Text(text = "1:00")
//    }

}

@Composable
fun CircularProgressIndicatorSample() {
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    ).value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(30.dp))
        Text("CircularProgressIndicator with undefined progress")
        CircularProgressIndicator()
        Spacer(Modifier.height(30.dp))
        Text("CircularProgressIndicator with progress set by buttons")
        CircularProgressIndicator(progress = animatedProgress)
        Spacer(Modifier.height(30.dp))
        OutlinedButton(
            onClick = {
                if (progress < 1f) progress += 0.1f
            }
        ) {
            Text("Increase")
        }

        OutlinedButton(
            onClick = {
                if (progress > 0f) progress -= 0.1f
            }
        ) {
            Text("Decrease")
        }
    }
}