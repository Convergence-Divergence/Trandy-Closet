package com.example.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PhotoPagerAdapter(fm: FragmentManager, val uriList: List<String>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return PhotoFragment.newInstance(uriList[position])
    }
    override fun getCount(): Int = uriList.size
}