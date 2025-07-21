package com.yash.fitnesstracker.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomisedTopBar(title: String,
                     navController: NavHostController,
                     showMoreIcon: Boolean = false,
                     onMoreClick: () -> Unit = {} ,
                     canNavigateBack : Boolean = true,
                     showSave:Boolean = false,
                     onSaveClick : ()-> Unit = {})
{
    TopAppBar(

        title = { Text(title) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }
        },
        actions = {
            if (showMoreIcon) {
                IconButton(onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }
            }
            if(showSave)
            {
                Text(text = "Save", modifier = Modifier.padding(end = 20.dp)
                    .clickable(onClick = {onSaveClick()}))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent// or Color.White
//            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
//            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}