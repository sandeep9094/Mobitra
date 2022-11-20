package org.digital.tracking.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import org.digital.tracking.R
import org.digital.tracking.databinding.FragmentOnBoardingBinding
import org.digital.tracking.utils.setDrawable


class OnBoardingFragment : BaseFragment() {

    private lateinit var binding: FragmentOnBoardingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = requireArguments().getInt(ARG_POSITION)
        val onBoardingTitles = requireContext().resources.getStringArray(R.array.on_boarding_titles)
        val onBoardingImages = getOnBoardAssetsLocation()

        with(binding) {
            onBoardingTitle.text = onBoardingTitles[position]
            bannerImage.setDrawable(requireContext(), onBoardingImages[position])
        }
    }

    private fun getOnBoardAssetsLocation(): List<Int> {
        val onBoardAssets: MutableList<Int> = ArrayList()
        onBoardAssets.add(R.drawable.banner_location_tracking)
        onBoardAssets.add(R.drawable.banner_my_current_location)
        onBoardAssets.add(R.drawable.banner_navigation)
        onBoardAssets.add(R.drawable.banner_data_report)
        return onBoardAssets
    }

    companion object {

        private const val ARG_POSITION = "position"
        fun getInstance(position: Int) = OnBoardingFragment().apply {
            arguments = bundleOf(ARG_POSITION to position)
        }
    }

}