package service

import model.Floor

class Control {
    fun checkRequireElevator(
        floorQueue: List<Floor>,
        elevatorCount: Int
    ) = floorQueue.sumBy { it.exitQueueSize } >= 10 * 2 * elevatorCount &&
            elevatorCount < 5
}