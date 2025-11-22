package model

import com.erp.server.model.ItemTable
import org.jetbrains.exposed.sql.Table

val allTables = listOf<Table>(
    UserTable,
    ItemTable,

    // dodaj tu kolejne tabele
)
