package com.newvisiondz.sayara.screens.usedcardetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.databinding.BidItemBinding
import com.newvisiondz.sayara.model.Bid

class BidsAdapter : ListAdapter<Bid, BidsAdapter.ViewHolder>(AdsCompareDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bid = getItem(position)
        holder.bind(bid)
    }
    class ViewHolder private constructor(val binding: BidItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bid: Bid) {
            binding.bid = bid
            binding.executePendingBindings()
        }


        companion object {
            @JvmStatic
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: BidItemBinding = BidItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class AdsCompareDiffUtil :
    DiffUtil.ItemCallback<Bid>() {
    override fun areItemsTheSame(oldItem: Bid, newItem: Bid): Boolean {
        return oldItem.carId == newItem.carId
    }

    override fun areContentsTheSame(oldItem: Bid, newItem: Bid): Boolean {
        return newItem == oldItem
    }
}