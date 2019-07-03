package com.deviprasaddayal.depiciat.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import java.util.ArrayList

/**
 * Created by HP on 7/28/2017.
 */

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var fragmentArrayList = ArrayList<Fragment>()
    var titleArrayList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentArrayList.add(fragment)
        titleArrayList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        try {
            return titleArrayList[position]
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }
}
