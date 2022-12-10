package com.xinto.opencord.ui.screens.pins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R

@Composable
fun PinsScreenError(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.error,
            LocalTextStyle provides MaterialTheme.typography.titleMedium,
        ) {
            Icon(
                modifier = Modifier.size(56.dp),
                painter = painterResource(R.drawable.ic_error),
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.pins_loading_error),
                textAlign = TextAlign.Center,
            )
        }
    }
}
