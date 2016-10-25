package com.github.grzegorzwitkowski.weirdtodo.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class WeirdTodoServer {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(WeirdTodoServer::class.java, *args)
        }
    }
}
