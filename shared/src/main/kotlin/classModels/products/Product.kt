package org.example.classModels.products

import org.example.classModels.item.Item

data class Product(
    val id: Int,
    val name: String,
    val components: Map<Item, Int>,
    val comparisonList: Map<Item, Int> = emptyMap()
)

