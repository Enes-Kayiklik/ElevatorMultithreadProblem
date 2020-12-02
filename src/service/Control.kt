package service

import kotlinx.coroutines.delay
import model.People

class Control {
    suspend fun checkRequireElevator(
            loginQueue: List<People>,
            exitQueue: List<People>,
            elevatorCount: Int
    ): Boolean {
        delay(500L)
        return loginQueue.sumBy { it.count } + exitQueue.sumBy { it.count } > 10 * 2 * elevatorCount && elevatorCount < 5
    }
}