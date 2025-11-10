package org.example.classModels.products

import kotlinx.serialization.Serializable
import org.example.classModels.item.Item

@Serializable
data class Product(
    val id: Int? = null,
    val name: String,
    val components: Map<Item, Int>,
    val comparisonList: Map<Item, Int> = emptyMap()
)

