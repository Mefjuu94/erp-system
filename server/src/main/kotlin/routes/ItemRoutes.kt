package com.erp.server.routes

import com.erp.server.dao.ItemDAO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.classModels.item.Item
import org.example.classModels.item.ItemType

fun Route.itemRoute() {
    route("/item") {

        post("/addItem") {
            println("ADD ITEM")
            val item = call.receive<Item>()

            val existing = ItemDAO.getAllItems().find { it.name.equals(item.name, ignoreCase = true) }

            if (existing != null) {
                println("${item.name} już istnieje")
                call.respondText("${item.name} już istnieje")
            } else {
                val created = ItemDAO.createItem(item)
                call.respond(HttpStatusCode.Created, created)
            }
        }

        post("/addItemsFromTextFile") {
            val content = call.receiveText()
            val useTypeFromColumn = call.parameters["useTypeFromColumn"]?.toBooleanStrictOrNull() ?: false

            val lines = content.lines().filter { it.isNotBlank() }
            val items = lines.map { line ->
                Item(
                    name = line.trim(),
                    type = if (useTypeFromColumn) ItemType.RAW_MATERIAL else ItemType.COMPONENT
                )

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