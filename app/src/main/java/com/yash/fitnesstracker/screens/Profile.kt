package com.yash.fitnesstracker.screens

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.Data
import coil.compose.AsyncImage
import com.yash.fitnesstracker.R
import com.yash.fitnesstracker.Service.DataStoreManager
import com.yash.fitnesstracker.navigation.Screens
import com.yash.fitnesstracker.screens.components.CustomisedTopBar
import com.yash.fitnesstracker.utils.bg
import com.yash.fitnesstracker.viewmodel.AppUiState
import com.yash.fitnesstracker.viewmodel.UserViewModel
import com.yash.fitnesstracker.viewmodel.appViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavHostController,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    userViewModel: UserViewModel,
    appViewModel: appViewModel,
    appUiState: AppUiState
) {

    val userUiState = userViewModel.uiState.collectAsState()


    var name by remember { mutableStateOf(userUiState.value.name) }
    var age by remember { mutableStateOf(userUiState.value.age) }
    var weight by remember { mutableStateOf(userUiState.value.weight) }
    var height by remember { mutableStateOf(userUiState.value.height) }
    var stepsGoal by remember { mutableStateOf(userUiState.value.stepsGoal) }
    val canNavigateBack = navController.previousBackStackEntry != null
    val scrollState = rememberScrollState()
    val corountineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            Log.d("SelectedImage", "URI: $uri")
            val file = getMultipartFromUri(context,uri)
            val requestBody: RequestBody = "_yash_".toRequestBody("text/plain".toMediaTypeOrNull())
            corountineScope.launch {
                appViewModel.uploadImage(file,requestBody)
            }

        }
    }


    Scaffold(
        topBar = {
            CustomisedTopBar(
                title = "Profile",
                navController = navController,
                showSave = true,
                canNavigateBack = canNavigateBack,
                onSaveClick = {
                    corountineScope.launch {
                        DataStoreManager.saveName(context,name)
                        DataStoreManager.saveAge(context,age.toInt())
                        DataStoreManager.saveWeight(context,weight.toDouble())
                        DataStoreManager.saveHeight(context,height.toDouble())
                        DataStoreManager.saveSteps(context,stepsGoal.toInt())

                    }
                    navController.navigate(Screens.Home.name){
                        popUpTo(0){
                            inclusive=true
                        }
                    }
                }
            )
        }
    ) {
        bg()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(it)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {

                AsyncImage(
                    model = appUiState.imageUrl,
                    contentDescription = "User Profile Image",
                    modifier = Modifier.size(150.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.padding(8.dp))
                Text(
                    text = "Change profile picture",
                    color = Color.Blue,
                    modifier = Modifier.clickable(onClick = {
                        launcher.launch("image/*")
                    })
                )


            }


            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp),
                    colors = TextFieldDefaults.colors(
//                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )

                )
                TextField(
                    value = age,
                    onValueChange = {
                        if(it.all { char -> char.isDigit() })
                        {
                            age = it
                        }
                    },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp),
                    colors = TextFieldDefaults.colors(
//                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )

                )
            }



            Spacer(modifier = Modifier.height(24.dp))

            // Fitness Stats
            Text("Fitness Stats",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp))
            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth() ,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = weight,
                    onValueChange = {
                        if(it.all { char -> char.isDigit() })
                        {
                            weight = it
                        }
                    },
                    label = { Text("Weight(in Kg)") },
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 8.dp),
                    colors = TextFieldDefaults.colors(
//                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )

                )
                TextField(
                    value = height,
                    onValueChange = {
                        if(it.all { char -> char.isDigit() })
                        {
                            height = it
                        }
                    },
                    label = { Text("Height(in Cm)") },
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                    colors = TextFieldDefaults.colors(
//                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )

                )
                TextField(
                    value = stepsGoal,
                    onValueChange = {
                        if(it.all { char -> char.isDigit() })
                        {
                            stepsGoal = it
                        }
                    },
                    label = { Text("Steps Goal") },
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                    colors = TextFieldDefaults.colors(
//                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )

                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Account Info
            Text("Account", style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp))
            Spacer(Modifier.height(8.dp))
            TextField(
                value = "yash@gmail",
                onValueChange = { },
                label = { Text("Email") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.colors(
//                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = if(isDarkTheme) Color.White else Color.Black,
                    disabledLabelColor = if(isDarkTheme) Color.White else Color.Black
                )

            )
            TextField(
                value = "_yash_",
                onValueChange = { },
                label = { Text("User Name") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.colors(
//                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = if(isDarkTheme) Color.White else Color.Black,
                    disabledLabelColor = if(isDarkTheme) Color.White else Color.Black
                )

            )




        }
    }
}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}

fun getMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part {
    val file = uriToFile(context, uri)
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("file", file.name, requestFile)
}