package com.yash.fitnesstracker.Screens.Components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgress(progress: Float)
{
    CircularProgressIndicator(
        progress = {progress},
        strokeWidth = 15.dp,
        strokeCap = StrokeCap.Butt,
        trackColor = Color.Red, // whole
        color = MaterialTheme.colorScheme.primary, // how much
        modifier = Modifier.size(100.dp))
}

@Preview(showSystemUi = true)
@Composable
fun pre()
{
    CircularProgress(0.2f)
}