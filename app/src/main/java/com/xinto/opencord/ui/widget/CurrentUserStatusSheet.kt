package com.xinto.opencord.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xinto.bdc.BottomSheetDialog
import com.xinto.opencord.R
import com.xinto.opencord.ui.viewmodel.CurrentUserViewModel

@Composable
fun CurrentUserStatusSheet(
    viewModel: CurrentUserViewModel,
    onClose: () -> Unit
) {
    BottomSheetDialog(onDismissRequest = onClose) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 28.dp, vertical = 35.dp),
        ) {
            // Status selector icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterHorizontally
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
            ) {
                val statuses = arrayOf(
                    R.drawable.ic_status_online,
                    R.drawable.ic_status_idle,
                    R.drawable.ic_status_dnd,
                    R.drawable.ic_status_invisible,
                )
                for (status in statuses) {
                    Icon(
                        painter = painterResource(status),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                viewModel.setStatus(status)
                                onClose()
                            },
                    )
                }
            }

            ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                // Custom status button
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.clickable { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_set_custom_status),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(25.dp),
                    )
                    Text("Set a custom status")
                }

                // Account switcher button
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.clickable { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_account_switch),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                    )
                    Text("Switch Accounts")
                }
            }
        }
    }
}
