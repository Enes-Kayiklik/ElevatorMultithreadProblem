import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import model.Floor
import model.People
import service.*
import utils.addElement
import utils.createThread

private val loginQueue = mutableListOf<People>()
private val exitQueue = mutableListOf<People>()
private val floorQueue = MutableList(5) { Floor(0, 0, it) }
private val elevatorThreadList = MutableList(5) { num ->
    Elevator(
        loginQueue, exitQueue, floorQueue,
        name = num + 1
    ).also {
        it.isAlive = num == 0; if (num == 0) it.callElevator()
    }
}

suspend fun main() {
    setupMall()
    delay(50000L)
}

private fun setLoginQueue(people: People) {
    loginQueue.addElement(people).also { loginQueue.sortBy { it.targetFloor } }
    floorQueue[people.currentFloor].currentCustomerSize += people.count
    floorQueue[people.currentFloor].exitQueueSize += people.count
}

private fun setExitQueue(people: People) {
    if (exitQueue.any { it.currentFloor == people.currentFloor })
        exitQueue.single { it.currentFloor == people.currentFloor }.count += people.count
    else
        exitQueue.add(people)

    floorQueue[people.currentFloor].exitQueueSize += people.count
}

private suspend fun setupMall() {
    CoroutineScope(createThread("login")).launch {
        Login().getRandomCustomer().collect { setLoginQueue(it) }
    }

    CoroutineScope(createThread("exit")).launch {
        Exit().exitRandomCustomer(floorQueue).collect { setExitQueue(it) }
    }

    CoroutineScope(createThread("control")).launch {
        while (true) {
            Print().printStatement(loginQueue, exitQueue, elevatorThreadList, floorQueue)
            elevatorBuffer()
            delay(500L)
        }
    }
}

private fun elevatorBuffer() {
    val control = Control().checkRequireElevator(floorQueue, elevatorThreadList.count { it.isAlive })
    if (control) {
        elevatorThreadList.first { !it.isAlive }.apply {
            isAlive = true
            callElevator()
        }
    } else if (
        elevatorThreadList.count { it.isAlive } > 1 &&
        elevatorThreadList.any { it.isAlive && it.customersInElevator.isEmpty() }
    ) {
        elevatorThreadList.last { it.isAlive }.isAlive = false
    }
}