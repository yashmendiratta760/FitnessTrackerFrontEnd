package com.yash.fitnesstracker.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.navigation.Screens
import kotlin.math.exp

//@Preview(showSystemUi = true)
@Composable
fun DropDownOption(expanded: Boolean
                   , onDismiss: () -> Unit,
                   navController: NavHostController)
{

    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onDismiss()
            },
            offset = DpOffset(x = (-11).dp, y = 0.dp),
            modifier = Modifier.background(color = Color(0xFF03193E))
        ) {
            DropdownMenuItem(
                text = { Text(text = "History") },
                onClick = {
                    navController.navigate(Screens.History.name)
                    onDismiss()
                },
                modifier = Modifier.height(35.dp)
                    .width(130.dp)
            )
        }
    }

}