package com.github.kr328.clash.design.model

import java.util.*

data class TaskerSettings(
    val turnOn: Boolean,
    val profile: UUID?,
) {
    companion object
}