package com.newvisiondz.sayara.screens.myorders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentMyOrdersBinding

class MyOrders : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMyOrdersBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false)
        val application = requireNotNull(this.activity).application
        val viewModel =
            ViewModelProviders.of(this, MyOrdersViewModelFactory(application))
                .get(MyOrdersViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.commandeList.adapter = MyOrdersAdapter(MyOrdersAdapter.Listener {

        })

        return binding.root
    }
}
