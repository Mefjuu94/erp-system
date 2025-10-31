package com.erp.server

import classModels.User
import com.typesafe.config.ConfigFactory
import dao.UserDao
import routes.userRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*

import model.allTables
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction



fun main() {
    println("Serwer startuje...")
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
    println("Serwer działa.")

}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }

    val config = HoconApplicationConfig(ConfigFactory.load()).config("ktor.database")
    println("🔌Lacze z baza danych...")
    Database.connect(
        url = config.property("url").getString(),
        driver = config.property("driver").getString(),
        user = config.property("user").getString(),
        password = config.property("password").getString()
    )

    println("✅ Połączono z bazą danych!")

    transaction {
        SchemaUtils.create(*allTables.toTypedArray())
    }

    routing {
        userRoutes()

        get("/ping") {
            call.respondText("pong")

            var testUser = UserDao.create(User(1, "testUser", "Testemail@email.com"))
            println(testUser.toString())

        }

        get("/user") {
            var testUser = UserDao.create(User(1, "testUser", "Testemail@email.com"))
            call.respondText("new user: ${testUser.toString()}")

            println(testUser.toString())

        }
    }
}

