package service

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import model.People

class Login {
    fun getRandomCustomer() =
            flow {
                while (true) {
                    delay(500L)
                    emit(People((1..10).random(), (1..4).random()))
                }
            }
}