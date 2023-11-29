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
import androidx.compose.foundation.layout.height
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
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CircleTimer(
                        timeInSec = 10,
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
    timeInSec: Int,
    size: Dp,
    callback: () -> Unit,
) {

    val refreshFrequency = 1
    val multiplier = 1/refreshFrequency
    val step = (timeInSec / refreshFrequency).toFloat()
    var isRunning by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var elapsedTime by remember { mutableStateOf(0f) }
    var displayedTimerText by remember { mutableStateOf(timeInSec.toString()) }
    val color = remember { Animatable(Color.Gray) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    ).value

    LaunchedEffect(key1 = isRunning) {
        while (isRunning) {
            delay(1.seconds)
            progress += 0.1f
            elapsedTime += 1
            val countdown = (timeInSec - elapsedTime).toLong()
            if (countdown <= 0) {
                isRunning = false
                displayedTimerText = "0:00 "
            } else {
                displayedTimerText = countdownFormatter(countdown)
            }
        }
        callback()
    }

    //todo sync color animation with timer
    LaunchedEffect(key1 = isRunning ) {
        while (isRunning) {
            color.animateTo(Color.Red, animationSpec = tween((timeInSec * 10000)))
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
            progress = timeInSec.toFloat(),
            color = Color.Blue,
            strokeWidth = size/20,
            modifier = Modifier.size(size),
        )
        CircularProgressIndicator(
            progress = progress,
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