package org.digital.tracking.enum

object ReplayViewSpeed {
    //Speed at which new location will push
    const val SLOWER_1X = 6 * 1000L
    const val SLOW_2X = 4 * 1000L
    const val NORMAL_3X = 2 * 1000L
    const val FAST_4X = 1 * 1000L
    const val FASTER_5X = 5 * 100L
//    const val SUPER_FAST_10X = 1 * 100L


    fun getSpeedList(): ArrayList<String> {
        val speedList = ArrayList<String>()
        speedList.add("Slower (1x)")
        speedList.add("Slow (2x)")
        speedList.add("Normal (3x)")
        speedList.add("Fast (4x)")
        speedList.add("Faster (5x)")
//        speedList.add("Super Fast (10x)")
        return speedList
    }
}