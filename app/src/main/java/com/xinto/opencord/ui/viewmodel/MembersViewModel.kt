package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.domain.member.DomainGuildMember
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel

class MembersViewModel(
    persistentDataManager: PersistentDataManager,
) : BasePersistenceViewModel(persistentDataManager) {
    val members = mutableStateListOf<DomainGuildMember>()
}
