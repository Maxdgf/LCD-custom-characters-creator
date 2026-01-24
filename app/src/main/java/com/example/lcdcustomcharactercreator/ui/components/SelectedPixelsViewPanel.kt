package com.example.lcdcustomcharactercreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.BitSet

/**
 * Creates lcd custom character preview frame
 * @param size size of preview frame in pixels.
 * @param pixelsMap pixels map.
 * @param isDisplayBlue bool state of preview skin, blue lcd or green.
 */
@Composable
fun SelectedUiPixelsViewPanel(
    size: Int,
    pixelsMap: BitSet,
    isDisplayBlue: Boolean
) {
    val enabledPixelColor = if (isDisplayBlue) Color(0xFFBDE5FC) else Color(0xFF000500)
    val disabledPixelColor = if (isDisplayBlue) Color(0xFF1A7CD5) else Color(0xFFA8FF00)
    val frameBackgroundColor = if (isDisplayBlue) Color(0xFF2398FF) else Color(0xFFD0FF00)

    Box(
        modifier = Modifier
            .width(100.dp)
            .height(176.dp)
            .background(
                color = frameBackgroundColor,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(5.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            for (i in 0..size - 1) {
                val state = pixelsMap.get(i)
                item {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(if (state) enabledPixelColor else disabledPixelColor)
                    )
                }
            }
        }
    }
}