package routes

import classModels.User
import dao.UserDao
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Route.userRoutes() {
    route("/users") {
        get {
            call.respond(UserDao.getAll())
        }

        post {
            val user = call.receive<User>()
            val created = UserDao.create(user)
            call.respond(HttpStatusCode.Created, created)
        }
    }
}
