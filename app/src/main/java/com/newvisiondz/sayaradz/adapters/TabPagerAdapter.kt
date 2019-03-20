package com.newvisiondz.sayaradz.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.newvisiondz.sayaradz.views.fragments.*

class TabPagerAdapter(fm: FragmentManager, private var tabCount: Int) :
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
        return tabCount
    }
}
