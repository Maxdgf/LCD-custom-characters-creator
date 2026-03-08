package com.example.lcdcustomcharactercreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp

import com.example.lcdcustomcharactercreator.R

/**
 * Creates a saved custom character pattern item.
 * @param number order number.
 * @param name name of pattern.
 * @param description description of pattern(optional).
 * @param creationDatetime pattern creation datetime.
 * @param onClick item on-click function.
 * @param onDelete delete item function.
 * @param onCopyCode copy pattern source code function.
 */
@Composable
fun SavedPatternUiItem(
    number: Int,
    name: String,
    description: String?,
    creationDatetime: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onCopyCode: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = number.toString()) // order number
        Column(modifier = Modifier.weight(1f)) {
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

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // delete saved pattern button
            IconButton(onClick = { onDelete() }) {
                Icon(
                    painter = painterResource(R.drawable.outline_delete_24),
                    contentDescription = null
                )
            }

            // copy source code button
            IconButton(onClick = { onCopyCode() }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_code_24),
                    contentDescription = null
                )
            }
        }
    }
}