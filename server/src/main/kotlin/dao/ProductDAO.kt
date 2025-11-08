package com.erp.server.dao

import com.erp.server.model.*
import org.example.classModels.item.Item
import org.example.classModels.products.Product
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
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
            productStorage[product.id] = product
        }
    }

    fun deleteProduct(id: Int) {
        productStorage.remove(id)
    }

    fun getAllProducts(): List<Product> = productStorage.values.toList()

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

    fun getComparisonListForProduct(productId: Int): Map<Item, Int> = transaction {
        ProductComparisonTable
            .select { ProductComparisonTable.product eq productId }
            .map { it[ProductComparisonTable.value] } as Map<Item, Int>
    }


}
