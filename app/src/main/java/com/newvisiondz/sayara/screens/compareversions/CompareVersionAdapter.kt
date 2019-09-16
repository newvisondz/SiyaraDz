package com.newvisiondz.sayara.screens.compareversions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.VersionCompare
import kotlinx.android.synthetic.main.compare_item.view.*

class CompareVersionAdapter(var items:MutableList<VersionCompare>) : RecyclerView.Adapter<CompareVersionAdapter.ViewHolder>() {
    override fun getItemCount(): Int =items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val version=items[position]
        holder.bind(version)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val value: TextView =itemView.spinner_v1
        private val optionName: TextView =itemView.option_name
        fun bind(option: VersionCompare) {
            value.text = option.firstValue
            optionName.text=option.optionName
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = LayoutInflater.from(parent.context).inflate(R.layout.compare_item,parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

