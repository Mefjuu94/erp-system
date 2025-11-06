package com.erp.server.dao

import org.example.classModels.products.Product

object ProductDAO {
    private val productStorage = mutableMapOf<Int, Product>()

    fun getProductById(id: Int): Product? = productStorage[id]

    fun saveProduct(product: Product) {
        productStorage[product.id] = product
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
}
