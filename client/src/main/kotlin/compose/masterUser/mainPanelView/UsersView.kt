package com.erp.client.compose.masterUser.mainPanelView

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import classModels.User
import com.erp.client.adressPrefix
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun usersView(client: HttpClient, onClick: (User) -> Unit) {
    val usersState = remember { mutableStateOf<List<User>>(emptyList()) }
    val expandedUserId = remember { mutableStateOf<Int?>(null) }
    val selectedUser = remember { mutableStateOf<User?>(null) }

    Row(modifier = Modifier.fillMaxSize().padding(16.dp),horizontalArrangement = Arrangement.spacedBy(24.dp)) {

        // 🔹 Lewa kolumna – lista pracowników
        Column(
            modifier = Modifier
                .weight(1f)
                .border(width = 2.dp, color = Color.DarkGray, shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            Text("Lista pracowników", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val users = getUsers(client)
                        usersState.value = users
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF007bab), // kolor tła
                    contentColor = Color.White           // kolor tekstu
                ),
                modifier = Modifier
                    .size(200.dp, 50.dp)
                    .padding(vertical = 4.dp)
            ) {
                Text("Wczytaj Pracowników")
            }


            if (usersState.value.isEmpty()) {
                Text("Brak użytkowników lub trwa ładowanie...")
            } else {
                usersState.value.forEach { user ->
                    Box(modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()) {

                        Button(
                            onClick = {
                                expandedUserId.value = if (expandedUserId.value == user.id) null else user.id
                                selectedUser.value = user
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("${user.username} ${user.surname} (${user.role})")
                        }

                        DropdownMenu(
                            expanded = expandedUserId.value == user.id,
                            onDismissRequest = { expandedUserId.value = null }
                        ) {
                            DropdownMenuItem(onClick = {
                                onClick(user)
                                expandedUserId.value = null
                            }) {
                                Text("Szczegóły")
                            }
                            DropdownMenuItem(onClick = {
                                println("Edytuj: ${user.username}")
                                expandedUserId.value = null
                            }) {
                                Text("Edytuj")
                            }
                            DropdownMenuItem(onClick = {
                                println("Przydziel zadanie: ${user.username}")
                                expandedUserId.value = null
                            }) {
                                Text("Przydziel zadanie")
                            }
                            DropdownMenuItem(onClick = {
                                println("Usuń: ${user.username}")
                                expandedUserId.value = null
                            }) {
                                Text("Usuń")
                            }
                        }
                    }
                }
            }
        }

        // 🔸 Prawa kolumna – zlecenia wybranego pracownika
        Column(
            modifier = Modifier
                .weight(1f)
                .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
                .fillMaxHeight()
        ) {
            Text("Zlecenia pracownika", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(8.dp))

            selectedUser.value?.let { user ->
                // Tu możesz podpiąć pobieranie zleceń z serwera
                val dummyTasks = listOf("Zlecenie A", "Zlecenie B", "Zlecenie C")

                Text("Aktualnie wybrany: ${user.username} ${user.surname}")
                Spacer(modifier = Modifier.height(8.dp))

                dummyTasks.forEach { task ->
                    Text("• $task", modifier = Modifier.padding(vertical = 4.dp))
                }
            } ?: Text("Wybierz pracownika, aby zobaczyć jego zlecenia.")
        }
    }
}




suspend fun getUsers(client: HttpClient): List<User> {
    return try {
        client.get(adressPrefix + "users").body()
    } catch (e: Exception) {
        println("Błąd pobierania użytkowników: ${e.message}")
        emptyList()
    }
}