package com.yash.fitnesstracker.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yash.fitnesstracker.utils.StopwatchHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StopWatch() {

    val stopWatch = remember { StopwatchHelper() }
    var timeMillis by remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Time: ${formatTime(timeMillis)} s")

        Row {
            Button(onClick = {
                stopWatch.start(coroutineScope){time->
                    timeMillis=time

                }
            }) {
                Text("Start")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                stopWatch.reset()
            }) {
                Text("Stop")
            }
        }
    }
}

fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
