package com.erp.server.model

import org.example.classModels.item.Item
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object ItemTable : IntIdTable("items") {
    val name = varchar("name", 50)
    val type = varchar("type", 50)
}


fun ResultRow.toItem(): Item = Item(
    id = this[ItemTable.id].value,
    name = this[ItemTable.name],
    type = this[ItemTable.type]
)
