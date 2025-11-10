package com.erp.server.dao

import com.erp.server.model.ItemTable
import org.example.classModels.item.Item
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object ItemDAO {
    private val itemStorage = mutableMapOf<Int, Item>()

    fun getItemById(id: Int): Item? = itemStorage[id]
    fun getItemByName(name: String): Item? = itemStorage.values.find { it.name == name }

    fun createItem(item: Item): Item {
        var insertedItem: Item? = null
        println("próbuję zapisać")
        transaction {
            val id = ItemTable.insertAndGetId {
                it[name] = item.name
                it[type] = item.type
            }.value
            insertedItem = item.copy(id = id)
        }
        return insertedItem!!
    }

    fun getAllItems(): List<Item> = transaction {
        ItemTable.selectAll().map {
            Item(
                id = it[ItemTable.id].value,
                name = it[ItemTable.name],
                type = it[ItemTable.type]
            )
        }
    }


    fun deleteItem(id: Int) {
        itemStorage.remove(id)
    }

    fun updateItem(item: Item) {
        if (itemStorage.containsKey(item.id)) {
            itemStorage[item.id as Int] = item
        }
    }
}
