package com.erp.client.Compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import classModels.User
import io.ktor.client.*
import io.ktor.client.call.body
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

    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }

    val optionsForRole = listOf("Frezer", "Tokarz", "Spawacz", "szlifierz", "Pilarz")
    val optionsForTitle = listOf("pracownik", "mistrz produkcji")

    var selectedOptionForRole by remember { mutableStateOf(optionsForRole[0]) }
    var selectedOptionForTitle by remember { mutableStateOf(optionsForTitle[0]) }

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


        Text("Rola: $selectedOptionForRole")
        OutlinedButton(onClick = { expanded1 = true }) {
            Text(selectedOptionForRole)
        }

        DropdownMenu(
            expanded = expanded1,
            onDismissRequest = { expanded1 = false }
        ) {
            optionsForRole.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionForRole = option
                    expanded1 = false
                }) {
                    Text(option)
                }
            }
        }

        Text("Funkcja: $selectedOptionForTitle")

        OutlinedButton(onClick = { expanded2 = true }) {
            Text(selectedOptionForTitle)
        }

        DropdownMenu(
            expanded = expanded2,
            onDismissRequest = { expanded2 = false }
        ) {
            optionsForTitle.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionForTitle = option
                    expanded2 = false
                }) {
                    Text(option)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = addUser(client, username, surname, selectedOptionForRole, "0", title = selectedOptionForTitle)
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
    hours: String,
    title: String
): String {
    return try {
        val (exists, id) = checkIfUserExists(client, username)
        if (exists) {
            "❗ Użytkownik już istnieje! ID: $id"
        } else {
            val createdUser: User = client.post("http://localhost:8080/users/adduser") {
                contentType(ContentType.Application.Json)
                setBody(User(username = username, surname = surname, role = role, hours = hours, title = title))
            }.body()
            "✅ Dodano: ${createdUser.username}"
        }
    } catch (e: Exception) {
        "❌ Błąd: ${e.message}"
    }
}



