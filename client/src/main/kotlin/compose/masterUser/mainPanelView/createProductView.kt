package com.erp.client.compose.masterUser.mainPanelView

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.classModels.item.Item

@Composable
fun createProductView(client: HttpClient){
// 🔹 Box 2: Podgląd lub inne dane (opcjonalnie)
    var userInput by remember { mutableStateOf("") }
    var productInput by remember { mutableStateOf("") }

    var itemSectionExpanded by remember { mutableStateOf(true) }
    var productSectionExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var itemsList by remember { mutableStateOf<List<Item>>(emptyList()) }
    var itemListByType by remember { mutableStateOf<List<Item>>(emptyList()) }
    val serverResponse = remember { mutableStateOf<String?>(null) }
    val isSuccess = remember { mutableStateOf<Boolean?>(null) }

    val selectedComponents = remember {
        mutableStateListOf<Pair<Item, Int>>() // Item + ilość
    }
    val scrollState = rememberScrollState()

    var selectedType by remember { mutableStateOf("Komponent") }
    var selectedCategory by remember { mutableStateOf("śruby, podkładki, nakrętki") }

    var isProductOrComponent: Boolean = false
    var needToCheckType: Boolean = false

    Column(
        modifier = Modifier
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
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Wczytaj Przedmioty")
        }

        // 🔽 Lista przedmiotów pod przyciskiem
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)
        ) {
            itemsList.forEach { item ->
                var expanded by remember { mutableStateOf(false) }

                Column {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .shadow(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF679fd3),
                            contentColor = Color.White
                        )
                    ) {
                        Text(item.name)
                    }

                    if (selectedType == "FINAL_PRODUCT") {

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                expanded = false
                                selectedComponents.add(item to 1) // domyślna ilość 1

                            }) {
                                Text("➕ Dodaj")
                            }
                        }
                    }
                }
            }
        }
    }

    //podzespoły gotowego produktu

    Column(
        modifier = Modifier
            .border(1.dp, Color.LightGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Podzespoły gotowego produktu")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .verticalScroll(scrollState)
                .border(1.dp, Color.LightGray)
                .padding(8.dp)
                .weight(1f)
        ) {
            // Nagłówek
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Komponent", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Ilość", modifier = Modifier.weight(0.3f), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Wiersze komponentów
            selectedComponents.forEachIndexed { index, (item, quantity) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.name, modifier = Modifier.weight(1f))

                    OutlinedTextField(
                        value = quantity.toString(),
                        onValueChange = { newValue ->
                            val parsed = newValue.toIntOrNull()
                            if (parsed != null && parsed > 0) {
                                selectedComponents[index] = item to parsed
                            }
                        },
                        modifier = Modifier.weight(0.3f),
                        singleLine = true
                    )
                }
            }
        }
    }



}