package service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.Floor
import model.People
import utils.addElement
import utils.createThread

data class Elevator(
    val loginQueue: List<People> = listOf(People()),
    val exitQueue: List<People> = listOf(People()),
    val floorQueue: List<Floor> = listOf(Floor()),
    val customersInElevator: MutableList<People> = mutableListOf(),
    val name: Int,
    var currentFloor: Int = 0,
) {
    private val elevatorThread = createThread(name.toString())
    private var isGoingUp = true
    private var currentSize = 0
    val direction get() = if (isGoingUp) "Up" else "Down"
    var isAlive: Boolean = false
        get() = when {
            !field -> customersInElevator.isNotEmpty()
            else -> field
        }
        set(value) {
            field = when {
                value -> value
                customersInElevator.isEmpty() -> value
                else -> isAlive
            }
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
        // if current floor !=0 and elevator isGoingDown and there is people at exit Queue get them inside
        for (people in exitQueue) {
            if (people.currentFloor == currentFloor && currentSize < 10 && people.count > 0 && isAlive) {
                val lastIncluded = minOf(people.count, 10 - currentSize)
                customersInElevator.addElement(People(lastIncluded, people.targetFloor))
                floorQueue[people.currentFloor].exitQueueSize -= lastIncluded
                floorQueue[people.currentFloor].currentCustomerSize -= lastIncluded
                currentSize += lastIncluded
                people.count = maxOf(people.count - lastIncluded, 0)
            }
        }
    }

    private fun getCustomersInTheElevator() {
        // if any empty space in elevator than let customers get in
        for (people in loginQueue) {
            if (currentSize < 10 && people.count > 0 && isAlive) {
                val lastIncluded = minOf(people.count, 10 - currentSize)
                customersInElevator.add(People(lastIncluded, people.targetFloor))
                currentSize += lastIncluded
                people.count = maxOf(people.count - lastIncluded, 0)
                floorQueue[currentFloor].exitQueueSize -= lastIncluded
            }
        }
    }
}