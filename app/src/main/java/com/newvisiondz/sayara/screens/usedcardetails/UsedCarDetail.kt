package com.newvisiondz.sayara.screens.usedcardetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentUsedCarDetailBinding
import com.newvisiondz.sayara.screens.versions.SliderAdapter


class UsedCarDetail : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentUsedCarDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_used_car_detail, container, false)
        val application = requireNotNull(activity).application
        binding.bidsCarList.adapter=BidsAdapter()
        val viewModelFactory = UsedCarDetailViewModelFactory(application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(UsedCarDetailViewModel::class.java)
        binding.viewModel=viewModel
        val usedCar=UsedCarDetailArgs.fromBundle(arguments!!).usedCar
        binding.usedCar=usedCar
        binding.lifecycleOwner=this
        binding.executePendingBindings()
        val dividerItemDecoration = DividerItemDecoration(
            binding.bidsCarList.context,
            LinearLayout.HORIZONTAL
        )
        binding.bidsCarList.addItemDecoration(dividerItemDecoration)
        viewModel.getAllBidsOfCar(usedCar.id)
        binding.imageSlider.sliderAdapter =SliderAdapter(context!!, usedCar.images,context!!.getString(R.string.baseUrl))
        return binding.root
    }
}
