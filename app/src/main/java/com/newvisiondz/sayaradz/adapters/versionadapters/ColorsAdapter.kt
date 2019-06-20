package com.newvisiondz.sayaradz.adapters.versionadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayaradz.databinding.RadioItemColorBinding
import com.newvisiondz.sayaradz.model.Color


class ColorsAdapter(
    private var options: MutableList<Color>
) :
    RecyclerView.Adapter<ColorsAdapter.ViewHolder>() {


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


    class ViewHolder(val binding: RadioItemColorBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            sSelected = adapterPosition
            sClickListener?.onColorClickListner(adapterPosition, view!!)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RadioItemColorBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(option: Color, position: Int) {
            binding.color = option
            binding.position = position
            binding.executePendingBindings()
        }
    }

    fun selectedItem() {
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: SingleClickListener) {
        sClickListener = clickListener
    }

    interface SingleClickListener {
        fun onColorClickListner(position: Int, view: View)
    }
}