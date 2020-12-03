package service

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import model.Floor
import model.People

class Exit {
    suspend fun exitRandomCustomer(floorQueue: List<Floor>) =
            flow {
                while (true) {
                    delay(1000L)
                    val peopleCount = (1..5).random()
                    val filteredFloor = floorQueue.filter { it.currentCustomerSize - it.exitQueueSize >= peopleCount && it.floorNumber > 0 }
                    if (filteredFloor.isNotEmpty())
                        emit(People(peopleCount, 0, filteredFloor.random().floorNumber))
                }
            }
}