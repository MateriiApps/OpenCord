package com.xinto.opencord.ui.component.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun OpenCordListCategory(
    text: String
) {
    CompositionLocalProvider(
        LocalContentAlpha provides ContentAlpha.medium
    ) {
        Row(
            modifier = Modifier
                .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .clickable {  }
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Collapse"
            )
            OpenCordText(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = text.uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.h6
            )
        }
    }
}