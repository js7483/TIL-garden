package com.example.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserPaymentApplication

fun main(args: Array<String>) {
    runApplication<UserPaymentApplication>(*args)
}