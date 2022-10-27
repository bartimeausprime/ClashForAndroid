package com.github.kr328.clash

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import com.github.kr328.clash.common.log.Log
import com.github.kr328.clash.design.TaskerSettingsDesign
import com.github.kr328.clash.design.model.TaskerSettings
import com.github.kr328.clash.service.remote.IProfileManager
import com.github.kr328.clash.util.withProfile
import kotlinx.coroutines.isActive
import kotlinx.coroutines.selects.select
import java.util.*
import com.twofortyfouram.locale.api.Intent as ApiIntent

class TaskerSettingsActivity : BaseActivity<TaskerSettingsDesign>() {
    override suspend fun main() {
        val intent = intent ?: return finish()

        val taskerSettings = TaskerSettings.from(intent)

        val design = TaskerSettingsDesign(this)

        design.init(taskerSettings, withProfile { queryAll() })

        setContentDesign(design)

        while (isActive) {
            select<Unit> {
                design.requests.onReceive {
                    when (it) {
                        is TaskerSettingsDesign.Request.Commit -> {
                            val res = withProfile {
                                it.settings.toIntent(this@TaskerSettingsActivity, this)
                            }
                            setResult(Activity.RESULT_OK, res)
                            finish()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val KEY_TURN_ON = "turn_on"
        private const val KEY_PROFILE = "profile"

        fun TaskerSettings.Companion.from(intent: Intent): TaskerSettings {
            val bundle = intent.getBundleExtra(ApiIntent.EXTRA_BUNDLE)

            Log.i("${bundle?.getSerializable(KEY_PROFILE)}")

            return TaskerSettings(
                turnOn = bundle?.getBoolean(KEY_TURN_ON) ?: true,
                profile = bundle?.getString(KEY_PROFILE)?.let(UUID::fromString),
            )
        }

        suspend fun TaskerSettings.toIntent(context: Context, pm: IProfileManager): Intent {
            val profile = profile
            val desc = if (profile == null) {
                if (turnOn) {
                    context.getString(R.string.start_clash)
                } else {
                    context.getString(R.string.stop_clash)
                }
            } else {
                val p = pm.queryByUUID(profile)?.name ?: profile.toString()

                if (turnOn) {
                    context.getString(R.string.format_start_and_switch_to_profile, p)
                } else {
                    context.getString(R.string.format_switch_to_profile, p)
                }
            }

            return Intent().putExtra(
                ApiIntent.EXTRA_BUNDLE, bundleOf(
                    KEY_TURN_ON to turnOn,
                    KEY_PROFILE to profile?.toString(),
                )
            ).putExtra(ApiIntent.EXTRA_STRING_BLURB, desc)
        }
    }
}