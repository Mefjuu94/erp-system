package org.example.classModels.products

import org.example.classModels.item.Item

data class BOMItem(
    val item: Item,
    val quantity: Int
)

