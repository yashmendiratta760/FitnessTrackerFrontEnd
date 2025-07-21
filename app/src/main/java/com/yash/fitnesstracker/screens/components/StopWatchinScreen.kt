package com.yash.fitnesstracker.screens.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Composable
fun Stopwatch(modifier: Modifier = Modifier,
              onTimeChange: (Long) -> Unit) {
    var isRunning by remember { mutableStateOf(false) }
    var timeInMillis by remember { mutableLongStateOf(0L) }
    var lastTimestamp by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            lastTimestamp = System.currentTimeMillis()
            while (isRunning) {
                delay(10L)
                val now = System.currentTimeMillis()
                timeInMillis += (now - lastTimestamp)
                lastTimestamp = now
                onTimeChange(timeInMillis)
            }
        }
    }

    val formattedTime = remember(timeInMillis) {
        val totalSeconds = timeInMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        String.format("%02d:%02d:%02d", hours,minutes, seconds)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.56f),
            contentAlignment = Alignment.BottomCenter
        ) {
            CircleButton(onClick = {
                isRunning = !isRunning
            },
                started = isRunning)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = formattedTime,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
