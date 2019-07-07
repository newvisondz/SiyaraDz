package com.newvisiondz.sayara.adapters.versionadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.Value
import kotlinx.android.synthetic.main.option_preview_item.view.*

class ItemPreviewAdapter(private val previewListValues: MutableList<Value>) :
    RecyclerView.Adapter<ItemPreviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun getItemCount(): Int = previewListValues.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(previewListValues[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val previewKey = itemView.preview_key
        val previewValue = itemView.preview_value

        fun bind(value: Value) {
            previewKey.text = value.value
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.option_preview_item, parent, false)
                return ViewHolder(view)
            }

        }
    }
}