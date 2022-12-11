package com.xinto.opencord.domain.activity

import androidx.compose.runtime.Immutable
import com.xinto.opencord.domain.activity.types.*
import com.xinto.opencord.rest.models.activity.ApiActivity

@Immutable
interface DomainActivity {
    val name: String
    val createdAt: Long
    val type: ActivityType
}

fun ApiActivity.toDomain(): DomainActivity {
    return when (ActivityType.fromValue(this.type)) {
        ActivityType.Game -> DomainActivityGame(
            name = name,
            createdAt = createdAt ?: 0,
            id = id!!,
            state = state!!,
            details = details!!,
            applicationId = applicationId!!.value,
            party = party?.toDomain(),
            assets = assets?.toDomain(),
            secrets = secrets?.toDomain(),
            timestamps = timestamps?.toDomain(),
        )
        ActivityType.Streaming -> DomainActivityStreaming(
            name = name,
            createdAt = createdAt ?: 0,
            id = id!!,
            url = url!!,
            state = state!!,
            details = details!!,
            assets = assets!!.toDomain(),
        )
        ActivityType.Listening -> DomainActivityListening(
            name = name,
            createdAt = createdAt ?: 0,
            id = id!!,
            flags = flags!!,
            state = state!!,
            details = details!!,
            syncId = syncId!!,
            party = party!!.toDomain(),
            assets = assets!!.toDomain(),
            metadata = metadata?.toDomain(),
            timestamps = timestamps!!.toDomain(),
        )
        ActivityType.Custom -> DomainActivityCustom(
            name = name,
            createdAt = createdAt ?: 0,
            status = state,
            emoji = emoji?.toDomain(),
        )
        else -> DomainActivityUnknown(
            name = name,
            createdAt = createdAt ?: 0,
        )
    }
}
