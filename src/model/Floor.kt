package model

data class Floor(
    var currentCustomerSize: Int = 0,
    var exitQueueSize: Int = 0,
    val floorNumber: Int = 0
)