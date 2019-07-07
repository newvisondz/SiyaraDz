package com.newvisiondz.sayara.adapters.versionadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.databinding.RadioItemEngineBinding
import com.newvisiondz.sayara.model.Value

class EngineAdapter(private var options: MutableList<Value>) :
    RecyclerView.Adapter<EngineAdapter.ViewHolder>() {
    companion object {
        private var sClickListener: SingleClickListener? = null
        @JvmStatic
        var sSelected = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int = options.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option, position)
    }

    class ViewHolder private constructor(val binding: RadioItemEngineBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(option: Value, position: Int) {
            binding.value = option
            binding.position = position
            binding.executePendingBindings()
        }

        override fun onClick(view: View?) {
            sSelected = adapterPosition
            sClickListener?.onEnginClickListner(adapterPosition, view!!)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RadioItemEngineBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    fun selectedItem() {
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: SingleClickListener) {
        sClickListener = clickListener
    }

    interface SingleClickListener {
        fun onEnginClickListner(position: Int, view: View)
    }
}