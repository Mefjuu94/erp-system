package routes

import classModels.User
import dao.UserDAO
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
            val created = UserDAO.create(user)
            call.respond(HttpStatusCode.Created, created)
        }

        get {
            call.respond(UserDAO.getAll())
        }

        get("/login/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Nieprawidłowe ID")
                return@get
            }

            val user = UserDAO.getById(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound, "Użytkownik nie istnieje")
            }
        }
    }
}
