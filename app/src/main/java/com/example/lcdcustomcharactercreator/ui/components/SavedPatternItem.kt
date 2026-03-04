package com.example.lcdcustomcharactercreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.lcdcustomcharactercreator.R

/**
 * Creates a saved custom character pattern item.
 * @param name name of pattern.
 * @param description description of pattern(optional).
 * @param creationDatetime pattern creation datetime.
 * @param onClick item on-click function.
 * @param onDelete delete item function.
 */
@Composable
fun SavedPatternUiItem(
    name: String,
    description: String?,
    creationDatetime: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onClick()
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = name)

            // description
            description?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Light
                )
            }

            Text(
                text = creationDatetime,
                fontStyle = FontStyle.Italic
            )
        }

        // delete saved pattern button
        IconButton(onClick = { onDelete() }) {
            Icon(
                painter = painterResource(R.drawable.outline_delete_24),
                contentDescription = null
            )
        }
    }
}