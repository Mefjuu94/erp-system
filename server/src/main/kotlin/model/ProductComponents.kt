package com.erp.server.model

import org.jetbrains.exposed.sql.Table

object ProductComponentTable : Table("product_components") {
    val product = reference("product_id", ProductTable)
    val item = reference("item_id", ItemTable)
    val quantity = integer("quantity")
}
