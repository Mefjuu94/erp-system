package com.erp.server.routes

import com.erp.server.dao.ProductDAO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.classModels.products.Product
import kotlin.text.toIntOrNull

fun Route.productRoute() {
    route("/product") {
        post("/addProduct") {
            println("ADD PRODUCT")
            val product = call.receive<Product>()
            val created = ProductDAO.createProduct(product)
            call.respond(HttpStatusCode.Created, created)
        }

        get("/getProductById") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Nieprawidłowe ID")
                return@get
            }

            val product = ProductDAO.getProductById(id = id)
            if (product != null) {
                call.respond(HttpStatusCode.OK, product)
            } else {
                call.respond(HttpStatusCode.NotFound, "Produkt nie istnieje")
            }
        }

        get("/products") {
            val products = ProductDAO.getAllProducts()
            call.respond(HttpStatusCode.OK, products)
        }

        get("/getProductByName") {
            val name = call.parameters["name"]?.toString()
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest, "Nieprawidłowa nazwa")
                return@get
            }

            val product = ProductDAO.getProductByName(name = name)
            if (product != null) {
                call.respond(HttpStatusCode.OK, product)
            } else {
                call.respond(HttpStatusCode.NotFound, "Produkt nie istnieje")
            }
        }

    }
}