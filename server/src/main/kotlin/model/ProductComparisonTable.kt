package com.erp.server.model

import org.jetbrains.exposed.sql.Table

object ProductComparisonTable : Table("product_comparisons") {
    val product = reference("product_id", ProductTable)
    val value = varchar("value", 100) // lub integer, enum, itp.
}

