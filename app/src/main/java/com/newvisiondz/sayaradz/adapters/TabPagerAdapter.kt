package com.newvisiondz.sayaradz.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.newvisiondz.sayaradz.views.fragments.NewCars
import com.newvisiondz.sayaradz.views.fragments.OldCars
import com.newvisiondz.sayaradz.views.fragments.Profile

class TabPagerAdapter(fm: FragmentManager) :
    androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        return when (position) {
            0 -> NewCars()
            1 -> OldCars()
            2 -> Profile()
            else -> null
        }
    }

    override fun getCount(): Int {
        return 3
    }
}
