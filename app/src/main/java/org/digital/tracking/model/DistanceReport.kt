package org.digital.tracking.model

data class DistanceReport(
    val point: String,
    val date: String,
    val address: String
) {

    constructor(date: String, address: String) : this("Start Point", date, address)
}
