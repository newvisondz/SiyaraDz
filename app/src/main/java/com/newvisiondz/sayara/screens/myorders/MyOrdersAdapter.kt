package com.newvisiondz.sayara.screens.myorders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.databinding.OrderItemBinding
import com.newvisiondz.sayara.model.CommandConfirmed

class MyOrdersAdapter(private val clickListener: Listener?) :
    ListAdapter<CommandConfirmed, MyOrdersAdapter.ViewHolder>(AdsCompareDiffUtil()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder.from(viewGroup)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commande = getItem(position)
        holder.bind(commande)
        holder.binding.commandePay.setOnClickListener {
            this.clickListener?.onClick(commande)
        }
    }


    class ViewHolder private constructor(val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(usedCar: CommandConfirmed) {
            binding.commade = usedCar
            binding.executePendingBindings()
        }


        companion object {
            @JvmStatic
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: OrderItemBinding =
                    OrderItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class Listener(val clickListener: (command: CommandConfirmed) -> Unit) {
        fun onClick(command: CommandConfirmed) = clickListener(command)
    }
}


class AdsCompareDiffUtil :
    DiffUtil.ItemCallback<CommandConfirmed>() {
    override fun areItemsTheSame(oldItem: CommandConfirmed, newItem: CommandConfirmed): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CommandConfirmed, newItem: CommandConfirmed): Boolean {
        return newItem == oldItem
    }
}