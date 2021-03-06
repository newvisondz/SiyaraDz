package com.newvisiondz.sayara.screens.tabs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentTabsBinding

class Tabs : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTabsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tabs, container, false)
        val adapter = TabPagerAdapter(childFragmentManager)
        binding.pager.adapter = adapter
        binding.pager.offscreenPageLimit = 2
        binding.pager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout)
        )
        binding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        return binding.root
    }
}
