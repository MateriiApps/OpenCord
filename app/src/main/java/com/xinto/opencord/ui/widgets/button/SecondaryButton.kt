package com.xinto.opencord.ui.widgets.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xinto.opencord.ui.component.button.OpenCordButton
import com.xinto.opencord.ui.theme.secondaryButtonColor

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    OpenCordButton(
        modifier = modifier,
        onClick = onClick,
        content = content,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondaryButtonColor
        )
    )
}