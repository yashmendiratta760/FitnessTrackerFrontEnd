package com.yash.fitnesstracker.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.database.Activities
import com.yash.fitnesstracker.navigation.Screens
import com.yash.fitnesstracker.screens.components.CustomisedTopBar
import com.yash.fitnesstracker.screens.components.CustomizedButton
import com.yash.fitnesstracker.screens.components.DropDownOption
import com.yash.fitnesstracker.utils.Bg

@Composable
fun ActivityScreen(navController: NavHostController,
                   isDarkTheme: Boolean)
{
    var menuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CustomisedTopBar("Activity",
                navController = navController,
                showMoreIcon = true,
                onMoreClick = {
                    menuExpanded = true
                })
        }
    ) {innerPadding->
        Box{
            Bg()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, top = 40.dp), // adjust spacing if needed
                contentAlignment = Alignment.TopEnd
            ) {
                DropDownOption(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    navController = navController
                )
            }

            Column(modifier = Modifier.padding(innerPadding)){
                LazyColumn {
                    items(Activities){activity->
                        CustomizedButton(text = activity.name, onClick = {
                            navController.navigate(route = "${Screens.ActivityRunning.name}?ScreenName=${activity.name}&Met=${activity.met}")
                        },
                            icon = activity.icon,
                            iconColor = activity.color,
                            modifier = Modifier.padding(top = 8.dp,
                                start = 8.dp,
                                end = 8.dp),
                            isDarkTheme = isDarkTheme
                            )

                    }
                }
            }

        }
    }
}