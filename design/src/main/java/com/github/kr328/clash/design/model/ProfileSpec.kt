package com.github.kr328.clash.design.model

import com.github.kr328.clash.service.model.Profile
import java.util.*

data class ProfileSpec(
    val uuid: UUID,
    val name: String,
    val selected: Boolean = false,
) {
    companion object {
        fun from(profile: Profile): ProfileSpec = ProfileSpec(
            uuid = profile.uuid,
            name = profile.name
        )
    }
}