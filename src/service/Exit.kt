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
                    val size = (1..5).random()
                    val filteredFloor = floorQueue.filter { it.currentCustomerSize >= size }
                    if (filteredFloor.isNotEmpty())
                        emit(People(size, 0, filteredFloor.random().floorNumber))
                }
            }
}