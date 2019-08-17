package com.newvisiondz.sayara.screens.myusedcars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentUsedCarsBinding
import com.newvisiondz.sayara.screens.bids.BidsAdapter

class UsedCars : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentUsedCarsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_used_cars, container, false)
        val application = requireNotNull(this.activity).application

        val mViewModel =
            ViewModelProviders.of(this, UsedCarsViewModelFactory(application)).get(UsedCarsViewModel::class.java)
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this
        binding.myBidsList.adapter = BidsAdapter(null)
        return binding.root
    }
}
