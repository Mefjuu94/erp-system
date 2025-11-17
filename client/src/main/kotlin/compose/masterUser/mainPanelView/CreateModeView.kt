package com.erp.client.compose.masterUser.mainPanelView


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.classModels.item.Item
import org.example.classModels.item.ItemType
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

@Composable
fun createModeView(client: HttpClient) {
    var userInput by remember { mutableStateOf("") }
    var productInput by remember { mutableStateOf("") }

    var itemSectionExpanded by remember { mutableStateOf(true) }
    var productSectionExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var itemsList by remember { mutableStateOf<List<Item>>(emptyList()) }
    val serverResponse = remember { mutableStateOf<String?>(null) }
    val isSuccess = remember { mutableStateOf<Boolean?>(null) }



    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Stwórz produkt, lub dodaj Komponent!", style = MaterialTheme.typography.h4)

        Text(
            text = "Panel zarządzania",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔽 Sekcja: Tworzenie ITEM
        SectionWithToggle(
            title = "TWORZENIE ITEM",
            expanded = itemSectionExpanded,
            onToggle = { itemSectionExpanded = !itemSectionExpanded }
        ) {
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

                        Button(onClick = {
                            coroutineScope.launch {
                                val client = HttpClient(CIO) {
                                    install(ContentNegotiation) { json() }
                                }

                                val result = addItem(client, name = userInput, type = ItemType.COMPONENT.toString())
                                serverResponse.value = result
                            }
                        }) {
                            Text("Dodaj pojedynczy element")
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val (success, message) = addItemsFromTextFile(false)
                                    serverResponse.value = message
                                    isSuccess.value = success
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
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

                // 🔹 Box 2: Podgląd lub inne dane (opcjonalnie)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color.LightGray)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                itemsList = fetchItems(client)
                                println(itemsList)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF007bab),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .size(200.dp, 50.dp)
                            .padding(vertical = 4.dp)
                    ) {
                        Text("Wczytaj Przedmioty")
                    }

                    // 🔽 Lista przedmiotów pod przyciskiem
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (item in itemsList) {
                            Text(item.name)
                        }
                    }
                }

            }
        }

        // 🔽 Sekcja: Tworzenie PRODUKTU
        SectionWithToggle(
            title = "TWORZENIE PRODUKTU",
            expanded = productSectionExpanded,
            onToggle = { productSectionExpanded = !productSectionExpanded }
        ) {
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
                        OutlinedTextField(
                            value = productInput,
                            onValueChange = { productInput = it },
                            label = { Text("Nazwa produktu") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    addItem(
                                        client = client,
                                        name = productInput,
                                        type = ItemType.FINAL_PRODUCT.toString()
                                    )
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Stwórz produkt")
                        }
                    }
                }

                // 🔹 Box 2: Podgląd lub inne dane (opcjonalnie)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color.LightGray)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                itemsList = fetchItems(client)
                                println(itemsList) // TODO: zrobić wyświetlaną listę!
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF007bab),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .size(200.dp, 50.dp)
                            .padding(vertical = 4.dp)
                    ) {
                        Text("Wczytaj Przedmioty")
                    }

                    // 🔽 Lista przedmiotów pod przyciskiem
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsList.forEach { item ->
                            var expanded by remember { mutableStateOf(false) }

                            Column {
                                Button(
                                    onClick = { expanded = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(item.name)
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        // TODO: akcja 1 dla item
                                    }) {
                                        Text("🔍 Podgląd")
                                    }

                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        // TODO: akcja 2 dla item
                                    }) {
                                        Text("🗑️ Usuń")
                                    }

                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        // TODO: akcja 3 dla item
                                    }) {
                                        Text("✏️ Edytuj")
                                    }
                                }
                            }
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun SectionWithToggle(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.weight(1f))
            Text(if (expanded) "▲" else "▼")
        }

        if (expanded) {
            content()
        }
    }
}


suspend fun addItem(
    client: HttpClient,
    name: String,
    type: String
): String {
    return try {
        val response: Item = client.post(adressPrefix + "item/addItem") {
            contentType(ContentType.Application.Json)
            setBody(Item(name = name, type = type))
        }.body()

        "✅ Dodano: ${response.name}"
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

suspend fun addItemsFromTextFile(useTypeFromColumn: Boolean): Pair<Boolean, String> {
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
