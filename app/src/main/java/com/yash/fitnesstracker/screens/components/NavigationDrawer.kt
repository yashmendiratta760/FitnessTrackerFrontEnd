package com.yash.fitnesstracker.screens.components

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.R
import com.yash.fitnesstracker.navigation.Screens
import com.yash.fitnesstracker.screens.HomeScreen
import com.yash.fitnesstracker.ui.theme.DarkBg
import com.yash.fitnesstracker.ui.theme.DarkG1
import com.yash.fitnesstracker.ui.theme.DarkG3
import com.yash.fitnesstracker.ui.theme.LightBg
import com.yash.fitnesstracker.ui.theme.LightG1
import com.yash.fitnesstracker.ui.theme.LightG2
import com.yash.fitnesstracker.viewmodel.AppUiState
import com.yash.fitnesstracker.viewmodel.UserDataState
import com.yash.fitnesstracker.viewmodel.UserViewModel
import com.yash.fitnesstracker.viewmodel.appViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationDrawer(appViewModel: appViewModel,
                     loginSignupViewModel: LoginSignupViewModel,
                     userViewModel: UserViewModel,
                     navController: NavHostController,
                     drawerState: DrawerState,
                     appUiState: AppUiState,
                     userUiState: UserDataState,
                     isDarkTheme: Boolean = isSystemInDarkTheme())
{
    val context = LocalContext.current
    val colorD by remember { mutableStateOf(listOf(DarkBg, DarkG1, DarkG3)) }
    val colorL by remember { mutableStateOf(listOf(LightBg, LightG1, LightG2)) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier
                .width(screenWidth / 1.5f),
                drawerContainerColor = if (isDarkTheme) Color(0xFF0A1734) else Color(0xFFA0B0EC)
            ){
                DrawerItems(loginSignupViewModel,context,navController, appUiState = appUiState,
                    userUiState = userUiState)
            }
        },
        drawerState = drawerState
    ) {
        HomeScreen(
            appViewModel = appViewModel,
            userViewModel = userViewModel,
            navController = navController, drawerState = drawerState
        )

    }
}

@Composable
fun DrawerItems(loginSignupViewModel: LoginSignupViewModel,
                context: Context,
                navController: NavHostController,
                appUiState: AppUiState,
                userUiState: UserDataState,
                isDarkTheme: Boolean = isSystemInDarkTheme())
{
    Column{
        NavigationDrawerItem(
            label = {
                Column(modifier = Modifier.fillMaxWidth()
                    .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = appUiState.imageUrl,
                        contentDescription = "User Profile Image",
                        modifier = Modifier.size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(text = userUiState.name,
                        style = MaterialTheme.typography.bodyLarge)
                }
            },
            selected = false,
            onClick = { }
        )
        Spacer(modifier = Modifier.padding(top =10.dp))

        HorizontalDivider(thickness = 0.5.dp, color = DividerDefaults.color)

        Spacer(modifier = Modifier.padding(top =10.dp))
        NavigationDrawerItem(
            label = {
                Row {
                    Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Profile")
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Profile",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                }
            },
            selected = false,
            onClick = {
                navController.navigate(Screens.Profile.name)
            }
        )
        NavigationDrawerItem(
            label = {
                Row {
                    Icon(imageVector = Icons.Outlined.Delete,
                        contentDescription = "Logout",
                        tint = Color.Red)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Logout",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Red
                    )
                }
            },
            selected = false,
            onClick = {
                loginSignupViewModel.logout(context) {
                    navController.navigate(Screens.Login.name) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        )
    }
}