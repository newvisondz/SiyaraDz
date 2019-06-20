package com.newvisiondz.sayaradz.adapters.versionadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayaradz.databinding.RadioEnginePowerItemBinding
import com.newvisiondz.sayaradz.model.Value

class EnginePowerAdapter(val list: MutableList<Value>) : RecyclerView.Adapter<EnginePowerAdapter.ViewHolder>() {

    companion object {
        private var sClickListener: SingleClickListener? = null
        @JvmStatic
        var sSelected = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = list[position]
        holder.bind(option, position)
    }

    class ViewHolder(val binding: RadioEnginePowerItemBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        override fun onClick(view: View?) {
            sSelected = adapterPosition
            sClickListener?.onEnginPowerClickListner(adapterPosition, view!!)
        }

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(option: Value, position: Int) {
            binding.value = option
            binding.position = position
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RadioEnginePowerItemBinding.inflate(layoutInflater, parent, false)
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
        fun onEnginPowerClickListner(position: Int, view: View)
    }
}


