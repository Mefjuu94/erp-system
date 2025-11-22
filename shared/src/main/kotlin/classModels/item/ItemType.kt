package org.example.classModels.item

import kotlinx.serialization.Serializable

enum class ItemType {
    RAW_MATERIAL,
    COMPONENT,
    TOOL,
    FINAL_PRODUCT
}

@Serializable
enum class ItemCategory {
    FASTENER,      // śruby, podkładki, nakrętki
    BEARING,       // łożyska
    ELECTRONIC,    // elementy elektroniczne
    STRUCTURAL,    // profile, blachy
    SUBASSEMBLY,   // podzespoły
    OTHER
}