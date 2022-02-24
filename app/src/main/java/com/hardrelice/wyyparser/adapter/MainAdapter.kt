package com.hardrelice.wyyparser.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MainAdapter(fm: FragmentManager, fragments:ArrayList<Fragment>):
    FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments:ArrayList<Fragment>? = fragments
    override fun getCount(): Int = fragments!!.size

    override fun getItem(position: Int): Fragment = fragments!![position]
}