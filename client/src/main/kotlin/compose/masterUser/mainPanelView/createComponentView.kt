package com.erp.client.compose.masterUser.mainPanelView


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.erp.client.adressPrefix
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import org.example.classModels.item.Item
import org.example.classModels.item.ItemCategory
import org.example.classModels.item.ItemType
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

@Composable
fun createComponentView(client: HttpClient) {
    var userInput by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val serverResponse = remember { mutableStateOf<String?>(null) }
    val isSuccess = remember { mutableStateOf<Boolean?>(null) }

    val scrollState = rememberScrollState()

    var selectedType by remember { mutableStateOf("Komponent") }
    var selectedCategory by remember { mutableStateOf("śruby, podkładki, nakrętki") }

    var needToCheckType by remember { mutableStateOf(false) }


    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        Text("Stwórz produkt, lub dodaj Komponent!", style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 🔹 Box 1: Formularz tworzenia komponentu
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color.Gray)
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Stwórz Komponent", style = MaterialTheme.typography.subtitle1)

                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        label = { Text("Nazwa") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)
                            .height(300.dp)
                            .verticalScroll(scrollState)
                    )
                    {

                        Row(Modifier.align(Alignment.CenterHorizontally)) {
                            Text("Kategoria")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedCategory == "FASTENER",
                                onClick = {
                                    selectedCategory = "FASTENER"
                                    needToCheckType = false
                                    selectedType = "COMPONENT"
                                }
                            )

                            Button(

                                onClick = {
                                    selectedCategory = "FASTENER"
                                    needToCheckType = false
                                    selectedType = "COMPONENT"
                                },colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF007bab),
                                    contentColor = Color.White)
                            ) {
                                Text("śruby, podkładki, nakrętki")
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedCategory == "BEARING",
                                onClick = {
                                    selectedCategory = "BEARING"
                                    needToCheckType = false
                                    selectedType = "COMPONENT"
                                }
                            )
                            Button(
                                onClick = {
                                    selectedCategory = "BEARING"
                                    needToCheckType = false
                                    selectedType = "COMPONENT"
                                },colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF007bab),
                                    contentColor = Color.White)
                            ) {
                                Text("Łożyska")
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedCategory == "STRUCTURAL",
                                onClick = {
                                    selectedCategory = "STRUCTURAL"
                                    needToCheckType = false
                                    selectedType = "RAW_MATERIAL"
                                }
                            )
                            Button(
                                onClick = {
                                    selectedCategory = "STRUCTURAL"
                                    needToCheckType = false
                                    selectedType = "RAW_MATERIAL"
                                },colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF007bab),
                                    contentColor = Color.White)
                            ) {
                                Text("materiał do obróbki")
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedCategory == "SUBASSEMBLY",
                                onClick = {
                                    selectedCategory = "SUBASSEMBLY"
                                    needToCheckType = true
                                    selectedType = "COMPONENT"
                                }
                            )
                            Button(
                                onClick = {
                                    selectedCategory = "SUBASSEMBLY"
                                    needToCheckType = true
                                    selectedType = "COMPONENT"
                                },colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF007bab),
                                    contentColor = Color.White)
                            ) {
                                Text("podzespoły")
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedCategory == "OTHER",
                                onClick = {
                                    selectedCategory = "OTHER"
                                    needToCheckType = true
                                    selectedType = "COMPONENT"
                                }
                            )
                            Button(
                                onClick = {
                                    selectedCategory = "OTHER"
                                    needToCheckType = true
                                    selectedType = "COMPONENT"
                                },colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF007bab),
                                    contentColor = Color.White)
                            ) {
                                Text("inne")
                            }
                        }

                    }

                    if (needToCheckType) {
                         Column(
                            verticalArrangement = Arrangement.spacedBy(1.dp),
                            modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)
                        ) {

                            Row(Modifier.align(Alignment.CenterHorizontally)) {
                                Text("Typ")
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedType == "RAW_MATERIAL",
                                    onClick = {
                                        selectedType = "RAW_MATERIAL"
                                    }
                                )
                                Text("Surowy materiał")
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedType == "COMPONENT",
                                    onClick = {
                                        selectedType = "COMPONENT"
                                    }
                                )
                                Text("Komponent")
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedType == "TOOL",
                                    onClick = {
                                        selectedType = "TOOL"
                                    }
                                )
                                Text("Narzędzie")
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedType == "FINAL_PRODUCT",
                                    onClick = {
                                        selectedType = "FINAL_PRODUCT"
                                    }
                                )
                                Text("Gotowy Produkt")
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (userInput.length > 3) {
                                coroutineScope.launch {
                                    val client = HttpClient(CIO) {
                                        install(ContentNegotiation) { json() }
                                    }

                                    val result = addItem(
                                        client,
                                        name = userInput,
                                        type = selectedType,
                                        category = selectedCategory
                                    )
                                    serverResponse.value = result
                                }
                            } else {
                                serverResponse.value = "Wprowadź co najmniej 3 znaki!"
                            }
                        }, modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Dodaj pojedynczy element")
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val (success, message) = addItemsFromTextFile(false, selectedType)
                                serverResponse.value = message
                                isSuccess.value = success
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Wczytaj z pliku tekstowego")
                    }

                    serverResponse.value?.let { message ->
                        val color =
                            if (isSuccess.value == true) Color(0xFF2E7D32) else Color(0xFFC62828) // zielony / czerwony
                        Text(
                            text = message,
                            color = color,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}


suspend fun addItem(
    client: HttpClient,
    name: String,
    type: String,
    category: String
): String {
    return try {
        val itemType = ItemType.valueOf(type)
        val itemCategory = ItemCategory.valueOf(category)

        val responseText = client.post(adressPrefix + "item/addItem") {
            contentType(ContentType.Application.Json)
            setBody(Item(name = name, type = itemType, category = itemCategory))
        }.bodyAsText()

        if (responseText.contains("już istnieje", ignoreCase = true)) {
            "⚠️ $responseText"
        } else {
            "✅ Dodano: $name"
        }
    } catch (e: Exception) {
        "❌ Błąd: ${e.message}"
    }
}


suspend fun fetchItems(client: HttpClient): List<Item> {
    return try {
        client.get(adressPrefix + "item/items").body()
    } catch (e: Exception) {
        println("Błąd pobierania itemów: ${e.message}")
        emptyList()
    }
}

//TODO zrobić te metody
suspend fun fetchItemsByCategory(client: HttpClient, category: String): List<Item> {
    return try {
        client.get(adressPrefix + "item/items").body()
    } catch (e: Exception) {
        println("Błąd pobierania itemów: ${e.message}")
        emptyList()
    }
}

suspend fun fetchItemsByType(client: HttpClient, type: String): List<Item> {
    return try {
        client.get(adressPrefix + "item/items").body()
    } catch (e: Exception) {
        println("Błąd pobierania itemów: ${e.message}")
        emptyList()
    }
}


suspend fun addItemsFromTextFile(
    useTypeFromColumn: Boolean,
    itemType: String
): Pair<Boolean, String> { //TODO argument typu
    val path = chooseFilePath() ?: return false to "Nie wybrano pliku"

    val fileContent = File(path.toString()).readText()

    return try {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) { json() }
        }

        val response = client.post(adressPrefix + "item/addItemsFromTextFile") {
            setBody(fileContent)
            contentType(ContentType.Text.Plain)
            url {
                parameters.append("useTypeFromColumn", useTypeFromColumn.toString())
            }
        }

        val bodyText = response.bodyAsText()
        val success = response.status.value in 200..299
        success to "Status: ${response.status}, Treść: $bodyText"
    } catch (e: Exception) {
        false to "Błąd: ${e.message}"
    }
}


fun chooseFilePath(): Path? {
    val dialog = FileDialog(null as Frame?, "Wybierz plik", FileDialog.LOAD)
    dialog.isVisible = true

    return if (dialog.file != null && dialog.directory != null) {
        Paths.get(dialog.directory, dialog.file)
    } else {
        null // użytkownik anulował wybór
    }
}