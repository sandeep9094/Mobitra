package org.digital.tracking.repository

import com.mobitra.tracking.ReceiveLocationSubscription

object MapsDummyRepository {

//    fun getHistoryLocationData(vehicleNumber: String): ArrayList<ReceiveLocationSubscription.ReceiveLocation> {
//        val list = ArrayList<ReceiveLocationSubscription.ReceiveLocation>()
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69010,74.97333, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69065,74.97427, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69116,74.97517, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69174,74.97603, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69207,74.97659, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69240,74.97715, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69253,74.97749, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69277,74.97785, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69298,74.97819, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69312,74.97840, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69323,74.97860, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69338,74.97885, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69356,74.97913, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69369,74.97933, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69378,74.97949, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69395,74.97974, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69407,74.97995, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69417,74.98014, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69430,74.98034, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69441,74.98053, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69454,74.98073, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69463,74.98088, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69473,74.98104, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69486,74.98123, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69493,74.98136, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69503,74.98155, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69513,74.98169, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69522,74.98185, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69533,74.98202, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69545,74.98217, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69554,74.98235, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69564,74.98252, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69573,74.98267, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69584,74.98286, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69594,74.98300, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69604,74.98316, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69617,74.98339, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69629,74.98357, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69635,74.98368, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69642,74.98382, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69653,74.98394, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69660,74.98408, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69671,74.98427, "", "", 0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69688,74.98451, "", "", 0
//            )
//        )
//
//        return list
//    }

    //
//
//    fun getReceiveLocationData(vehicleNumber: String): ArrayList<ReceiveLocationSubscription.ReceiveLocation> {
//        val list = ArrayList<ReceiveLocationSubscription.ReceiveLocation>()
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69010,74.97333, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69065,74.97427, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69116,74.97517, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69174,74.97603, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69207,74.97659, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69240,74.97715, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69253,74.97749, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69277,74.97785, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69298,74.97819, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69312,74.97840, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69323,74.97860, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69338,74.97885, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69356,74.97913, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69369,74.97933, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69378,74.97949, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69395,74.97974, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69407,74.97995, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69417,74.98014, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69430,74.98034, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69441,74.98053, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69454,74.98073, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69463,74.98088, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69473,74.98104, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69486,74.98123, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69493,74.98136, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69503,74.98155, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69513,74.98169, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69522,74.98185, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69533,74.98202, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69545,74.98217, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69554,74.98235, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69564,74.98252, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69573,74.98267, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69584,74.98286, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69594,74.98300, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69604,74.98316, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69617,74.98339, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69629,74.98357, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69635,74.98368, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69642,74.98382, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69653,74.98394, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69660,74.98408, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69671,74.98427, "", "", 0.0, 0,0.0
//            )
//        )
//        list.add(
//            ReceiveLocationSubscription.ReceiveLocation(
//                "", "", 31.69688,74.98451, "", "", 0.0, 0,0.0
//            )
//        )

//        return list
//    }
}