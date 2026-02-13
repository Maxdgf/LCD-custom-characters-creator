package com.example.lcdcustomcharactercreator.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import java.util.BitSet

/**
 * Creates custom character pixels input panel.
 * @param size pixels count.
 * @param pixelsMap pixels map.
 * @param updatePixelStateByIndex update pixel state by index function.
 */
@Composable
fun CharacterPixelsUiInputPanel(
    size: Int,
    pixelsMap: BitSet,
    updatePixelStateByIndex: (index: Int, state: Boolean) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier
            .width(250.dp)
            .wrapContentHeight()
    ) {
        for (i in 0..size - 1) {
            val index = i
            item {
                Checkbox(
                    checked = pixelsMap.get(index),
                    onCheckedChange = { state ->
                        updatePixelStateByIndex(index, state)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress) // haptic
                    }
                )
            }
        }
    }
}