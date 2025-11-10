package com.erp.client.Compose.masterUser


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import classModels.User
import com.erp.client.adressPrefix
import com.erp.client.client
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.example.classModels.item.Item
import org.example.classModels.item.ItemType

import java.awt.FileDialog
import java.awt.Frame
import java.io.File



@Composable
fun MainPanelView(user: User) {
    var state by remember { mutableStateOf("dashboard") }
    var usersList by remember { mutableStateOf(listOf<User>()) }
    var allItems by remember { mutableStateOf(listOf<Item>()) }
    var filteredItems by remember { mutableStateOf(listOf<Item>()) }
    var searchQuery by remember { mutableStateOf("") }
    var filePath by remember { mutableStateOf("") }
    var useTypeFromColumn by remember { mutableStateOf(false) }


        Row(modifier = Modifier.fillMaxSize()) {

            // Lewa belka (sidebar)
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFEEEEEE))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Menu", style = MaterialTheme.typography.h6)

                Button(onClick = {
                    state = "dashboard"
                    println("menu")
                }) {
                    Text("Tablica")
                }

                Button(onClick = {
                    state = "users"


                    println("users")
                }) {
                    Text("Użytkownicy")
                }

                Button(onClick = {
                    state = "settings"
                    println("settings")
                }) {
                    Text("Ustawienia")
                }
                Button(onClick = {
                    state = "createMode"
                    println("createMode")
                }) {
                    Text("Stwórz")
                }
            }


            // Główna zawartość
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {

                    when (state) {
                        "dashboard" -> {
                            Text("Witaj w panelu!", style = MaterialTheme.typography.h4)
                            Spacer(Modifier.height(16.dp))
                            Text("Tu będą informacje, formularze, wykresy itd.")
                        }

                        "users" -> {
                            Text("Użytkownicy!", style = MaterialTheme.typography.h4)
                            Spacer(Modifier.height(16.dp))

                            LaunchedEffect(Unit) {
                                try {
                                    val response: List<User> = client.get(adressPrefix + "users").body()
                                    println(response)
                                    usersList = response
                                } catch (e: Exception) {
                                    println("coś poszło nie tak")
                                }
                            }

                            usersList.forEach {
                                Text("${it.username} ${it.surname}", style = MaterialTheme.typography.h6)
                            }
                        }

                        "settings" -> {
                            Text("Ustawienia!", style = MaterialTheme.typography.h4)
                        }

                        "createMode" -> {
                            Text("Stwórz produkt, lub dodaj Komponent!", style = MaterialTheme.typography.h4)

                            var userInput by remember { mutableStateOf("") }

//                        OutlinedTextField(
//                            value = userInput,
//                            onValueChange = { userInput = it },
//                            label = { Text("nazwa") },
//                            singleLine = true,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//
//                        val coroutineScope = rememberCoroutineScope()
//
//                        Button(onClick = {
//                            coroutineScope.launch {
//                                addItem(client = client,userInput, ItemType.COMPONENT.toString())
//                            }
//                            println("createMode: $userInput")
//                        }) {
//                            Text("Stwórz")
//                        }


                            var itemSectionExpanded by remember { mutableStateOf(true) }
                            var productSectionExpanded by remember { mutableStateOf(false) }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize()
                            ) {

                                // 🔷 Nagłówek
                                Text(
                                    text = "Panel zarządzania",
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                // 🔽 Sekcja: Tworzenie ITEM
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Nagłówek sekcji z przełącznikiem
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { itemSectionExpanded = !itemSectionExpanded }
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("TWORZENIE ITEM", style = MaterialTheme.typography.h6)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(if (itemSectionExpanded) "▲" else "▼")
                                    }

                                    // Zawartość sekcji
                                    if (itemSectionExpanded) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 8.dp),
                                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            // 🔹 Box 1: Formularz tworzenia ITEM
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

                                                    val coroutineScope = rememberCoroutineScope()

                                                    Button(
                                                        onClick = {
                                                            coroutineScope.launch {
                                                                addItem(
                                                                    client = client,
                                                                    userInput,
                                                                    ItemType.COMPONENT.toString()
                                                                )
                                                            }
                                                            println("createMode: $userInput")
                                                        },
                                                        modifier = Modifier.align(Alignment.End)
                                                    ) {
                                                        Text("Stwórz")
                                                    }
                                                }
                                            }

                                            // 🔹 Box 2: Lista stworzonych ITEMów
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .border(1.dp, Color.Gray)
                                                    .padding(16.dp)
                                            ) {
                                                Column(
                                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text("Lista ITEMów", style = MaterialTheme.typography.subtitle1)
                                                    // 🔍 Wyszukiwarka
                                                    OutlinedTextField(
                                                        value = searchQuery,
                                                        onValueChange = {
                                                            searchQuery = it
                                                            filteredItems = allItems.filter { item ->
                                                                item.name.contains(searchQuery, ignoreCase = true)
                                                            }
                                                        },
                                                        label = { Text("Szukaj po nazwie") },
                                                        singleLine = true,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )

                                                    val scope = CoroutineScope(Dispatchers.IO)
                                                    // 📜 Przewijana lista
                                                    LazyColumn(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .heightIn(max = 300.dp) // ogranicz wysokość, żeby nie rozjeżdżało layoutu
                                                    ) {
                                                        items(filteredItems) { item ->
                                                            Card(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(vertical = 4.dp),
                                                                elevation = 2.dp
                                                            ) {
                                                                Row(
                                                                    modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .padding(12.dp),
                                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                                ) {
                                                                    scope.launch {
                                                                        coroutineScope {
                                                                            fetchItems(client)
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    var filePath by remember { mutableStateOf("") }
                                                    var useTypeFromColumn by remember { mutableStateOf(false) }

                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(16.dp),
                                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                                    ) {
                                                        Text("Importuj ITEMy z Excela", style = MaterialTheme.typography.h6)

                                                        // 🔹 Pole tekstowe z wybraną ścieżką
                                                        OutlinedTextField(
                                                            value = filePath,
                                                            onValueChange = { filePath = it },
                                                            label = { Text("Ścieżka do pliku .xlsx") },
                                                            modifier = Modifier.fillMaxWidth()
                                                        )

                                                        // 🔹 Przycisk do otwarcia eksploratora plików
                                                        Button(onClick = {
                                                            val fileDialog = FileDialog(null as Frame?, "Wybierz plik Excel", FileDialog.LOAD)
                                                            fileDialog.isVisible = true
                                                            val selectedFile = fileDialog.file
                                                            val selectedDir = fileDialog.directory
                                                            if (selectedFile != null && selectedDir != null) {
                                                                filePath = File(selectedDir, selectedFile).absolutePath
                                                            }
                                                        }) {
                                                            Text("Wybierz plik...")
                                                        }

                                                        // 🔹 RadioButton: czy pobierać typ z kolumny
                                                        Text("Czy pobierać typ z kolumny?")
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            RadioButton(
                                                                selected = useTypeFromColumn,
                                                                onClick = { useTypeFromColumn = true }
                                                            )
                                                            Text("Tak")
                                                            Spacer(modifier = Modifier.width(16.dp))
                                                            RadioButton(
                                                                selected = !useTypeFromColumn,
                                                                onClick = { useTypeFromColumn = false }
                                                            )
                                                            Text("Nie (ustaw COMPONENT)")
                                                        }

                                                        // 🔹 Przycisk do wysłania danych
                                                        Button(
                                                            onClick = {
                                                                if (filePath.isNotBlank()) {
                                                                    sendImportRequest(filePath, useTypeFromColumn)
                                                                } else {
                                                                    println("Nie wybrano pliku.")
                                                                }
                                                            },
                                                            modifier = Modifier.align(Alignment.End)
                                                        ) {
                                                            Text("Wyślij")
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                                Spacer(modifier = Modifier.height(16.dp))

                                // 🔽 Sekcja: Tworzenie PRODUKTU
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { productSectionExpanded = !productSectionExpanded }
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(if (productSectionExpanded) "▲" else "▼")

                                    }

                                    if (productSectionExpanded) {
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            // 🔹 Box 1: Formularz tworzenia PRODUKTU
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(8.dp)
                                                    .border(1.dp, Color.Gray)
                                            ) {
                                                Text("Formularz tworzenia PRODUKTU")
                                                // Tu dodaj swój formularz
                                            }

                                            // 🔹 Box 2: Lista dostępnych komponentów
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(8.dp)
                                                    .border(1.dp, Color.Gray)
                                            ) {
                                                Text("Lista dostępnych ITEMów")
                                                // Tu dodaj listę z checkboxami
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


