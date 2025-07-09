package com.yash.fitnesstracker.screens.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgress(progress: Float,
                     modifier: Modifier= Modifier,
                     isDarkTheme: Boolean
)
{ 
    val color by remember { mutableStateOf(
        if(isDarkTheme) Color(0xF1628FDE) else Color(0xF1114195)
    ) }
    CircularProgressIndicator(
        progress = {progress},
        strokeWidth = 15.dp,
        strokeCap = StrokeCap.Butt,
        trackColor = color.copy(alpha = 0.3f),
        color = color,
        modifier = modifier.size(100.dp))
}

