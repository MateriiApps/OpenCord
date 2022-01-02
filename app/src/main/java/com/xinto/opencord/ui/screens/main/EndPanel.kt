package com.xinto.opencord.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.domain.model.DomainGuildMember
import com.xinto.opencord.ui.component.layout.OpenCordBackground
import com.xinto.opencord.ui.widgets.member.WidgetMember

@Composable
fun EndPanel(
    guildMembers: List<DomainGuildMember>
) {
    OpenCordBackground(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        backgroundColorAlpha = 0.6f
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(guildMembers) { member ->
                WidgetMember(
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable {},
                    guildMember = member
                )
            }
        }
    }
}