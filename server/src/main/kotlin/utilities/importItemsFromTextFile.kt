package com.erp.server.utilities

import org.example.classModels.item.Item
import org.example.classModels.item.ItemType
import java.io.File


fun importItemsFromTextFile(
    file: File,
    defaultType: String,
    useTypeFromColumn: Boolean = false
): List<Item> {
    val items = mutableListOf<Item>()



    fun importItemsFromFile(filePath: String): List<Item> {
        return File(filePath).readLines()
            .filter { it.isNotBlank() }
            .map { line -> Item(name = line.trim(), type = ItemType.COMPONENT.toString()) }
    }

    return items
}

