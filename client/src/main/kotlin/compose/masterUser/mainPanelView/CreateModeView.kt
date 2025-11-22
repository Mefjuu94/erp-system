//package com.erp.client.compose.masterUser.mainPanelView
//
//
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.erp.client.adressPrefix
//import io.ktor.client.*
//import io.ktor.client.call.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.plugins.contentnegotiation.*
//import io.ktor.client.request.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.serialization.kotlinx.json.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import org.example.classModels.item.Item
//import org.example.classModels.item.ItemCategory
//import org.example.classModels.item.ItemType
//import java.awt.FileDialog
//import java.awt.Frame
//import java.io.File
//import java.nio.file.Path
//import java.nio.file.Paths
//
//@Composable
//fun createModeView(client: HttpClient) {
//    var userInput by remember { mutableStateOf("") }
//    var productInput by remember { mutableStateOf("") }
//
//    var itemSectionExpanded by remember { mutableStateOf(true) }
//    var productSectionExpanded by remember { mutableStateOf(false) }
//
//    val coroutineScope = rememberCoroutineScope()
//    var itemsList by remember { mutableStateOf<List<Item>>(emptyList()) }
//    var itemListByType by remember { mutableStateOf<List<Item>>(emptyList()) }
//    val serverResponse = remember { mutableStateOf<String?>(null) }
//    val isSuccess = remember { mutableStateOf<Boolean?>(null) }
//
//    val selectedComponents = remember {
//        mutableStateListOf<Pair<Item, Int>>() // Item + ilość
//    }
//    val scrollState = rememberScrollState()
//
//    var selectedType by remember { mutableStateOf("Komponent") }
//    var selectedCategory by remember { mutableStateOf("śruby, podkładki, nakrętki") }
//
//    var isProductOrComponent: Boolean = false
//    var needToCheckType: Boolean = false
//
//
//
//    Column(
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxSize().padding(8.dp)
//    ) {
//        Text("Stwórz produkt, lub dodaj Komponent!", style = MaterialTheme.typography.h4)
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        // 🔽 Sekcja: Tworzenie ITEM
//        SectionWithToggle(
//            title = "Tworzenie Przedmiotu",
//            expanded = itemSectionExpanded,
//            onToggle = { itemSectionExpanded = !itemSectionExpanded }
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 8.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // 🔹 Box 1: Formularz tworzenia komponentu
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .border(1.dp, Color.Gray)
//                        .padding(16.dp)
//                ) {
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Stwórz Komponent", style = MaterialTheme.typography.subtitle1)
//
//                        OutlinedTextField(
//                            value = userInput,
//                            onValueChange = { userInput = it },
//                            label = { Text("Nazwa") },
//                            singleLine = true,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                        Column(
//                            modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)
//                                .height(300.dp)
//                                .verticalScroll(scrollState)
//                        )
//                        {
//
//                            Row(Modifier.align(Alignment.CenterHorizontally)) {
//                                Text("Kategoria")
//                            }
//
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                RadioButton(
//                                    selected = selectedCategory == "FASTENER",
//                                    onClick = { selectedCategory = "FASTENER"
//                                        needToCheckType = false
//                                        selectedType = "COMPONENT"
//                                    }
//                                )
//                                Text("śruby, podkładki, nakrętki")
//                            }
//
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                RadioButton(
//                                    selected = selectedCategory == "BEARING",
//                                    onClick = { selectedCategory = "BEARING"
//                                        needToCheckType = false
//                                    selectedType = "COMPONENT"
//                                    }
//                                )
//                                Text("Łożyska")
//                            }
//
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                RadioButton(
//                                    selected = selectedCategory == "ELECTRONIC",
//                                    onClick = { selectedCategory = "ELECTRONIC"
//                                        needToCheckType = true}
//                                )
//                                Text("elementy hydrauliczne")
//                            }
//
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                RadioButton(
//                                    selected = selectedCategory == "STRUCTURAL",
//                                    onClick = { selectedCategory = "STRUCTURAL"
//                                        needToCheckType = false
//                                        selectedType = "RAW_MATERIAL"
//                                    }
//                                )
//                                Text("materiał do obróbki")
//                            }
//
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                RadioButton(
//                                    selected = selectedCategory == "SUBASSEMBLY",
//                                    onClick = { selectedCategory = "SUBASSEMBLY"
//                                        needToCheckType = true}
//                                )
//                                Text("podzespoły")
//                            }
//
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                RadioButton(
//                                    selected = selectedCategory == "OTHER",
//                                    onClick = { selectedCategory = "OTHER"
//                                        needToCheckType = true}
//                                )
//                                Text("inne")
//                            }
//                        }
//
//                        if(needToCheckType) {
//
//                            Column(
//                                verticalArrangement = Arrangement.spacedBy(1.dp),
//                                modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)
//                            )
//                            {
//
//                                Row(Modifier.align(Alignment.CenterHorizontally)) {
//                                    Text("Typ")
//                                }
//
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    RadioButton(
//                                        selected = selectedType == "RAW_MATERIAL",
//                                        onClick = {
//                                            selectedType = "RAW_MATERIAL"
//                                            isProductOrComponent = false
//                                        }
//                                    )
//                                    Text("Surowy materiał")
//                                }
//
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    RadioButton(
//                                        selected = selectedType == "COMPONENT",
//                                        onClick = {
//                                            selectedType = "COMPONENT"
//                                            isProductOrComponent = false
//                                        }
//                                    )
//                                    Text("Komponent")
//                                }
//
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    RadioButton(
//                                        selected = selectedType == "TOOL",
//                                        onClick = {
//                                            selectedType = "TOOL"
//                                            isProductOrComponent = false
//                                        }
//                                    )
//                                    Text("Narzędzie")
//                                }
//
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    RadioButton(
//                                        selected = selectedType == "FINAL_PRODUCT",
//                                        onClick = {
//                                            selectedType = "FINAL_PRODUCT"
//                                            isProductOrComponent = true
//                                        }
//                                    )
//                                    Text("Gotowy Produkt")
//                                }
//                            }
//                        }
//
//                        Button(
//                            onClick = {
//                                if(userInput.length > 3) {
//                                    coroutineScope.launch {
//                                        val client = HttpClient(CIO) {
//                                            install(ContentNegotiation) { json() }
//                                        }
//
//                                        val result = addItem(
//                                            client,
//                                            name = userInput,
//                                            type = selectedType,
//                                            category = selectedCategory
//                                        )
//                                        serverResponse.value = result
//                                    }
//                                }else{
//                                    serverResponse.value = "Wprowadź co najmniej 3 znaki!"
//                                }
//                            }, modifier = Modifier.align(Alignment.CenterHorizontally)
//                        ) {
//                            Text("Dodaj pojedynczy element")
//                        }
//
//                        Button(
//                            onClick = {
//                                coroutineScope.launch {
//                                    val (success, message) = addItemsFromTextFile(false,selectedType)
//                                    serverResponse.value = message
//                                    isSuccess.value = success
//                                }
//                            },
//                            modifier = Modifier.align(Alignment.CenterHorizontally)
//                        ) {
//                            Text("Wczytaj z pliku tekstowego")
//                        }
//
//                        serverResponse.value?.let { message ->
//                            val color =
//                                if (isSuccess.value == true) Color(0xFF2E7D32) else Color(0xFFC62828) // zielony / czerwony
//                            Text(
//                                text = message,
//                                color = color,
//                                modifier = Modifier.padding(top = 8.dp)
//                            )
//                        }
//                    }
//                }
//
//                // 🔹 Box 2: Podgląd lub inne dane (opcjonalnie)
//                Column(
//                    modifier = Modifier
//                        .weight(1f)
//                        .border(1.dp, Color.LightGray)
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    Button(
//                        onClick = {
//                            CoroutineScope(Dispatchers.IO).launch {
//                                itemsList = fetchItems(client)
//                                println(itemsList)
//                            }
//                        },
//                        colors = ButtonDefaults.buttonColors(
//                            backgroundColor = Color(0xFF007bab),
//                            contentColor = Color.White
//                        ),
//                        modifier = Modifier
//                            .size(200.dp, 50.dp)
//                            .padding(vertical = 4.dp)
//                            .align(Alignment.CenterHorizontally)
//                    ) {
//                        Text("Wczytaj Przedmioty")
//                    }
//
//                    // 🔽 Lista przedmiotów pod przyciskiem
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)
//                    ) {
//                        itemsList.forEach { item ->
//                            var expanded by remember { mutableStateOf(false) }
//
//                            Column {
//                                Button(
//                                    onClick = { expanded = true },
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .clip(RoundedCornerShape(8.dp))
//                                        .shadow(4.dp),
//                                    colors = ButtonDefaults.buttonColors(
//                                        backgroundColor = Color(0xFF679fd3),
//                                        contentColor = Color.White
//                                    )
//                                ) {
//                                    Text(item.name)
//                                }
//
//                                if (selectedType == "FINAL_PRODUCT") {
//
//                                    DropdownMenu(
//                                        expanded = expanded,
//                                        onDismissRequest = { expanded = false }
//                                    ) {
//                                        DropdownMenuItem(onClick = {
//                                            expanded = false
//                                            selectedComponents.add(item to 1) // domyślna ilość 1
//
//                                        }) {
//                                            Text("➕ Dodaj")
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//                //podzespoły gotowego produktu
//
//                Column(
//                    modifier = Modifier
//                        .weight(1f)
//                        .border(1.dp, Color.LightGray)
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    Text("Podzespoły gotowego produktu")
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .heightIn(max = 200.dp)
//                            .verticalScroll(scrollState)
//                            .border(1.dp, Color.LightGray)
//                            .padding(8.dp)
//                            .weight(1f)
//                    ) {
//                        // Nagłówek
//                        Row(modifier = Modifier.fillMaxWidth()) {
//                            Text("Komponent", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
//                            Text("Ilość", modifier = Modifier.weight(0.3f), fontWeight = FontWeight.Bold)
//                        }
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Wiersze komponentów
//                        selectedComponents.forEachIndexed { index, (item, quantity) ->
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Text(item.name, modifier = Modifier.weight(1f))
//
//                                OutlinedTextField(
//                                    value = quantity.toString(),
//                                    onValueChange = { newValue ->
//                                        val parsed = newValue.toIntOrNull()
//                                        if (parsed != null && parsed > 0) {
//                                            selectedComponents[index] = item to parsed
//                                        }
//                                    },
//                                    modifier = Modifier.weight(0.3f),
//                                    singleLine = true
//                                )
//                            }
//                        }
//                    }
//                }
//
//
//
//            }
//        }
//
//
//        // 🔽 Sekcja: Tworzenie PRODUKTU
//        SectionWithToggle(
//            title = "TWORZENIE PRODUKTU",
//            expanded = productSectionExpanded,
//            onToggle = { productSectionExpanded = !productSectionExpanded }
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 8.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // 🔹 Box 1: Formularz tworzenia komponentu
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .border(1.dp, Color.Gray)
//                        .padding(16.dp)
//                ) {
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        OutlinedTextField(
//                            value = productInput,
//                            onValueChange = { productInput = it },
//                            label = { Text("Nazwa produktu") },
//                            singleLine = true,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                        val scrollState = rememberScrollState()
//
//
//                        Button(
//                            onClick = {
//                                coroutineScope.launch {
//                                    addItem(
//                                        client = client,
//                                        name = productInput,
//                                        type = ItemType.FINAL_PRODUCT.toString(),
//                                        category = ItemCategory.FASTENER.toString() // TODO tutaj zmienić
//                                    )
//                                }
//                            },
//                            modifier = Modifier.align(Alignment.CenterHorizontally)
//                        ) {
//                            Text("Stwórz produkt")
//                        }
//
//                    }
//                }
//
//                // 🔹 Box 2: Podgląd lub inne dane (opcjonalnie)
//                Column(
//                    modifier = Modifier
//                        .weight(1f)
//                        .border(1.dp, Color.LightGray)
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    Button(
//                        onClick = {
//                            CoroutineScope(Dispatchers.IO).launch {
//                                itemsList = fetchItems(client)
//                                println(itemsList)
//                            }
//                        },
//                        colors = ButtonDefaults.buttonColors(
//                            backgroundColor = Color(0xFF007bab),
//                            contentColor = Color.White
//                        ),
//                        modifier = Modifier
//                            .size(200.dp, 50.dp)
//                            .padding(vertical = 4.dp)
//                            .align(Alignment.CenterHorizontally)
//                    ) {
//                        Text("Wczytaj Przedmioty")
//                    }
//                    Button(
//                        onClick = {
//                            CoroutineScope(Dispatchers.IO).launch {
//                                itemListByType = fetchItems(client)
//                                println(itemListByType)
//                            }
//                        },
//                        colors = ButtonDefaults.buttonColors(
//                            backgroundColor = Color(0xFF007bab),
//                            contentColor = Color.White
//                        ),
//                        modifier = Modifier
//                            .size(200.dp, 50.dp)
//                            .padding(vertical = 4.dp)
//                            .align(Alignment.CenterHorizontally)
//                    ) {
//                        Text("Wczytaj Produkty")
//                    }
//
//                    // 🔽 Lista Produktów pod przyciskiem
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)
//                    ) {
//                        itemsList.forEach { item ->
//                            var expanded by remember { mutableStateOf(false) }
//
//                            Column {
//                                Button(
//                                    onClick = { expanded = true },
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .clip(RoundedCornerShape(8.dp))
//                                        .shadow(4.dp),
//                                    colors = ButtonDefaults.buttonColors(
//                                        backgroundColor = Color(0xFF679fd3),
//                                        contentColor = Color.White
//                                    )
//                                ) {
//                                    Text(item.name)
//                                }
//
//                                DropdownMenu(
//                                    expanded = expanded,
//                                    onDismissRequest = { expanded = false }
//                                ) {
//                                    DropdownMenuItem(onClick = {
//                                        expanded = false
//                                        selectedComponents.add(item to 1) // domyślna ilość 1
//
//                                    }) {
//                                        Text("➕ Dodaj")
//                                    }
//
//                                }
//                            }
//                        }
//                    }
//
//                }
//
//            }
//        }
//    }
//}
//
//@Composable
//fun SectionWithToggle(
//    title: String,
//    expanded: Boolean,
//    onToggle: () -> Unit,
//    content: @Composable () -> Unit
//) {
//    Column(
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { onToggle() }
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(title, style = MaterialTheme.typography.h6)
//            Spacer(modifier = Modifier.weight(1f))
//            Text(if (expanded) "▲" else "▼")
//        }
//
//        if (expanded) {
//            content()
//        }
//    }
//}
//
//
//suspend fun addItem(
//    client: HttpClient,
//    name: String,
//    type: String,
//    category: String
//): String {
//    return try {
//        val itemType = ItemType.valueOf(type)
//        val itemCategory = ItemCategory.valueOf(category)
//
//        val responseText = client.post(adressPrefix + "item/addItem") {
//            contentType(ContentType.Application.Json)
//            setBody(Item(name = name, type = itemType, category = itemCategory))
//        }.bodyAsText()
//
//        if (responseText.contains("już istnieje", ignoreCase = true)) {
//            "⚠️ $responseText"
//        } else {
//            "✅ Dodano: $name"
//        }
//    } catch (e: Exception) {
//        "❌ Błąd: ${e.message}"
//    }
//}
//
//
//suspend fun fetchItems(client: HttpClient): List<Item> {
//    return try {
//        client.get(adressPrefix + "item/items").body()
//    } catch (e: Exception) {
//        println("Błąd pobierania itemów: ${e.message}")
//        emptyList()
//    }
//}
//
////TODO zrobić te metody
//suspend fun fetchItemsByCategory(client: HttpClient, category: String): List<Item> {
//    return try {
//        client.get(adressPrefix + "item/items").body()
//    } catch (e: Exception) {
//        println("Błąd pobierania itemów: ${e.message}")
//        emptyList()
//    }
//}
//
//suspend fun fetchItemsByType(client: HttpClient, type: String): List<Item> {
//    return try {
//        client.get(adressPrefix + "item/items").body()
//    } catch (e: Exception) {
//        println("Błąd pobierania itemów: ${e.message}")
//        emptyList()
//    }
//}
//
//
//suspend fun addItemsFromTextFile(useTypeFromColumn: Boolean, itemType: String): Pair<Boolean, String> { //TODO argument typu
//    val path = chooseFilePath() ?: return false to "Nie wybrano pliku"
//
//    val fileContent = File(path.toString()).readText()
//
//    return try {
//        val client = HttpClient(CIO) {
//            install(ContentNegotiation) { json() }
//        }
//
//        val response = client.post(adressPrefix + "item/addItemsFromTextFile") {
//            setBody(fileContent)
//            contentType(ContentType.Text.Plain)
//            url {
//                parameters.append("useTypeFromColumn", useTypeFromColumn.toString())
//            }
//        }
//
//        val bodyText = response.bodyAsText()
//        val success = response.status.value in 200..299
//        success to "Status: ${response.status}, Treść: $bodyText"
//    } catch (e: Exception) {
//        false to "Błąd: ${e.message}"
//    }
//}
//
//
//fun chooseFilePath(): Path? {
//    val dialog = FileDialog(null as Frame?, "Wybierz plik", FileDialog.LOAD)
//    dialog.isVisible = true
//
//    return if (dialog.file != null && dialog.directory != null) {
//        Paths.get(dialog.directory, dialog.file)
//    } else {
//        null // użytkownik anulował wybór
//    }
//}