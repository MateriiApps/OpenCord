package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.screens.section
import com.xinto.opencord.ui.screens.setting

context(LazyListScope)
fun accountScreen() {
    section("Account Information") {
        setting(
            label = "Username",
            onClick = {},
        ) {
            Text("opencord#1234")
        }
        setting(
            label = "Email",
            onClick = {},
        ) {
            Text("opencord@gmail.com")
        }
        setting(
            label = "Phone",
            onClick = {},
        ) {

        }
        setting(
            label = "Password",
            onClick = {},
        )

        setting(
            label = "Two-factor authentication",
            onClick = {},
            trailingContent = {
                OutlinedButton(onClick = {}) {
                    Text("Enable")
                }
            },
        )
    }

    section("Account Management") {
        item {
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(
                    onClick = { /*TODO show confirm dialog*/ },
                ) {
                    Text("Disable Account")
                }

                FilledTonalButton(
                    onClick = { /*TODO show confirm dialog*/ },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    ),
                ) {
                    Text("Delete Account")
                }
            }
        }
    }
}
