package com.yash.fitnesstracker.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.R
import com.yash.fitnesstracker.database.ActivityDTO
import com.yash.fitnesstracker.screens.components.Stopwatch
import com.yash.fitnesstracker.viewmodel.UserDataState
import com.yash.fitnesstracker.viewmodel.AppViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat


//@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityRunningScreen(navController: NavHostController,
                          appViewModel: AppViewModel,
                          screenName: String,
                          icon: ImageVector,met: Double,
                          userDataState: UserDataState)
{


    val coroutineScope = rememberCoroutineScope()

    var timeInMillis by remember { mutableLongStateOf(0L) }
    val formattedTime = remember(timeInMillis) {
        val totalSeconds = timeInMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        String.format("%02d:%02d:%02d", hours,minutes, seconds)
    }

    val sec = (timeInMillis / 1000 ).toInt()
    val calories = calBurned(met, bw = userDataState.weight.toDouble(),sec = sec)

    val infiniteTransition = rememberInfiniteTransition(label = "gradient")

    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = -350f,
        targetValue = 350f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xD07B90B3))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f) // match gradient height
        ) {
            // Canvas background
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val width = size.width
                val height = size.height

                val path = Path().apply {
                    moveTo(0f, height * 0.8f)
                    lineTo(width / 3f, height)
                    cubicTo(
                        width / 3f, height,
                        width * 0.5f, height * 0.8f,
                        (width / 3) * 2f, height
                    )
                    lineTo(width, height * 0.8f)
                    lineTo(width, 0f)
                    lineTo(0f, 0f)
                    close()
                }

                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF86EBF1),
                            Color(0xFF0A55C2),
                            Color(0xF0076974),
                            Color(0xFF1281AF)
                        ),
                        start = Offset(animatedOffset, 0f),
                        end = Offset(animatedOffset + size.width * 1.2f, size.height)
                    ),
                    style = Fill
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column {
                    Icon(
                        imageVector = icon, contentDescription = null, Modifier
                            .size(120.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.padding(10.dp))
                    Text(
                        text = screenName,
                        fontSize = 35.sp,
                        color = Color.White
                    )
                }
            }


        }


        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Stopwatch(onTimeChange = { newTime -> timeInMillis = newTime })

            Spacer(Modifier.padding(20.dp))
            val df = DecimalFormat("#.#")
            Row {
                Image(
                    painter = painterResource(R.drawable.pngtreefueguito_png_6502196),
                    contentDescription = "calories burned",
                    modifier = Modifier
                        .size(50.dp)
                )


                Text(text = " : ${df.format(calories)}kcal", fontSize = 40.sp)
            }

        }


    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 50.dp, end = 30.dp)) {
            val df = DecimalFormat("#.#")
            Text(text = "Finish", color = Color.Cyan,
                modifier = Modifier
                    .clickable(onClick = {

                        Log.d("click", "click")
                        coroutineScope.launch {
                            val activity = ActivityDTO(
                                name = screenName,
                                time = formattedTime,
                                cal = df.format(calories).toDouble()
                            )

                            appViewModel.addActivity(activity)

                            navController.popBackStack()


                        }
                    })
                    .align(Alignment.TopEnd)

            )

    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 50.dp, start = 20.dp)) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Arrow Back",
            modifier = Modifier
                .align(Alignment.TopStart)
                .clickable(
                    onClick = { navController.popBackStack() }
                ))
    }

    

}

private fun calBurned(met: Double, bw: Double, sec:Int): Double
{
    return met*bw*(sec/3600.0)
}