package com.erp.client.Compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import classModels.User
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddUserForm(client: HttpClient, onUserCreated: (String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var createStatus by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Imię") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Nazwisko") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = role,
            onValueChange = { role = it },
            label = { Text("Rola") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = addUser(client, username, surname, role, "0")
                    withContext(Dispatchers.Main) {
                        onUserCreated(result)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("✅ Zapisz użytkownika")
        }
    }
}

suspend fun addUser(
    client: HttpClient,
    username: String,
    surname: String,
    role: String,
    hours: String
): String {
    return try {
        val (exists, id) = checkIfUserExists(client, username)
        if (exists) {
            "❗ Użytkownik już istnieje! ID: $id"
        } else {
            val createdUser: User = client.post("http://localhost:8080/users/adduser") {
                contentType(ContentType.Application.Json)
                setBody(User(username = username, surname = surname, role = role, hours = hours))
            }.body()
            "✅ Dodano: ${createdUser.username}"
        }
    } catch (e: Exception) {
        "❌ Błąd: ${e.message}"
    }
}



