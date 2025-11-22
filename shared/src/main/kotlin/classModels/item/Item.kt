package org.example.classModels.item

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int? = null,
    val name: String,
    val type: ItemType,
    val category: ItemCategory? = null,
    val children: List<Item> = emptyList()
)
