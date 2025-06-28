package com.yash.fitnesstracker.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomizedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val color by remember { mutableStateOf(
        if(isDarkTheme)Color(0xFF4B5859) else Color(0xFFAEBDBF)
    )
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp) // Increased height for a proper button
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.6f))


            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier= Modifier.weight(1f))
            Icon(imageVector = Icons.Default.ArrowForward,
                contentDescription = null)
        }
    }
}