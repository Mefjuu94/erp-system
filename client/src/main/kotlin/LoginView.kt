package com.erp.client

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import classModels.User
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import io.ktor.client.*
import io.ktor.http.contentType


@Composable
fun LoginView(onLoginSuccess: (User) -> Unit) {
    var userIdInput by remember { mutableStateOf("") }
    var loginStatus by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔐 Logowanie",
                style = MaterialTheme.typography.h5
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = userIdInput,
                onValueChange = { userIdInput = it },
                label = { Text("ID użytkownika") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val id = userIdInput.toIntOrNull()
                    if (id != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val user = loginById(id)
                            withContext(Dispatchers.Main) {
                                if (user != null) {
                                    onLoginSuccess(user)
                                } else {
                                    loginStatus = "❌ Nie znaleziono użytkownika"
                                }
                            }
                        }
                    } else {
                        loginStatus = "❌ Nieprawidłowe ID"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zaloguj")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = loginStatus,
                color = if (loginStatus.startsWith("✅")) Color(0xFF2E7D32) else MaterialTheme.colors.error,
                style = MaterialTheme.typography.body1
            )
        }
    }
}



suspend fun createUser(client: HttpClient, username: String, email: String): String {
    return try {
        val user = User(username = username, email = email)
        val response: User = client.post("http://localhost:8080/users") {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(user)
        }.body()

        "✅ Dodano: ${response.username}"
    } catch (e: Exception) {
        "❌ Błąd: ${e.message}"
    }
}

suspend fun loginById(id: Int): User? {
    return try {
        client.get("http://localhost:8080/users/login/$id").body()
    } catch (e: Exception) {
        null
    }
}
