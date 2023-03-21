package org.bmsk.webtoon.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.bmsk.webtoon.MainActivity
import org.bmsk.webtoon.WebViewFragment

class ViewPagerAdapter(private val mainActivity: MainActivity) :
    FragmentStateAdapter(mainActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                return WebViewFragment(position)
            }
            1 -> {
                return WebViewFragment(position)
            }
            else -> {
                return WebViewFragment(position)
            }
        }
    }

}