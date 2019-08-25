package com.newvisiondz.sayara.screens.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.newvisiondz.sayara.screens.usedcars.UsedCars
import com.newvisiondz.sayara.screens.brands.Brands
import com.newvisiondz.sayara.screens.profile.Profile

class TabPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> Brands()
            1 -> UsedCars()
            2 -> Profile()
            else -> null
        }!!
    }

    override fun getCount(): Int {
        return 3
    }
}
