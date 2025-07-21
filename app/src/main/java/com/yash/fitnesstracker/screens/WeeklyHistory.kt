package com.yash.fitnesstracker.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.database.StepsDTO
import com.yash.fitnesstracker.screens.components.BarGraph
import com.yash.fitnesstracker.screens.components.CustomisedTopBar
import com.yash.fitnesstracker.utils.Bg
import com.yash.fitnesstracker.viewmodel.AppUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyHistory(navController: NavHostController,
                  isDarkTheme: Boolean,
                  appUiState: AppUiState)
{
    var weekOffset by remember { mutableIntStateOf(1) }
//    val referenceDate = LocalDate.now().minusWeeks(weekOffset.toLong())

    Scaffold(
        topBar = {
            CustomisedTopBar("Weekly History", navController = navController)
        }
    ) {innerPadding->

        Box {
            Bg()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var totalDrag by remember { mutableFloatStateOf(0f) }
                val swipeThreshold = with(LocalDensity.current) { 100.dp.toPx() }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (totalDrag > swipeThreshold) {
                                        // Swipe right → go to previous week
                                        val potentialOffset = weekOffset + 1
                                        val potentialDate =
                                            LocalDate.now().minusWeeks(potentialOffset.toLong())
                                        if (hasDataForWeek(
                                                appUiState.stepsData,
                                                potentialDate
                                            )
                                        ) {
                                            weekOffset = potentialOffset
                                        }
                                    } else if (totalDrag < -swipeThreshold) {
                                        // Swipe left → go to a newer week
                                        val potentialOffset = weekOffset - 1
                                        if (potentialOffset >= 1) {
                                            weekOffset = potentialOffset
                                        }
                                    }

                                    totalDrag = 0f
                                },
                                onHorizontalDrag = { _, dragAmount ->
                                    totalDrag += dragAmount
                                }
                            )
                        }


                ) {
                    val pagerState = rememberPagerState(
                        initialPage = 0,
                        pageCount = { 52 })

                    HorizontalPager(
                        state = pagerState,
                        reverseLayout = true, // So right swipe = previous week
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val referenceDate = LocalDate.now().minusWeeks(page.toLong())

                        MakeStepsGraphWeekly(
                            stepsData = appUiState.stepsData,
                            referenceDate = referenceDate,
                            isDarkTheme=isDarkTheme
                        )
                        Log.d(
                            "WEEKLY_DEBUG",
                            "Page: $page → ${LocalDate.now().minusWeeks(page.toLong())}"
                        )
                    }

                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun hasDataForWeek(stepsData: List<StepsDTO>, referenceDate: LocalDate): Boolean {
    val weekDates = getWeekDates(referenceDate)
    return stepsData.any { step ->
        weekDates.contains(LocalDate.parse(step.date).format(DateTimeFormatter.ISO_DATE))
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun getWeekDates(referenceDate: LocalDate): List<String> {
    val formatter = DateTimeFormatter.ISO_DATE

    // Find Sunday of the week containing referenceDate
    val startOfWeek = referenceDate.minusDays((referenceDate.dayOfWeek.value % 7).toLong())

    return (0..6).map { offset ->
        startOfWeek.plusDays(offset.toLong()).format(formatter)
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MakeStepsGraphWeekly(
    stepsData: List<StepsDTO>,
    referenceDate: LocalDate = LocalDate.now(),
    isDarkTheme: Boolean
) {
    val formatter = DateTimeFormatter.ISO_DATE
    val weekDates = getWeekDates(referenceDate)

    val stepsMap = stepsData.associate { it.date to (it.steps.toIntOrNull() ?: 0) }

    val data = weekDates.map { date -> stepsMap[date] ?: 0 }

    val dates = weekDates.map { date ->
        LocalDate.parse(date, formatter).toString()
    }
    val labels= weekDates.map { date ->
        LocalDate.parse(date, formatter).dayOfWeek.name.take(3)
    }

    BarGraph(labels = labels, data = data,date = dates, isDarkTheme = isDarkTheme)
}

