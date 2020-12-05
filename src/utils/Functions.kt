package utils

import kotlinx.coroutines.newSingleThreadContext
import model.People
import service.Elevator
import java.lang.StringBuilder

fun createThread(name: String) = newSingleThreadContext(name)

@JvmName("printContentPeople")
fun List<People>.printContent(): StringBuilder? {
    val result = StringBuilder("")
    val size = this.size
    result.append('[')
    for ((index, t) in this.withIndex()) {
        result.append("[${t.component1()}, ${t.component2()}]")
        if (index != size - 1) result.append(", ")
    }
    result.append(']')
    return result
}

fun People.printContent(): StringBuilder? {
    return if (this.count == 0)
        null
    else
        StringBuilder("[${this.count}, ${this.targetFloor}]")
}

fun MutableList<People>.addElement(element: People) {
    if (this.any { it.targetFloor == element.targetFloor })
        this.first { it.targetFloor == element.targetFloor }.count += element.count
    else
        this.add(element)
}
/*@JvmName("printContentElevator")
fun List<Elevator>.printContent(): StringBuilder {
    val result = StringBuilder("")
    val size = this.size
    result.append('[')
    for ((index, t) in this.withIndex()) {
        result.append("[${t.customersInElevator.printContent()}, size -> ${t.currentSize}, currentFloor -> ${t.currentFloor}]")
        if (index != size - 1) result.append(", ")
    }
    result.append(']')
    return result
}*/