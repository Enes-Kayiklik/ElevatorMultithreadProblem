package service

import kotlinx.coroutines.delay
import model.Floor

class Control {
    suspend fun checkRequireElevator(
            floorQueue: List<Floor>,
            elevatorCount: Int
    ): Boolean {
        delay(30L)
        return floorQueue.sumBy { it.exitQueueSize } >= 10 * 2 * elevatorCount && elevatorCount < 5
    }
}