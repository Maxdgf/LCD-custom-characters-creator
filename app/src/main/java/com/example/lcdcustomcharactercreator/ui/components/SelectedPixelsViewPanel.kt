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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.BitSet

/**
 * Generates style color tuple for lcd preview by skin state.
 * @param lcdPreviewSkinState lcd skin state.
 * @return color triple -> contains: (`enabled pixel color`, `disabled pixel color`, `frame bg color`)
 */
private fun generateLcdStyle(lcdPreviewSkinState: Pair<Boolean, Boolean>): Triple<Color, Color, Color> =
    when (lcdPreviewSkinState) {
        Pair(true, false) -> Triple(Color(0xFFBDE5FC), Color(0xFF1A7CD5), Color(0xFF2398FF)) // for blue
        Pair(false, true) -> Triple(Color(0xFF000000), Color(0xFFA8FF00), Color(0xFFD0FF00)) // for green
        else -> Triple(Color(0xFFBDE5FC), Color(0xFF1A7CD5), Color(0xFF2398FF)) // for blue (default)
    }

/**
 * Creates lcd frame pixel.
 * @param color enabled or disabled pixel color.
 */
@Composable
private fun Pixel(color: Color) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(color)
    )
}

/**
 * Creates lcd custom character preview 5x8 frame.
 *
 * @param pixelsMap pixels map.
 * @param lcdPreviewSkinState bool state of preview skin, blue lcd or green.
 */
@Composable
fun SelectedUiPixelsViewPanel(
    pixelsMap: BitSet,
    lcdPreviewSkinState: Pair<Boolean, Boolean>
) {
    val styleColorScheme = generateLcdStyle(lcdPreviewSkinState)

    Box(
        modifier = Modifier
            .width(100.dp)
            .height(176.dp)
            .background(
                color = styleColorScheme.third, // lcd frame background
                shape = RoundedCornerShape(5.dp)
            )
            .padding(5.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5), // 5 cells
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // create 40 pixels
            for (i in 0..39) {
                val state = pixelsMap.get(i) // get pixel state
                item {
                    Pixel(
                        color =
                            if (state) styleColorScheme.first // enabled pixel color
                            else styleColorScheme.second // disabled pixel color
                    ) // set pixel
                }
            }
        }
    }
}