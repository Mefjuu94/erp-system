package com.erp.server.utilities

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.example.classModels.item.Item
import java.io.File


fun importItemsFromExcel(
    file: File,
    defaultType: String,
    useTypeFromColumn: Boolean = false
): List<Item> {
    val items = mutableListOf<Item>()

    val workbook = XSSFWorkbook(file.inputStream())
    val sheet = workbook.getSheetAt(0)

    for (row in sheet.drop(1)) { // pomijamy nagłówek
        val nameCell = row.getCell(1)
        val typeCell = row.getCell(2)

        val name = nameCell?.stringCellValue ?: continue
        val type = if (useTypeFromColumn) {
            typeCell?.stringCellValue ?: defaultType
        } else {
            defaultType
        }

        items.add(Item(name = name, type = type)) // bez id
    }

    workbook.close()
    return items
}

