package service

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import model.Floor
import model.People
import utils.filtered

class Exit {
    suspend fun exitRandomCustomer(floorQueue: List<Floor>) =
        flow {
            while (true) {
                delay(1000L)
                floorQueue.filtered { it.currentCustomerSize > 0 && it.floorNumber > 0 }?.random()?.let { randomFloor ->
                    emit(People(minOf((1..5).random(), randomFloor.currentCustomerSize), 0, randomFloor.floorNumber))
                }
            }
        }
}