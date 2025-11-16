package com.erp.client.compose.masterUser.mainPanelView


import io.ktor.client.HttpClient
import org.example.classModels.item.ItemType

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import classModels.User
import com.erp.client.adressPrefix
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.classModels.item.Item

@Composable
fun createModeView(client: HttpClient) {
    var userInput by remember { mutableStateOf("") }
    var productInput by remember { mutableStateOf("") }

    var itemSectionExpanded by remember { mutableStateOf(true) }
    var productSectionExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var itemsList by remember { mutableStateOf<List<Item>>(emptyList())}

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

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    addItem(
                                        client = client,
                                        name = userInput,
                                        type = ItemType.COMPONENT.toString()
                                    )
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Stwórz")
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
    type: String,
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

fun sendImportRequest(path: String, useTypeFromColumn: Boolean) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json()
                }
            }

            val response = client.post(adressPrefix + "item/addItemsFromExcel") {
                url {
                    parameters.append("path", path)
                    parameters.append("useTypeFromColumn", useTypeFromColumn.toString())
                }
            }

            println("Odpowiedź serwera: ${response.status}")
        } catch (e: Exception) {
            println("Błąd wysyłania żądania: ${e.message}")
        }
    }
}