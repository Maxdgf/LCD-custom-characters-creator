package com.example.lcdcustomcharactercreator.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Creates orientation-responsive container.
 * @param modifier modifier.
 * @param content composable content.
 */
@Composable
fun AdaptiveUiBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current // current configuration
    val orientation = configuration.orientation // screen orientation

    if (orientation == Configuration.ORIENTATION_PORTRAIT) Column(modifier = modifier) { content() }
    else Row(modifier = modifier) { content() }
}