package com.newvisiondz.sayara.screens.modeltabs


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout

import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentModelTabsBinding

class ModelTabs : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentModelTabsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_model_tabs, container, false)
        val adapter = ModelTabsPagerAdapter(childFragmentManager,arguments)
        binding.modelTabsPager.adapter = adapter
        binding.modelTabsPager.offscreenPageLimit = 2
        binding.modelTabsPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout)
        )
        binding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.modelTabsPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        return binding.root
    }


}
