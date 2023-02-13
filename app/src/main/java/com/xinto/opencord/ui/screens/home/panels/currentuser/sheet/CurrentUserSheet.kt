package com.xinto.opencord.ui.screens.home.panels.currentuser.sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.ui.viewmodel.CurrentUserViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CurrentUserSheet(
    onClose: () -> Unit,
    viewModel: CurrentUserViewModel = getViewModel(),
) {
    ModalBottomSheet(
        onDismissRequest = onClose,
    ) {
        // Status selector icons
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterHorizontally,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
        ) {
            val statuses = listOf(
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
            if (viewModel.userCustomStatus != null) {
                // Custom status canceller
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp, vertical = 13.dp),
                ) {
                    // TODO: draw status emoji here
                    Text(viewModel.userCustomStatus!!.text ?: "")
                    Icon(
                        painter = painterResource(R.drawable.ic_cancel),
                        contentDescription = stringResource(R.string.current_user_clear_status),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable {
                                viewModel.setCustomStatus(null)
                                onClose()
                            },
                    )
                }
            } else {
                // Custom status select
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /*TODO*/ }
                        .padding(horizontal = 25.dp, vertical = 13.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_set_custom_status),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(25.dp),
                    )
                    Text(stringResource(R.string.current_user_set_status))
                }
            }

            // Account switcher button
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /*TODO*/ }
                    .padding(horizontal = 25.dp, vertical = 13.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_account_switch),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                )
                Text(stringResource(R.string.accounts_open))
            }
        }

        Spacer(Modifier.height(25.dp))
    }
}
