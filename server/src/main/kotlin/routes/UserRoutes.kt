package routes

import classModels.User
import dao.UserDao
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.call

fun Route.userRoutes() {
    route("/users") {
        post("/adduser") {
            println("ADD USER")
            val user = call.receive<User>()
            val created = UserDao.create(user)
            call.respond(HttpStatusCode.Created, created)
        }

        get {
            call.respond(UserDao.getAll())
        }

        get("/login/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Nieprawidłowe ID")
                return@get
            }

            val user = UserDao.getById(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound, "Użytkownik nie istnieje")
            }
        }
    }
}
