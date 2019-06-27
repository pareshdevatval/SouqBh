package com.souqbh.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class IntroductionViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val mFragmentList = ArrayList<Fragment>()

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}