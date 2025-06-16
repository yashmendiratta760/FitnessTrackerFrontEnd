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
fun CircularProgress(progress: Float,
                     modifier: Modifier= Modifier)
{
    CircularProgressIndicator(
        progress = {progress},
        strokeWidth = 15.dp,
        strokeCap = StrokeCap.Butt,
        trackColor = Color(0xF1628FDE).copy(alpha = 0.3f),
        color = Color(0xF1628FDE),
        modifier = modifier.size(100.dp))
}

@Preview
@Composable
fun pre()
{

    CircularProgress(0.2f)
}