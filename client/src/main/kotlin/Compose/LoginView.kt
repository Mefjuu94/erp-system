package com.erp.client.Compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import classModels.User
import com.erp.client.client
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import io.ktor.client.*
import io.ktor.http.ContentType
import io.ktor.http.contentType


@Composable
fun LoginView(client: HttpClient, onLoginSuccess: (User) -> Unit) {
    var userIdInput by remember { mutableStateOf("") }
    var loginStatus by remember { mutableStateOf("") }
    var showForm by remember { mutableStateOf(false) }
    var createStatus by remember { mutableStateOf("") }

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
                text = if (showForm) "➕ Dodaj pracownika" else "🔐 Logowanie",
                style = MaterialTheme.typography.h5
            )

            Spacer(Modifier.height(16.dp))

            if (showForm) {
                AddUserForm(
                    client = client,
                    onUserCreated = { message ->
                        createStatus = message
                        showForm = false
                    }
                )
            } else {
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

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { showForm = !showForm },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showForm) "⬅️ Wróć do logowania" else "➕ Dodaj pracownika")
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = createStatus,
                color = if (createStatus.startsWith("✅")) Color(0xFF2E7D32) else MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

suspend fun createUser(client: HttpClient, username: String, surname: String, role: String, hours: String,title: String): String {
    val user = User(username = username, surname = surname, role = role, hours = hours, title = title)
    return try {
        val response: User = client.post("http://localhost:8080/users/adduser") {
            contentType(ContentType.Application.Json)
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

suspend fun checkIfUserExists(client: HttpClient, username: String): Pair<Boolean, Int?> {
    return try {
        val users: List<User> = client.get("http://localhost:8080/users").body()
        val user = users.find { it.username == username }
        if (user != null) {
            Pair(true, user.id)
        } else {
            Pair(false, null)
        }
    } catch (e: Exception) {
        Pair(false, null)
    }
}





// Button(
//                onClick = {
//                    CoroutineScope(Dispatchers.IO).launch {
//
//                        val (exists, id) = checkIfUserExists(client, "Misiak")
//
//                        if (!exists) {
//                            val result = createUser(client, "Misiak", "Kowalski", "welder", "0")
//                            withContext(Dispatchers.Main) {
//                                createStatus = result
//                            }
//
//                        } else
//                            createStatus = "użytkownik o tym imieniu już istnieje! Jego ID: $id"
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("➕ Dodaj nowego pracownika")
//            }