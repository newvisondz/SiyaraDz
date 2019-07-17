package com.newvisiondz.sayara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.databinding.CompareItemBinding
import com.newvisiondz.sayara.model.VersionCompare

class CompareVersionAdapter : ListAdapter<VersionCompare, CompareVersionAdapter.ViewHolder>(VersionCompareDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: CompareItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: VersionCompare) {
            binding.versionCompare = option
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CompareItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class VersionCompareDiffUtil :
    DiffUtil.ItemCallback<VersionCompare>() {
    override fun areItemsTheSame(oldItem: VersionCompare, newItem: VersionCompare): Boolean {
        return oldItem.optionName == newItem.optionName
    }

    override fun areContentsTheSame(oldItem: VersionCompare, newItem: VersionCompare): Boolean {
        return newItem == oldItem
    }
}