package com.example.siyaradz.views

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import com.example.siyaradz.R
import com.example.siyaradz.views.fragments.NeufFragment
import com.example.siyaradz.views.fragments.OccasionFragment
import com.example.siyaradz.views.fragments.ProfileFragment
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter
        container.offscreenPageLimit =3
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {

            when (position) {
                0 -> {
                    Log.i("position",position.toString()+"neuf")
                    return NeufFragment()
                }
                1 -> {
                    Log.i("position",position.toString()+"occasion")
                    return OccasionFragment()
                }
                2 -> {
                    Log.i("position",position.toString()+"profile")
                    return ProfileFragment()
                }
            }
            return null
        }

        override fun getCount(): Int {
            return 3
        }
    }

}
