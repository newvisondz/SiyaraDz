package com.newvisiondz.sayara.screens.usedcars

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.databinding.FragmentBidCardBinding
import com.newvisiondz.sayara.model.UsedCar

class BidsAdapter(private val clickListener: Listener?) : ListAdapter<UsedCar, BidsAdapter.ViewHolder>(AdsCompareDiffUtil()) {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder.from(viewGroup)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bid = getItem(position)
        holder.bind(bid)
        holder.binding.bidCard.setOnClickListener {
            this.clickListener?.onClick(bid)
        }
    }


    class ViewHolder private constructor(val binding: FragmentBidCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(usedCar: UsedCar) {
            binding.usedCar = usedCar
            binding.executePendingBindings()
        }


        companion object {
            @JvmStatic
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: FragmentBidCardBinding = FragmentBidCardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class Listener(val clickListener: (adver: UsedCar) -> Unit) {
        fun onClick(adver: UsedCar) = clickListener(adver)
    }
}

class AdsCompareDiffUtil :
    DiffUtil.ItemCallback<UsedCar>() {
    override fun areItemsTheSame(oldItem: UsedCar, newItem: UsedCar): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UsedCar, newItem: UsedCar): Boolean {
        return newItem == oldItem
    }
}