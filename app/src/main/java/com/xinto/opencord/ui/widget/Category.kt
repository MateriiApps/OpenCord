package com.xinto.opencord.ui.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import java.util.*

@Composable
fun WidgetCategory(
    title: String,
    modifier: Modifier = Modifier,
    collapsed: Boolean,
    onClick: () -> Unit,
) {
    val iconRotation = animateFloatAsState(if (collapsed) 0f else 90f, spring(dampingRatio = 2.5f))

    CompositionLocalProvider(
        LocalContentAlpha provides ContentAlpha.medium,
        LocalTextStyle provides MaterialTheme.typography.labelMedium
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .then(modifier)
        ) {
            Icon(
                painterResource(R.drawable.ic_collapsed_category),
                contentDescription = "Collapse category",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(iconRotation.value)
            )
            Text(
                title.uppercase(Locale.getDefault()),
            )
        }
    }
}