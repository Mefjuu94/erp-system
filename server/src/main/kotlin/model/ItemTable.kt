package com.erp.server.model

import org.example.classModels.item.Item
import org.example.classModels.item.ItemCategory
import org.example.classModels.item.ItemType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table


object ItemTable : IntIdTable("items") {
    val name = varchar("name", 50)
    val type = varchar("type", 50)
    val category = varchar("category", 50).nullable()
}

object ItemComponentsTable : Table("item_components") {
    val parent = reference("parent_id", ItemTable)
    val child = reference("child_id", ItemTable)
    val quantity = integer("quantity").default(1)

    override val primaryKey = PrimaryKey(parent, child)
}


fun ResultRow.toItem(): Item = Item(
    id = this[ItemTable.id].value,
    name = this[ItemTable.name],
    type = ItemType.valueOf(this[ItemTable.type]), // konwersja String -> ItemType
    category = this[ItemTable.category]?.let { ItemCategory.valueOf(it) }
)


