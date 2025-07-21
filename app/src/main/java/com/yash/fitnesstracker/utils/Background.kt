package com.yash.fitnesstracker.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.yash.fitnesstracker.ui.theme.DarkBg
import com.yash.fitnesstracker.ui.theme.DarkG1
import com.yash.fitnesstracker.ui.theme.DarkG3
import com.yash.fitnesstracker.ui.theme.LightBg
import com.yash.fitnesstracker.ui.theme.LightG1
import com.yash.fitnesstracker.ui.theme.LightG2

@Composable
fun Bg(isDarkTheme: Boolean = isSystemInDarkTheme())
{
    val colorD by remember { mutableStateOf(listOf(DarkBg, DarkG1, DarkG3)) }
    val colorL by remember { mutableStateOf(listOf(LightBg, LightG1, LightG2)) }
    Column(Modifier.fillMaxSize()
        .background(brush = Brush.linearGradient(colors = if(isDarkTheme) colorD else colorL))) {

    }
}