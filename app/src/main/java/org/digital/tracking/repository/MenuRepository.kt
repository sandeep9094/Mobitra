package org.digital.tracking.repository

import org.digital.tracking.R
import org.digital.tracking.model.MenuModel

object MenuRepository {

    fun getOtherMenuList(): List<MenuModel> {
        val menuList = ArrayList<MenuModel>()
        menuList.add(MenuModel(R.drawable.ic_baseline_history_24_grey, R.string.title_history_view))
        menuList.add(MenuModel(R.drawable.ic_baseline_replay_24_grey, R.string.title_replay_view))
//        menuList.add(MenuModel(R.drawable.ic_baseline_history_24_grey, R.string.title_advance_history))
//        menuList.add(MenuModel(R.drawable.ic_baseline_stop_24, R.string.title_stop_resume_vehicle))
//        menuList.add(MenuModel(R.drawable.ic_baseline_settings_24_grey, R.string.title_settings))
        menuList.add(MenuModel(R.drawable.ic_baseline_share_24_grey, R.string.title_share_app))
        menuList.add(MenuModel(R.drawable.ic_baseline_star_rate_24, R.string.title_rate_us))
        menuList.add(MenuModel(R.drawable.ic_baseline_link_24_grey, R.string.title_terms_and_conditions))
        menuList.add(MenuModel(R.drawable.ic_baseline_privacy_24_grey, R.string.title_privacy_policy))
        menuList.add(MenuModel(R.drawable.ic_baseline_feedback_24_grey, R.string.title_feedback))
//        menuList.add(MenuModel(R.drawable.ic_baseline_share_24_grey, R.string.title_share_track_link))
//        menuList.add(MenuModel(R.drawable.ic_baseline_settings_24_grey, R.string.title_notification_settings))
//        menuList.add(MenuModel(R.drawable.ic_baseline_language_24_grey, R.string.title_change_language))
//        menuList.add(MenuModel(R.drawable.ic_baseline_backup_24_grey, R.string.title_upload_documents))

        return menuList
    }

    fun getReportsMenuList(): List<MenuModel> {
        val menuList = ArrayList<MenuModel>()
//        menuList.add(MenuModel(R.drawable.ic_baseline_details_24_grey, R.string.title_detail_report))
        menuList.add(MenuModel(R.drawable.ic_social_distance_24, R.string.title_distance_report))
        menuList.add(MenuModel(R.drawable.ic_baseline_calendar_month_24_grey, R.string.title_daily_distance_report))
        menuList.add(MenuModel(R.drawable.ic_baseline_speed_24_grey, R.string.title_over_speed_report))
        menuList.add(MenuModel(R.drawable.ic_baseline_exit_to_app_24_grey, R.string.title_last_location_report))
//        menuList.add(MenuModel(R.drawable.ic_baseline_history_24_grey, R.string.title_fuel_consumption_report))
        menuList.add(MenuModel(R.drawable.ic_baseline_my_location_24_grey, R.string.title_halt_report))
//        menuList.add(MenuModel(R.drawable.ic_summarize_24, R.string.title_summary_report))
        menuList.add(MenuModel(R.drawable.ic_baseline_stop_circle_24_grey, R.string.title_stopage_summary))
//        menuList.add(MenuModel(R.drawable.ic_baseline_calendar_today_24_grey, R.string.title_sensor))

        return menuList
    }

}