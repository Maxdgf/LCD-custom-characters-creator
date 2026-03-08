package com.example.lcdcustomcharactercreator.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import com.example.lcdcustomcharactercreator.R

/**
 * Creates a title with icon, text and dismiss button.
 *
 * @param titleText dialog title text.
 * @param modifier dialog modifier.
 * @param titleIconPainter dialog title icon painter resource.
 * @param dismissDialogButtonFunction dismiss button function.
 */
@Composable
fun AppUiDialogTitle(
    titleText: String,
    modifier: Modifier = Modifier,
    titleIconPainter: Painter,
    dismissButtonPainter: Painter = painterResource(R.drawable.baseline_clear_24),
    dismissDialogButtonFunction: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = titleIconPainter,
            contentDescription = null
        )

        Text(
            text = titleText,
            modifier = Modifier
                .weight(1f)
                .basicMarquee(Int.MAX_VALUE),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        IconButton(onClick = { dismissDialogButtonFunction() }) {
            Icon(
                painter = dismissButtonPainter,
                contentDescription = null
            )
        }
    }
}