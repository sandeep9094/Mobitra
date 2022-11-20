package org.digital.tracking.view.activity

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.adapter.OnBoardingAdapter
import org.digital.tracking.auth.UserAuthActivity
import org.digital.tracking.databinding.ActivityOnboardingBinding
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.utils.navigateToActivity
import org.digital.tracking.utils.setDrawable
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: ActivityOnboardingBinding

    private var onBoardingPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            updateCircleMarker(binding, position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numberOfScreens = resources.getStringArray(R.array.on_boarding_titles).size
        val onBoardingAdapter = OnBoardingAdapter(this, numberOfScreens)
        binding.onBoardingViewPager.adapter = onBoardingAdapter
        binding.onBoardingViewPager.registerOnPageChangeCallback(onBoardingPageChangeCallback)

        binding.continueButton.setOnClickListener(this)

    }

    private fun updateCircleMarker(binding: ActivityOnboardingBinding, position: Int) {
        when (position) {
            0 -> {
                binding.circle1.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange)
                binding.circle2.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
                binding.circle3.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
                binding.circle4.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
            }
            1 -> {
                binding.circle1.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
                binding.circle2.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange)
                binding.circle3.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
                binding.circle4.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
            }
            2 -> {
                binding.circle1.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
                binding.circle2.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
                binding.circle3.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange)
                binding.circle4.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
            }
            3 -> {
                binding.circle1.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
                binding.circle2.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
                binding.circle3.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange_outlined)
                binding.circle4.setDrawable(this@OnboardingActivity, R.drawable.bg_circle_orange)
            }

        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.continueButton.id -> {
                val currentPosition = binding.onBoardingViewPager.currentItem
                val numberOfScreens = resources.getStringArray(R.array.on_boarding_titles).size - 1
                if (currentPosition == numberOfScreens) {
                    openNextScreen()
                    return
                }
                binding.onBoardingViewPager.setCurrentItem(currentPosition.inc(), true)
            }
        }
    }

    private fun openNextScreen() {
        sharedPrefs.isUserFirstTime = false
        navigateToActivity(UserAuthActivity::class.java)
    }

}