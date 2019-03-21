package com.newvisiondz.sayaradz.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.newvisiondz.sayaradz.views.fragments.NewCars
import com.newvisiondz.sayaradz.views.fragments.OldCars
import com.newvisiondz.sayaradz.views.fragments.Profile

class TabPagerAdapter(fm: FragmentManager, private var tabCount: Int) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return NewCars()
            1 -> return OldCars()
            2 -> return Profile()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return 3
    }
}
