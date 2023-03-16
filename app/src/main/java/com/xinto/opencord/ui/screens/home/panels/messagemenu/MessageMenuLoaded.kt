package com.xinto.opencord.ui.screens.home.panels.messagemenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.opencord.R
import com.xinto.opencord.domain.emoji.DomainGuildEmoji
import com.xinto.opencord.domain.emoji.DomainUnicodeEmoji
import com.xinto.opencord.domain.emoji.DomainUnknownEmoji
import com.xinto.opencord.ui.components.OCImage
import com.xinto.opencord.ui.components.OCSize
import com.xinto.opencord.ui.viewmodel.MessageMenuViewModel
import com.xinto.opencord.util.Quad

@Composable
fun MessageMenuLoaded(
    viewModel: MessageMenuViewModel,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp),
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
        ) {
            MessageMenuPreviewMessage(
                message = viewModel.message!!,
            )
        }

        Divider(
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            for (emoji in viewModel.frequentReactions) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier
                        .size(42.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { /* TODO: react to message */ },
                    ) {
                        when (emoji) {
                            is DomainUnicodeEmoji -> {
                                Text(
                                    text = emoji.emoji,
                                    fontSize = 20.sp,
                                )
                            }
                            is DomainGuildEmoji -> {
                                OCImage(
                                    url = emoji.url,
                                    size = OCSize(64, 64),
                                    modifier = Modifier
                                        .size(18.dp),
                                )
                            }
                            is DomainUnknownEmoji -> {}
                        }
                    }
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier
                    .size(42.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { /* TODO: reaction selector sheet */ },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_reaction),
                        contentDescription = "Open reaction picker",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(6.dp),
                    )
                }
            }
        }

        val messageMenuItems = remember(viewModel.isEditable, viewModel.isDeletable, viewModel.pinState) {
            arrayOf(
                Quad("Reply", R.drawable.ic_reply, viewModel::onReply, true),
                Quad("Edit", R.drawable.ic_edit, viewModel::onEdit, viewModel.isEditable),
                Quad("Delete", R.drawable.ic_delete, viewModel::onDelete, viewModel.isDeletable),
                Quad("Copy Message Link", R.drawable.ic_link, viewModel::onCopyLink, true),
                Quad("Copy Message", R.drawable.ic_file_copy, viewModel::onCopyMessage, true),
                Quad("Mark Unread", R.drawable.ic_mark_unread, viewModel::onMarkUnread, true),
                Quad("Pin", R.drawable.ic_pin, viewModel::togglePinned, viewModel.pinState == MessageMenuViewModel.PinState.Pinnable),
                Quad("Unpin", R.drawable.ic_pin, viewModel::togglePinned, viewModel.pinState == MessageMenuViewModel.PinState.Unpinnable),
                Quad("Copy ID", R.drawable.ic_file_copy, viewModel::onCopyId, true),
            )
        }

        for ((name, icon, onClick, enabled) in messageMenuItems) {
            if (enabled) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onClick)
                            .padding(horizontal = 14.dp, vertical = 13.dp),
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                        )

                        Text(
                            text = name,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
        }
    }
}
