package com.xinto.opencord.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.viewmodel.MembersViewModel
import com.xinto.opencord.ui.widget.WidgetMemberListItem
import org.koin.androidx.compose.getViewModel

@Composable
fun ChannelMembersScreen(
    modifier: Modifier = Modifier,
    viewModel: MembersViewModel = getViewModel()
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
    ) {
        items(viewModel.members) { member ->
            WidgetMemberListItem(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable {},
                guildMember = member,
            )
        }
    }
}
