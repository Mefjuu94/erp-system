package com.erp.client.compose.masterUser.mainPanelView

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.erp.client.adressPrefix
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.classModels.item.Item

@Composable
fun createProductView(client: HttpClient) {
    val coroutineScope = rememberCoroutineScope()

    // 🔹 Stan
    val finalProducts = remember { mutableStateListOf<Item>() }
    val availableItems = remember { mutableStateListOf<Item>() }
    val selectedComponents = remember { mutableStateListOf<Pair<Item, Int>>() }

    val scrollStateLeft = rememberScrollState()
    val scrollStateRight = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // 🔹 Przycisk wczytania produktów typu FINAL_PRODUCT
        Button(
            onClick = {
                coroutineScope.launch {
                    finalProducts.clear()
                    finalProducts.addAll(getItemsByType(client,"FINAL_PRODUCT")) // DAO pobiera tylko FINAL_PRODUCT
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF007bab),
                contentColor = Color.White
            )
        ) {
            Text("Wczytaj Produkty")
        }

        Spacer(Modifier.height(16.dp))

        // 🔹 Dwukolumnowy układ
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            // 🔸 Kolumna 1: Dodane komponenty
            Column(
                modifier = Modifier.weight(1f)
                    .border(1.dp, Color.Gray)
                    .verticalScroll(scrollStateLeft)
                    .padding(8.dp)
            ) {
                Text("Dodane komponenty", fontWeight = FontWeight.Bold)

                selectedComponents.forEachIndexed { index, (item, qty) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.name, modifier = Modifier.weight(1f))
                        OutlinedTextField(
                            value = qty.toString(),
                            onValueChange = { newValue ->
                                val parsed = newValue.toIntOrNull()
                                if (parsed != null && parsed > 0) {
                                    selectedComponents[index] = item to parsed
                                }
                            },
                            modifier = Modifier.width(60.dp),
                            singleLine = true
                        )
                        IconButton(onClick = { selectedComponents.removeAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Usuń")
                        }
                    }
                }
            }

            // 🔸 Kolumna 2: Lista dostępnych Itemów do dodania
            Column(
                modifier = Modifier.weight(1f)
                    .border(1.dp, Color.Gray)
                    .verticalScroll(scrollStateRight)
                    .padding(8.dp)
            ) {
                Text("Dostępne Itemy", fontWeight = FontWeight.Bold)

                availableItems.forEach { item ->
                    Button(
                        onClick = {
                            if (selectedComponents.none { it.first.id == item.id }) {
                                selectedComponents.add(item to 1)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF679fd3),
                            contentColor = Color.White
                        )
                    ) {
                        Text(item.name)
                    }
                }
            }
        }
    }
}


suspend fun getItemsByType(client: HttpClient, type: String): List<Item> {
    return try {
        client.get(adressPrefix + "item/items").body()
    } catch (e: Exception) {
        println("Błąd pobierania itemów: ${e.message}")
        emptyList()
    }
}