package com.erp.client.Compose.masterUser

import classModels.User


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.erp.client.client
import io.ktor.client.call.body
import io.ktor.client.request.get


@Composable
fun MainPanelView(user: User) {
    var state by remember { mutableStateOf("dashboard") }
    var usersList by remember { mutableStateOf(listOf<User>()) }

    println("MainPanelView działa!")

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
        }

        // Główna zawartość
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                                val response: List<User> = client.get("http://localhost:8080/users").body()
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
                }
            }
        }
    }
}

