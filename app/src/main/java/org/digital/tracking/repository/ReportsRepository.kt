package org.digital.tracking.repository

import org.digital.tracking.model.*
import org.digital.tracking.utils.Constants

object ReportsRepository {

    val distanceReportList: ArrayList<DistanceReport> = ArrayList()
    val overSpeedReportList: ArrayList<OverSpeedReport> = ArrayList()
    val lastLocationReportList: ArrayList<LastLocationReport> = ArrayList()
    val haltReportList: ArrayList<HaltReport> = ArrayList()

    init {
//        initDummyReports()
    }

    private fun getDummyDistanceReport(): List<DistanceReport> {
        val distanceReportList: ArrayList<DistanceReport> = ArrayList()
//
//        distanceReportList.add(
//            DistanceReport(
//                "24-08-2022 14:02:02",
//                "B/22, Harshad Chambers, Odhav Road, Opp Vallabhnagar High School, Odhav,Ahmedabad,Gujarat"
//            )
//        )
//        distanceReportList.add(
//            DistanceReport(
//                "07-08-2012 05:32:49",
//                "Bhaira Bus Stop, National Highway 22, Himachal Pradesh 172001, India"
//            )
//        )
//        distanceReportList.add(
//            DistanceReport(
//                "26-08-2022 14:42:04",
//                "Unnamed Road, Tura Mandi, Sai Baba Enclave, Masudabad, Najafgarh, Delhi, 110043, India"
//            )
//        )
//        distanceReportList.add(
//            DistanceReport(
//                "23-08-2022 20:31:57",
//                "Shop No.19, Orchid Plaza, R.t. Rd, Bhd Win Sales (rajashree Talkies), Dahisar, Maharashtra 400068, India"
//            )
//        )
//        distanceReportList.add(
//            DistanceReport(
//                "12-08-2022 03:43:15",
//                "3/4, Sambava Chambers, 20 , Sir P M Road, Fort, Mumbai, Maharashtra 400001, India"
//            )
//        )

        return distanceReportList
    }

    private fun getDummyOverSpeedReport(): List<OverSpeedReport> {
        val overSpeedReportList: ArrayList<OverSpeedReport> = ArrayList()

//        overSpeedReportList.add(
//            OverSpeedReport(
//                "24-08-2022 14:02:02",
//                "B/22, Harshad Chambers, Odhav Road, Opp Vallabhnagar High School, Odhav,Ahmedabad,Gujarat",
//                "70"
//            )
//        )
//        overSpeedReportList.add(
//            OverSpeedReport(
//                "07-08-2012 05:32:49",
//                "Bhaira Bus Stop, National Highway 22, Himachal Pradesh 172001, India",
//                "89"
//            )
//        )
//        overSpeedReportList.add(
//            OverSpeedReport(
//                "26-08-2022 14:42:04",
//                "Unnamed Road, Tura Mandi, Sai Baba Enclave, Masudabad, Najafgarh, Delhi, 110043, India",
//                "62"
//            )
//        )
//        overSpeedReportList.add(
//            OverSpeedReport(
//                "23-08-2022 20:31:57",
//                "Shop No.19, Orchid Plaza, R.t. Rd, Bhd Win Sales (rajashree Talkies), Dahisar, Maharashtra 400068, India",
//                "81"
//            )
//        )
//        overSpeedReportList.add(
//            OverSpeedReport(
//                "12-08-2022 03:43:15",
//                "3/4, Sambava Chambers, 20 , Sir P M Road, Fort, Mumbai, Maharashtra 400001, India",
//                "55"
//            )
//        )

        return overSpeedReportList
    }

    private fun getDummyLastLocationReport(): List<LastLocationReport> {
        val lastLocationReportList = ArrayList<LastLocationReport>()
//        lastLocationReportList.apply {
//            add(
//                LastLocationReport(
//                    "HP52B1415",
//                    "24-08-2022 14:02:02",
//                    "B/22, Harshad Chambers, Odhav Road, Opp Vallabhnagar High School, Odhav,Ahmedabad,Gujarat",
//                    Constants.DEFAULT_INDIA_LAT,
//                    Constants.DEFAULT_INDIA_LONG
//                )
//            )
//            add(
//                LastLocationReport(
//                    "OD15S1116",
//                    "07-08-2012 05:32:49",
//                    "Bhaira Bus Stop, National Highway 22, Himachal Pradesh 172001, India",
//                    Constants.DEFAULT_INDIA_LAT,
//                    Constants.DEFAULT_INDIA_LONG
//                )
//            )
//            add(LastLocationReport("TS07JB3264", "26-08-2022 14:42:04", "Sai Baba Enclave, Masudabad, Najafgarh, Delhi, 110043, India",
//                Constants.DEFAULT_INDIA_LAT,
//                Constants.DEFAULT_INDIA_LONG))
//            add(
//                LastLocationReport(
//                    "DL1VC1872",
//                    "23-08-2022 20:31:57",
//                    "Orchid Plaza, R.t. Rd, Bhd Win Sales (rajashree Talkies), Dahisar, Maharashtra 400068, India",
//                    Constants.DEFAULT_INDIA_LAT,
//                    Constants.DEFAULT_INDIA_LONG
//                )
//            )
//            add(LastLocationReport("LWK-2324", "12-08-2022 03:43:15", "Sir P M Road, Fort, Mumbai, Maharashtra 400001, India",
//                Constants.DEFAULT_INDIA_LAT,
//                Constants.DEFAULT_INDIA_LONG))
//        }

        return lastLocationReportList
    }

    private fun getDummyHaltReport(): List<HaltReport> {
        return emptyList()
//        return ArrayList<HaltReport>().apply {
//            add(HaltReport("23-08-2022 20:31:57","234","Dahisar, Maharashtra 400068, India"))
//            add(HaltReport("07-08-2012 05:32:49","71","National Highway 22, Himachal Pradesh 172001, India"))
//            add(HaltReport("24-08-2022 14:02:02","1013", "Odhav,Ahmedabad,Gujarat"))
//            add(HaltReport("26-08-2022 14:42:04","84", "Masudabad, Najafgarh, Delhi, 110043, India"))
//        }

    }

}