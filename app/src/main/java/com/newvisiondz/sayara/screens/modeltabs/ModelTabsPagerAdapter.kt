package com.newvisiondz.sayara.screens.modeltabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.newvisiondz.sayara.screens.datasheet.DataSheetFragment
import com.newvisiondz.sayara.screens.versions.Versions

class ModelTabsPagerAdapter(
    fm: FragmentManager,
    arguments: Bundle?
) :
    FragmentPagerAdapter(fm) {
    private val args=arguments

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> Versions(args)
            1 -> DataSheetFragment(args)
            else -> null
        }!!
    }

    override fun getCount(): Int {
        return 2
    }
}