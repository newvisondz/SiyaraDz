package com.newvisiondz.sayara.screens.myusedcars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentUsedCarsBinding
import com.newvisiondz.sayara.screens.bids.BidsAdapter
import com.newvisiondz.sayara.utils.isOnline


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


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (isOnline(context!!)) {
                    mViewModel.deleteUsedCarAd(viewHolder.adapterPosition)
                } else Toast.makeText(context, "You'll have to be online to delete permanantly !", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(binding.myBidsList)


        return binding.root
    }
}
