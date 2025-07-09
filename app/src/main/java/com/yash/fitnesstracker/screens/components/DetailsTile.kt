package com.yash.fitnesstracker.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yash.fitnesstracker.R

@Composable
fun DetailsTile(
    text: String,
    time: String,
    cal: Double,
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
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon,
                contentDescription = text,
                tint = iconColor)
            Spacer(modifier= Modifier.padding(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier= Modifier.weight(1f))
            Column {
                Row {
                    Image(painter = painterResource(R.drawable.pngtreefueguito_png_6502196),
                        contentDescription = null,
                        Modifier.size(20.dp))

                    Text(text = " : $cal")
                }
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}