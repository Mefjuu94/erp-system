package org.example.classModels.products

data class Product(
    val id: Int,
    val name: String,
    val bom: List<BOMItem>
)
