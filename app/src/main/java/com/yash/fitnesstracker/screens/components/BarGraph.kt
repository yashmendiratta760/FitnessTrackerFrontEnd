package com.yash.fitnesstracker.screens.components

import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.work.Data


@Composable
fun BarGraph(data:List<Int>,
             labels: List<String>,
             date:List<String>,
             isDarkTheme: Boolean = isSystemInDarkTheme())
{
    val max = data.maxOrNull() ?: 1
    val barRects = remember { mutableStateListOf<Rect>() }
    val labelPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 30f
        textAlign = android.graphics.Paint.Align.CENTER
        color = if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
    }
    var selectedBarIndex by remember { mutableStateOf<Int?>(null) }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Text(text = "${date.get(0)}  ->  ${date.get(data.size-1)}",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 40.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)

                .pointerInput(data) {
                    detectTapGestures { offset ->
                        var found = false
                        barRects.forEachIndexed { index, rect ->
                            if (rect.contains(offset.x.toInt(), offset.y.toInt())) {
                                selectedBarIndex = index
                                found = true
                            }
                            if (!found) {
                                selectedBarIndex = null
                            }
                        }
                    }
                }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                barRects.clear()
                val canvasWidth = size.width
                val canvasHeight = size.height

                val paddingBottom = 40f
                val paddingLeft = 50f

                val barWidth = (canvasWidth - paddingLeft) / (data.size * 2)
                val usableHeight = canvasHeight - paddingBottom

                // --- Draw Axes ---
                drawLine(
                    color = if (isDarkTheme) Color.White else Color.Black,
                    start = Offset(paddingLeft, 0f),
                    end = Offset(paddingLeft, usableHeight),
                    strokeWidth = 5f
                ) // Y-axis

                drawLine(
                    color = if (isDarkTheme) Color.White else Color.Black,
                    start = Offset(paddingLeft, usableHeight),
                    end = Offset(canvasWidth, usableHeight),
                    strokeWidth = 5f
                ) // X-axis

                // --- Draw Bars + X-Labels ---
                data.forEachIndexed { index, value ->
                    val barHeight = (value / max.toFloat()) * (usableHeight - 20f)
                    val x = paddingLeft + index * 2 * barWidth + barWidth / 2
                    val y = usableHeight - barHeight

                    val rect = Rect(
                        x.toInt(),
                        y.toInt(),
                        (x + barWidth).toInt(),
                        (y + barHeight).toInt()
                    )
                    barRects.add(rect)
                    // Draw bar
                    drawRoundRect(
                        color = Color(0xF03B82A3),
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(x=12f,y=12f)
                    )

                    // Draw x-axis label
                    drawContext.canvas.nativeCanvas.drawText(
                        labels.getOrNull(index) ?: "",
                        x + barWidth / 2,
                        canvasHeight,
                        labelPaint
                    )
                    if (selectedBarIndex == index) {
                        val text = value.toString()
                        val textBounds = Rect()
                        labelPaint.getTextBounds(text, 0, text.length, textBounds)
                        val boxWidth = textBounds.width() + 2 * 10f
                        val boxHeight = 100f
                        val boxX = x + barWidth / 2 - boxWidth / 2
                        val boxY = y - boxHeight

                        val path = tooltipPath(boxWidth, boxHeight, boxX, boxY)

                        drawPath(
                            path = path,
                            color = Color.Black
                        )
                        val labelPaint = Paint().asFrameworkPaint().apply {
                            isAntiAlias = true
                            textSize = 30f
                            color = android.graphics.Color.WHITE

                            textAlign = android.graphics.Paint.Align.CENTER
                            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                        }
                        drawContext.canvas.nativeCanvas.drawText(
                            value.toString(),
                            boxX + boxWidth / 2,
                            boxY + boxHeight * 0.65f - 18f, // slightly above triangle tip
                            labelPaint
                        )
                    }
                }

                // --- Draw Y-axis Labels (optional) ---
                val steps = 5
                for (i in 0..steps) {
                    val labelValue = (max * i) / steps
                    val yPos = usableHeight - (i / steps.toFloat()) * (usableHeight - 20f)

                    // Draw label
                    drawContext.canvas.nativeCanvas.drawText(
                        labelValue.toString(),
                        0f,
                        yPos,
                        labelPaint
                    )
                }
            }
        }
    }


}

