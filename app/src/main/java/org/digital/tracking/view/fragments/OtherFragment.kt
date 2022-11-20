package org.digital.tracking.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.digital.tracking.R
import org.digital.tracking.adapter.MenuAdapter
import org.digital.tracking.databinding.FragmentOtherBinding
import org.digital.tracking.model.MenuModel
import org.digital.tracking.repository.MenuRepository
import org.digital.tracking.utils.*
import org.digital.tracking.view.activity.HistoryViewActivity

class OtherFragment : BaseFragment(), MenuAdapter.Listener {

    private lateinit var binding: FragmentOtherBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOtherBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomeToolbar(binding.root)

        initOtherMenuList()
    }

    private fun initOtherMenuList() {
        val menuList = MenuRepository.getOtherMenuList()
        binding.otherMenuRecyclerView.setHasFixedSize(true)
        binding.otherMenuRecyclerView.adapter = MenuAdapter(menuList, this)
        binding.otherMenuRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.otherMenuRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    override fun onMenuItemClicked(menuModel: MenuModel) {
        when (menuModel.titleResId) {
            R.string.title_stop_resume_vehicle -> {
                navigateToFragment(R.id.action_navigation_other_to_stopResumeVehicleFragment)
            }
            R.string.title_history_view,
            R.string.title_advance_history -> {
                val intent = Intent(context, HistoryViewActivity::class.java)
                intent.putExtra(Constants.INTENT_KEY_TITLE, getString(menuModel.titleResId))
                intent.putExtra(Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE, Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_HISTORY)
                context.navigateToActivity(intent)
            }
            R.string.title_replay_view -> {
                val intent = Intent(context, HistoryViewActivity::class.java)
                intent.putExtra(Constants.INTENT_KEY_TITLE, getString(menuModel.titleResId))
                intent.putExtra(Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE, Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_REPLAY)
                context.navigateToActivity(intent)
            }
            R.string.title_share_app -> {
                PlayStoreUtil.shareApp(activity)
            }
            R.string.title_rate_us -> {
                PlayStoreUtil.rateApp(activity)
            }
            R.string.title_terms_and_conditions -> {
                PlayStoreUtil.openLinkInBrowsers(activity, Constants.URL_TERMS_AND_CONDITIONS)
            }
            R.string.title_privacy_policy -> {
                PlayStoreUtil.openLinkInBrowsers(activity, Constants.URL_PRIVACY_POLICY)
            }
            R.string.title_feedback -> {
                PlayStoreUtil.helpAndFeedbackViaEmail(activity, Constants.HelpAndSupport.EMAIL, Constants.HelpAndSupport.EMAIL_SUBJECT)
            }
            else -> context.showToast(menuModel.titleResId)
        }

    }


}