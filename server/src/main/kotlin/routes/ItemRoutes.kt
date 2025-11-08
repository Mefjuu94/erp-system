package com.erp.server.routes

import com.erp.server.dao.ItemDAO
import com.erp.server.utilities.importItemsFromExcel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.classModels.item.Item
import java.io.File
import kotlin.text.toIntOrNull

fun Route.itemRoute() {
    route("/item") get@{
        post("/addItem") {
            println("ADD ITEM")
            val item = call.receive<Item>()
            val created = ItemDAO.createItem(item)
            call.respond(HttpStatusCode.Created, created)
        }

        post("/addItemsFromExcel") {
            val path = call.parameters["path"] // upewnij się, że to "path", nie "patch"
            if (path == null) {
                call.respond(HttpStatusCode.BadRequest, "Brak parametru 'path'")
                return@post
            }

            val file = File(path)
            if (!file.exists()) {
                call.respond(HttpStatusCode.NotFound, "Plik nie istnieje")
                return@post
            }

            val items = importItemsFromExcel(file)

            println("ADD ITEMS FROM EXCEL")

            for (item in items) {
                val created = ItemDAO.createItem(item)
                call.respond(HttpStatusCode.Created, created)
                println("$item was added to database")
            }
        }

        get("/getItemById") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Nieprawidłowe ID")
                return@get
            }

            val item = ItemDAO.getItemById(id = id)
            if (item != null) {
                call.respond(HttpStatusCode.OK, item)
            } else {
                call.respond(HttpStatusCode.NotFound, "Rzecz nie istnieje")
            }
        }

        get("/items") {
            val items = ItemDAO.getAllItems()
            call.respond(HttpStatusCode.OK, items)
        }

        get("/getItemByName") {
            val name = call.parameters["name"]?.toString()
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest, "Nieprawidłowa nazwa")
                return@get
            }

            val item = ItemDAO.getItemByName(name = name)
            if (item != null) {
                call.respond(HttpStatusCode.OK, item)
            } else {
                call.respond(HttpStatusCode.NotFound, "Produkt nie istnieje")
            }
        }

    }
}