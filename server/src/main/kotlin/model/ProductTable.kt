package com.erp.server.model

import classModels.User
import io.ktor.util.valuesOf
import org.example.classModels.item.Item
import org.example.classModels.products.Product
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.collections.get

object ProductTable : IntIdTable("products") {
    val name = varchar("name", 50)
}


fun ResultRow.toProduct(): Product = Product(
    id = this[ProductTable.id].value,
    name = this[ProductTable.name],
    components = emptyMap(), // tymczasowo puste
    comparisonList = emptyMap() // tymczasowo puste
)