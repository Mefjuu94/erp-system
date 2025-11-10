package com.erp.server.dao

import com.erp.server.model.*
import org.example.classModels.item.Item
import org.example.classModels.products.Product
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object ProductDAO {
    private val productStorage = mutableMapOf<Int, Product>()

    fun getProductById(id: Int): Product? = productStorage[id]
    fun getProductByName(name: String): Product? =
        productStorage.values.find { it.name == name }


    fun createProduct(product: Product) {
        val productId = ProductTable.insertAndGetId {
            it[name] = product.name
        }
    }


    fun updateProduct(product: Product) {
        if (productStorage.containsKey(product.id)) {
            productStorage[product.id as Int] = product
        }
    }

    fun deleteProduct(id: Int) {
        productStorage.remove(id)
    }

    fun getAllProducts(): List<Product> = transaction {
        ProductTable.selectAll().map { productRow ->
            val productId = productRow[ProductTable.id].value
            val name = productRow[ProductTable.name]

            // 🔹 Pobierz komponenty z ProductComponentTable
            val components: Map<Item, Int> = ProductComponentTable
                .select { ProductComponentTable.product eq productId }.associate { componentRow ->
                    val itemRow = ItemTable
                        .select { ItemTable.id eq componentRow[ProductComponentTable.item] }
                        .single()

                    val item = Item(
                        id = itemRow[ItemTable.id].value,
                        name = itemRow[ItemTable.name],
                        type = itemRow[ItemTable.type]
                    )

                    item to componentRow[ProductComponentTable.quantity]
                }

            // 🔹 Pobierz porównania z ProductComparisonTable
            val comparisonList: Map<Item, Int> = ProductComparisonTable
                .select { ProductComparisonTable.product eq productId }.associate { row ->
                    val itemRow = ItemTable
                        .select { ItemTable.id eq row[ProductComparisonTable.item] }
                        .single()

                    val item = Item(
                        id = itemRow[ItemTable.id].value,
                        name = itemRow[ItemTable.name],
                        type = itemRow[ItemTable.type]
                    )

                    item to row[ProductComparisonTable.quantity]
                }


            Product(
                id = productId,
                name = name,
                components = components,
                comparisonList = comparisonList
            )
        }
    }



    fun getFullProduct(id: Int): Product {
        val row = ProductTable.select { ProductTable.id eq id }.single()
        val baseProduct = row.toProduct()

        val components = getComponentsForProduct(id) // np. z ProductComponentTable
        val comparisonList = getComparisonListForProduct(id) // jeśli masz taką tabelę

        return baseProduct.copy(
            components = components,
            comparisonList = comparisonList
        )
    }

    fun getComponentsForProduct(productId: Int): Map<Item, Int> = transaction {
        ProductComponentTable
            .select { ProductComponentTable.product eq productId }
            .associate { row ->
                val itemId = row[ProductComponentTable.item]
                val itemRow = ItemTable.select { ItemTable.id eq itemId }.single()
                val item = Item(
                    itemId.value, itemRow[ItemTable.name],
                    itemRow[ItemTable.type]

                )
                val quantity = row[ProductComponentTable.quantity]
                item to quantity
            }
    }

    fun getComparisonListForProduct(id: Int): Map<Item, Int> = transaction {
        ProductComparisonTable
            .select { ProductComparisonTable.product eq id }.associate { row ->
                val itemRow = ItemTable
                    .select { ItemTable.id eq row[ProductComparisonTable.item] }
                    .single()

                val item = Item(
                    id = itemRow[ItemTable.id].value,
                    name = itemRow[ItemTable.name],
                    type = itemRow[ItemTable.type]
                )

                item to row[ProductComparisonTable.quantity]
            }
    }



}
