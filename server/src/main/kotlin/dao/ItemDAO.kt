package com.erp.server.dao

import com.erp.server.model.ItemComponentsTable
import com.erp.server.model.ItemTable
import com.erp.server.model.toItem
import org.example.classModels.item.Item
import org.example.classModels.item.ItemCategory
import org.example.classModels.item.ItemType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object ItemDAO {

    fun getItemById(id: Int): Item? = transaction {
        ItemTable.select { ItemTable.id eq id }.map {
            Item(
                id = it[ItemTable.id].value,
                name = it[ItemTable.name],
                type = ItemType.valueOf(it[ItemTable.type]),
                category = it[ItemTable.category]?.let { c -> ItemCategory.valueOf(c) }
            )
        }.singleOrNull()
    }

    fun getItemByName(name: String): Item? = transaction {
        ItemTable.select { ItemTable.name eq name }.map {
            Item(
                id = it[ItemTable.id].value,
                name = it[ItemTable.name],
                type = ItemType.valueOf(it[ItemTable.type]),
                category = it[ItemTable.category]?.let { c -> ItemCategory.valueOf(c) }
            )
        }.singleOrNull()
    }

    fun getItemsByType(type: ItemType): List<Item> = transaction {
        ItemTable
            .select { ItemTable.type eq type.name }
            .map { it.toItem() }
    }

    fun getItemsByCategory(category: ItemCategory): List<Item> = transaction {
        ItemTable
            .select { ItemTable.category eq category.name }
            .map { it.toItem() }
    }

    // Możesz też zrobić wersję łączoną:
    fun getItemsByTypeAndCategory(type: ItemType, category: ItemCategory): List<Item> = transaction {
        ItemTable
            .select { (ItemTable.type eq type.name) and (ItemTable.category eq category.name) }
            .map { it.toItem() }
    }

    fun createItem(item: Item): Item = transaction {
        val id = ItemTable.insertAndGetId {
            it[name] = item.name
            it[type] = item.type.name
            it[category] = item.category?.name
        }.value
        item.copy(id = id)
    }

    fun getAllItems(): List<Item> = transaction {
        ItemTable.selectAll().map {
            Item(
                id = it[ItemTable.id].value,
                name = it[ItemTable.name],
                type = ItemType.valueOf(it[ItemTable.type]),
                category = it[ItemTable.category]?.let { c -> ItemCategory.valueOf(c) }
            )
        }
    }

    fun deleteItem(id: Int) = transaction {
        ItemTable.deleteWhere { ItemTable.id eq id }
    }

    fun updateItem(item: Item) = transaction {
        ItemTable.update({ ItemTable.id eq item.id!! }) {
            it[name] = item.name
            it[type] = item.type.name
            it[category] = item.category?.name
        }
    }

    fun getComponentsForItem(parentId: Int): List<Item> = transaction {
        (ItemComponentsTable innerJoin ItemTable)
            .select { ItemComponentsTable.parent eq parentId }
            .map { it.toItem() }
    }

    fun addComponentToItem(parentId: Int, childId: Int, quantity: Int = 1) = transaction {
        ItemComponentsTable.insert {
            it[parent] = parentId
            it[child] = childId
            it[ItemComponentsTable.quantity] = quantity
        }
    }

}