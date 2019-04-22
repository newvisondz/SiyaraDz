package com.newvisiondz.sayaradz.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.newvisiondz.sayaradz.views.fragments.NewCars
import com.newvisiondz.sayaradz.views.fragments.OldCars
import com.newvisiondz.sayaradz.views.fragments.Profile

class TabPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

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
