import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import model.Floor
import model.People
import service.*
import utils.createThread

private val loginQueue = mutableListOf<People>()
private val exitQueue = mutableListOf<People>()
private val floorQueue = MutableList(5) { Floor(0, 0, it) }
private val elevatorThreadList = MutableList(5) {
    Elevator(loginQueue, exitQueue, floorQueue,
            isAlive = it == 0,
            name = it + 1
    )
}

suspend fun main() {
    setupMall()
    delay(50000L)
}

private fun setLoginQueue(people: People) {
    if (loginQueue.any { it.targetFloor == people.targetFloor })
        loginQueue.first { it.targetFloor == people.targetFloor }.count += people.count
    else
        loginQueue.add(people).also { loginQueue.sortBy { it.targetFloor } }

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
            if (Control().checkRequireElevator(loginQueue, emptyList(), elevatorThreadList.count { it.isAlive }))
                elevatorThreadList.first { !it.isAlive }.apply {
                    isAlive = true
                    callElevator()
                }
            else if (!Control().checkRequireElevator(loginQueue, emptyList(), elevatorThreadList.count { it.isAlive }) && elevatorThreadList.count { it.isAlive } != 1) {
                elevatorThreadList.last { it.isAlive }.isAlive = false
            }
        }
    }

    CoroutineScope(createThread("print")).launch {
        while (true) {
            Print().printStatement(loginQueue, exitQueue, elevatorThreadList, floorQueue)
            delay(2000L)
        }
    }
}