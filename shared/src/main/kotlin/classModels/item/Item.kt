package org.example.classModels.item

import org.example.classModels.operation.Operation

data class Item(
    val id: Int,
    val name: String,
    val type: ItemType,
)