package com.newvisiondz.sayara.screens.myoffers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.UserBidItemBinding
import com.newvisiondz.sayara.model.UserBid
import com.newvisiondz.sayara.screens.versions.SliderAdapter
import com.smarteist.autoimageslider.SliderView
import java.lang.Exception

class UserBidAdapter(private var userBidsList: List<UserBid>) :
    RecyclerView.Adapter<UserBidAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int = userBidsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userBid = userBidsList[position]
        holder.bind(userBid)
    }

    class ViewHolder private constructor(val binding: UserBidItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val slider: SliderView = binding.imageSlider
        private val context = binding.root.context
        fun bind(userBid: UserBid) {
            try {
                slider.sliderAdapter = SliderAdapter(
                    context,
                    userBid.usedCar.images,
                    context!!.getString(R.string.baseUrl)
                )
            } catch (e: NullPointerException) {
               Toast.makeText(context,"No Images found",Toast.LENGTH_SHORT).show()
            }
            binding.userBid = userBid
            binding.executePendingBindings()
        }


        companion object {
            @JvmStatic
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: UserBidItemBinding =
                    UserBidItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}