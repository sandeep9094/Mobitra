package org.digital.tracking.utils

object ValuesFormatter {

    fun formatDoubleValue(value: Double?): Double {
        var speedDouble = 0.0
        if (value == null || value <= 0) {
            return speedDouble
        }
        speedDouble = "%.2f".format(value).toDouble()
        return speedDouble
    }

}