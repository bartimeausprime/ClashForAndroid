package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import com.github.kr328.clash.design.adapter.TaskerProfileAdapter
import com.github.kr328.clash.design.databinding.DesignSettingsTaskerBinding
import com.github.kr328.clash.design.model.ProfileSpec
import com.github.kr328.clash.design.model.TaskerSettings
import com.github.kr328.clash.design.util.*
import com.github.kr328.clash.service.model.Profile
import java.util.*

class TaskerSettingsDesign(context: Context) : Design<TaskerSettingsDesign.Request>(context) {
    sealed class Request {
        data class Commit(val settings: TaskerSettings) : Request()
    }

    private val binding = DesignSettingsTaskerBinding
        .inflate(context.layoutInflater, context.root, false)
    private val adapter = TaskerProfileAdapter(context, this::requestActive)

    override val root: View
        get() = binding.root

    private val dummyProfile = ProfileSpec(
        uuid = UUID(0L, 0L),
        name = "使用当前配置",
    )

    suspend fun init(currentSettings: TaskerSettings, profiles: List<Profile>) {
        val selectedProfileUUID = currentSettings.profile ?: dummyProfile.uuid
        val l = mutableListOf(dummyProfile).also {
            profiles.mapTo(it, ProfileSpec.Companion::from)
        }.map {
            if (it.uuid == selectedProfileUUID) {
                it.copy(selected = true)
            } else {
                it
            }
        }

        adapter.apply {
            patchDataSet(this::profiles, l, id = { it.uuid })
        }

        binding.switchView.isChecked = currentSettings.turnOn
    }

    init {
        binding.self = this

        binding.activityBarLayout.applyFrom(context)

        binding.recyclerList.also {
            it.applyLinearAdapter(context, adapter)
        }
    }

    private fun requestActive(profile: ProfileSpec) {
        requests.trySend(Request.Commit(TaskerSettings(
            turnOn = binding.switchView.isChecked,
            profile = profile.uuid.takeUnless { it == dummyProfile.uuid }
        )))
    }
}