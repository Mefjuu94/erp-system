package org.example.classModels.item

import org.example.classModels.operation.Operation
import org.gradle.language.nativeplatform.internal.Dimensions

data class Item(
    val id: Int,
    val name: String,
    val type: ItemType,
    val material: String,
    val dimensions: Dimensions,
    val defaultOperations: List<Operation> = emptyList()
)