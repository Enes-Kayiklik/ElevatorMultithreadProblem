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
        println("\u000c")
        printFloorQueue(floorQueue, exitQueue)
        printElevator(elevatorList)
        printWaitingQueue(loginQueue, exitQueue)
    }

    private fun printWaitingQueue(loginQueue: List<People>, exitQueue: List<People>) {
        println("0. floor : ${loginQueue.printContent()}")
        println("1. floor : ${exitQueue.singleOrNull { it.currentFloor == 1 }?.printContent() ?: emptyList<Int>()}")
        println("2. floor : ${exitQueue.singleOrNull { it.currentFloor == 2 }?.printContent() ?: emptyList<Int>()}")
        println("3. floor : ${exitQueue.singleOrNull { it.currentFloor == 3 }?.printContent() ?: emptyList<Int>()}")
        println("4. floor : ${exitQueue.singleOrNull { it.currentFloor == 4 }?.printContent() ?: emptyList<Int>()}")

    }

    private fun printElevator(elevatorList: List<Elevator>) {
        elevatorList.forEach {
            println(""" active : ${it.isAlive}
                        floor : ${it.currentFloor}
                        destination : $...
                        direction : $..
                        capacity : 10
                        countInside : ${it.customersInElevator.sumBy { p -> p.count }}
                        inside : ${it.customersInElevator.printContent()}
                
                
            """.trimIndent())
        }
    }

    private fun printFloorQueue(floorQueue: List<Floor>, exitQueue: List<People>) {
        floorQueue.forEach {
            println("${it.floorNumber}. floor : ${it.currentCustomerSize} queue : ${exitQueue.singleOrNull { single -> single.targetFloor == 0 }?.count ?: 0}")
        }
        println("\n\n")
    }
}