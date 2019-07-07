package com.newvisiondz.sayara.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.newvisiondz.sayara.views.fragments.Bids
import com.newvisiondz.sayara.views.fragments.Brands
import com.newvisiondz.sayara.views.fragments.Profile

class TabPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        return when (position) {
            0 -> Brands()
            1 -> Bids()
            2 -> Profile()
            else -> null
        }
    }

    override fun getCount(): Int {
        return 3
    }
}
