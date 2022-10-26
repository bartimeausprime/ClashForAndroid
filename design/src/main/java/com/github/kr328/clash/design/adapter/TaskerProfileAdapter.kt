package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.databinding.AdapterTaskerProfileBinding
import com.github.kr328.clash.design.model.ProfileSpec
import com.github.kr328.clash.design.util.layoutInflater

class TaskerProfileAdapter(
    private val context: Context,
    private val onClicked: (ProfileSpec) -> Unit,
) : RecyclerView.Adapter<TaskerProfileAdapter.Holder>() {
    class Holder(val binding: AdapterTaskerProfileBinding) : RecyclerView.ViewHolder(binding.root)

    var profiles: List<ProfileSpec> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            AdapterTaskerProfileBinding
                .inflate(context.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = profiles[position]
        val binding = holder.binding

        if (current === binding.profile)
            return

        binding.profile = current
        binding.setClicked {
            onClicked(current)
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }
}