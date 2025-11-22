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
            .map { line ->
                val parts = line.split(";")
                val name = parts[0].trim()
                val type = if (parts.size > 1) ItemType.valueOf(parts[1].trim()) else ItemType.COMPONENT
                Item(name = name, type = type)
            }
    }



    return items
}

