package com.yash.fitnesstracker.Login_Signup.Screens.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogueBoxOpt(showDialogue: MutableState<Boolean>,
                   onLogoutClick : ()-> Unit)
{
    Dialog(
        onDismissRequest = { showDialogue.value = false }
    ){
        Surface(shape = RoundedCornerShape(8.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.padding(16.dp)){
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select an option", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text(text = "Logout", color = Color.Red,
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp)
                        .clickable(onClick = {onLogoutClick()}))
            }
        }
    }
}

