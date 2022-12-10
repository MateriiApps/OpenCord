package com.xinto.opencord.ui.components.embed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmbedField(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        ProvideTextStyle(MaterialTheme.typography.labelMedium) {
            Text(name)
        }
        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
            Text(value)
        }
    }
}
