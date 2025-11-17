package com.erp.server.routes

import com.erp.server.dao.ItemDAO
import com.erp.server.utilities.importItemsFromTextFile
import com.sun.tools.javac.jvm.Items
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.MultiPartData
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.classModels.item.Item
import org.jetbrains.exposed.sql.Query
import java.io.File
import kotlin.text.toIntOrNull

fun Route.itemRoute() {
    route("/item") {

        post("/addItem") {
            println("ADD ITEM")
            val item = call.receive<Item>()

            val itemsInDatabase: List<Item> = ItemDAO.getAllItems()
            if (itemsInDatabase.contains(item)){    //TODO NIE DZIAŁA
                call.respondText("$item.name już istnieje")
            }else {
                val created = ItemDAO.createItem(item)
                call.respond(HttpStatusCode.Created, created)
            }

        }

        post("/addItemsFromTextFile") {
            val content = call.receiveText()
            val useTypeFromColumn = call.parameters["useTypeFromColumn"]?.toBooleanStrictOrNull() ?: false

            val lines = content.lines().filter { it.isNotBlank() }
            val items = lines.map { line ->
                Item(name = line.trim(), type = if (useTypeFromColumn) "UNKNOWN" else "COMPONENT")
            }

            val createdItems = mutableListOf<Item>()
            val itemsInDatabase: List<Item> = ItemDAO.getAllItems()

            var duplicateCount = 0

            items.forEach { item ->
                val exists = itemsInDatabase.any { it.name == item.name }
                if (exists) {
                    duplicateCount++
                } else {
                    val created = ItemDAO.createItem(item)
                    createdItems.add(created)
                }
            }
            println("ilość duplikatów: $duplicateCount")

            val responseMessage = buildString {
                append("Utworzono ${createdItems.size} nowych elementów.")
                if (duplicateCount > 0) {
                    append(" Pominięto $duplicateCount duplikatów.")
                }
            }

            call.respond(HttpStatusCode.Created, responseMessage)
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
            println(items)
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