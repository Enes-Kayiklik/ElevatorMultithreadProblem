package service

import model.Floor
import model.People
import utils.printContent

class Print {
    fun printStatement(
        loginQueue: List<People>,
        exitQueue: List<People>,
        elevatorList: List<Elevator>,
        floorQueue: List<Floor>
    ) {
        printFloorQueue(floorQueue)
        printElevator(elevatorList)
        printWaitingQueue(loginQueue, exitQueue)
        println("-------------------------------------------------------------------------------------------------")
    }

    private fun printFloorQueue(floorQueue: List<Floor>) {
        println("0. floor : -- queue : ${floorQueue[0].exitQueueSize}")
        (1..4).forEach { floor ->
            val it = floorQueue[floor]
            println("${it.floorNumber}. floor : ${it.currentCustomerSize} queue : ${it.exitQueueSize}")
        }
        println("\n\n")
    }

    private fun printElevator(elevatorList: List<Elevator>) {
        elevatorList.forEach {
            println(
                """ active : ${it.isAlive}
                        floor : ${it.currentFloor}
                        destination : $...
                        direction : ${it.direction}
                        capacity : 10
                        countInside : ${it.customersInElevator.sumBy { p -> p.count }}
                        inside : ${it.customersInElevator.printContent()}
                
                
            """.trimIndent()
            )
        }
    }

    private fun printWaitingQueue(loginQueue: List<People>, exitQueue: List<People>) {
        println("0. floor : ${loginQueue.printContent()}")
        (1..4).forEach { floor ->
            println(
                "$floor. floor : ${
                    exitQueue.singleOrNull { it.currentFloor == floor }?.printContent() ?: emptyList<Int>()
                }"
            )
        }
    }
}