package com.erp.server.utilities

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.example.classModels.item.Item
import java.io.File


fun importItemsFromExcel(file: File): List<Item> {
    val items = mutableListOf<Item>()

    val workbook = XSSFWorkbook(file.inputStream())
    val sheet = workbook.getSheetAt(0)

    for (row in sheet.drop(1)) { // pomijamy nagłówek
        val idCell = row.getCell(0)
        val nameCell = row.getCell(1)
        val typeCell = row.getCell(2)

        val id = idCell.numericCellValue.toInt()
        val name = nameCell.stringCellValue
        val type = typeCell.stringCellValue

        items.add(Item(id, name, type))
    }

    workbook.close()
    return items
}
