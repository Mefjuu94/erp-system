package model

import com.erp.server.model.ItemTable
import com.erp.server.model.ProductComparisonTable
import com.erp.server.model.ProductComponentTable
import com.erp.server.model.ProductTable
import org.jetbrains.exposed.sql.Table

val allTables = listOf<Table>(
    UserTable,
    ItemTable,
    ProductTable,
    ProductComponentTable,
    ProductComparisonTable
    // dodaj tu kolejne tabele
)
