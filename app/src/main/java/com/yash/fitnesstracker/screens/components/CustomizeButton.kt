package com.yash.fitnesstracker.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomizedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon : ImageVector = Icons.Default.History,
    isDarkTheme: Boolean,
    iconColor:Color = if(isDarkTheme) Color.White else Color.Black
) {
    val color by remember { mutableStateOf(
        if(isDarkTheme)Color(0xFF4B5859) else Color(0xFFAEBDBF)
    )
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.6f))


            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row {
            Icon(imageVector = icon,
                contentDescription = text,
                tint = iconColor)
            Spacer(modifier= Modifier.padding(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier= Modifier.weight(1f))
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "forward arrow")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CircleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    started: Boolean = false
) {
    Box(
        modifier = modifier
            .size(150.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF86EBF1),
                        Color(0xFF0A55C2),
                        Color(0xF0076974),
                        Color(0xFF1281AF)
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = if(!started)Icons.Outlined.PlayArrow else Icons.Outlined.Pause,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color(0xFF91E5FA),
            )
    }
}