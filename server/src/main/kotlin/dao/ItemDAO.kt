package com.erp.server.dao

import org.example.classModels.item.Item

object ItemDAO {
    private val itemStorage = mutableMapOf<Int, Item>()

    fun getItemById(id: Int): Item? = itemStorage[id]
    fun getItemByName(name: String): Item? = itemStorage.values.find { it.name == name }

    fun createItem(item: Item) {
        itemStorage[item.id] = item
    }

    fun getAllItems(): List<Item> = itemStorage.values.toList()

    fun deleteItem(id: Int) {
        itemStorage.remove(id)
    }

    fun updateItem(item: Item) {
        if (itemStorage.containsKey(item.id)) {
            itemStorage[item.id] = item
        }
    }
}
