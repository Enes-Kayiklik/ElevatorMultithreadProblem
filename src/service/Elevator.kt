package service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.Floor
import model.People
import utils.createThread

data class Elevator(
        val loginQueue: List<People> = listOf(People()),
        val exitQueue: List<People> = listOf(People()),
        val floorQueue: List<Floor> = listOf(Floor()),
        val customersInElevator: MutableList<People> = mutableListOf(),
        val name: Int,
        var currentFloor: Int = 0,
        var isAlive: Boolean = false
) {
    private val elevatorThread = createThread(name.toString())
    private var isGoingUp = true
    private var currentSize = 0

    init {
        callElevator()
    }

    fun callElevator() {
        CoroutineScope(elevatorThread).launch {
            startElevator()
        }
    }

    private suspend fun startElevator() {
        while (isAlive) {
            delay(200L)
            customersInElevator.find { it.targetFloor == currentFloor }.also {
                customersInElevator.remove(it)
                floorQueue[currentFloor].currentCustomerSize += it?.count ?: 0
                currentSize -= it?.count ?: 0
            }

            if (currentFloor == 0)
                getCustomersInTheElevator()
            else if (!isGoingUp)
                getExitQueue()

            if (isGoingUp) {
                currentFloor++
                if (currentFloor == 4) isGoingUp = false
            } else {
                currentFloor--
                if (currentFloor == 0) isGoingUp = true
            }
        }
    }

    private fun getExitQueue() {
        if (currentSize < 10 && exitQueue.any { it.currentFloor == currentFloor }) {
            val people = exitQueue.firstOrNull() ?: People()
            if (people.count > 0) {
                floorQueue[currentFloor].currentCustomerSize -= minOf(people.count, 10 - currentSize)
                customersInElevator.add(People(minOf(people.count, 10 - currentSize), people.targetFloor))
                val lastIncluded = minOf(people.count, 10 - currentSize)
                currentSize += lastIncluded
                people.count = maxOf(people.count - lastIncluded, 0)
            }
        }
    }

    private fun getCustomersInTheElevator() {
        // if any empty space in elevator than let customers get in
        for (people in loginQueue) {
            if (currentSize < 10 && people.count > 0) {
                customersInElevator.add(People(minOf(people.count, 10 - currentSize), people.targetFloor))
                val lastIncluded = minOf(people.count, 10 - currentSize)
                currentSize += lastIncluded
                people.count = maxOf(people.count - lastIncluded, 0)
            }
        }
    }
}