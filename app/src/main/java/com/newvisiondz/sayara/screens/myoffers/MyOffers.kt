package com.newvisiondz.sayara.screens.myoffers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentMyOffersBinding
import com.newvisiondz.sayara.model.UserBid
import com.newvisiondz.sayara.screens.usedcardetails.BidsAdapter

class MyOffers : Fragment() {
    private val userBidsList= mutableListOf<UserBid>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentMyOffersBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_offers, container, false)
        val application = requireNotNull(this.activity).application
        val viewModel =
            ViewModelProviders.of(this, UsedCarsViewModelFactory(application)).get(MyOffersViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.userBidsList.adapter=UserBidAdapter(userBidsList)
        viewModel.userBidsList.observe(this, Observer { list->
            userBidsList.clear()
            userBidsList.addAll(list)
            (binding.userBidsList.adapter as UserBidAdapter).notifyDataSetChanged()
        })

        return binding.root
    }

}
