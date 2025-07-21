package com.yash.fitnesstracker.screens.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Path


//
//fun tooltipPath(width: Float, height: Float, topLeftX: Float, topLeftY: Float): Path {
//    return Path().apply {
//        moveTo(topLeftX, topLeftY)
//
//        lineTo(topLeftX + width, topLeftY)
//        lineTo(topLeftX + width, topLeftY + height * 0.7f)
//        lineTo(topLeftX + width * 0.66f, topLeftY + height * 0.7f)
//        lineTo(topLeftX + width / 2, topLeftY + height)
//        lineTo(topLeftX + width * 0.33f, topLeftY + height * 0.7f)
//        lineTo(topLeftX, topLeftY + height * 0.7f)
//
//        close()
//    }
//}
//

fun tooltipPath(width: Float, height: Float, topLeftX: Float, topLeftY: Float): Path {
    val cornerRadius = 16f
    val triangleHeight = height * 0.3f
    val rectHeight = height - triangleHeight

    val path = Path()

    // Rounded Rect
    path.addRoundRect(
        roundRect = androidx.compose.ui.geometry.RoundRect(
            left = topLeftX,
            top = topLeftY,
            right = topLeftX + width,
            bottom = topLeftY + rectHeight,
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )
    )

    // Triangle
    path.moveTo(topLeftX + width / 2 - 20f, topLeftY + rectHeight)
    path.lineTo(topLeftX + width / 2, topLeftY + height*0.9f)
    path.lineTo(topLeftX + width / 2 + 20f, topLeftY + rectHeight)
    path.close()

    return path
}


