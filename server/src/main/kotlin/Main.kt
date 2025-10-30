package com.erp.server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    println("Serwer startuje...")
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/ping") {
                call.respondText("pong")
            }
        }
    }.start(wait = true)
    println("Serwer działa.")

}
