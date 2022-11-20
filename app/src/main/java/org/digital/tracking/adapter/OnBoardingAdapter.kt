package org.digital.tracking.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.digital.tracking.view.fragments.OnBoardingFragment


class OnBoardingAdapter(activity: AppCompatActivity, private val itemsCount: Int) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return OnBoardingFragment.getInstance(position)
    }

}