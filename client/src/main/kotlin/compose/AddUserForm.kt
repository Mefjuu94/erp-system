package com.erp.client.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import classModels.User
import com.erp.client.adressPrefix
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
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

    val occupationOptions = listOf("Frezer", "Tokarz", "Spawacz", "szlifierz", "Pilarz")
    val roleOptions = listOf("pracownik", "mistrz produkcji")

    var selectedOccupation by remember { mutableStateOf(occupationOptions[0]) }
    var selectedRole by remember { mutableStateOf(roleOptions[0]) }

    var createStatus by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
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

        Text("Funkcja: $selectedRole")

        OutlinedButton(onClick = { expanded2 = true }) {
            Text(selectedRole)
        }

        DropdownMenu(
            expanded = expanded2,
            onDismissRequest = { expanded2 = false }
        ) {
            roleOptions.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedRole = option
                    expanded2 = false
                }) {
                    Text(option)
                }
            }
        }

        if (selectedRole == "pracownik") {

            Text("zawód: $selectedOccupation")
            OutlinedButton(onClick = { expanded1 = true }) {
                Text(selectedOccupation)
            }

            DropdownMenu(
                expanded = expanded1,
                onDismissRequest = { expanded1 = false }
            ) {
                occupationOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedOccupation = option
                        expanded1 = false
                    }) {
                        Text(option)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = addUser(client, username, surname, selectedOccupation, "0", title = selectedRole)
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
            val createdUser: User = client.post(adressPrefix + "users/adduser") {
                contentType(ContentType.Application.Json)
                setBody(User(username = username, surname = surname, role = role, hours = hours, title = title))
            }.body()
            "✅ Dodano: ${createdUser.username}"
        }
    } catch (e: Exception) {
        "❌ Błąd: ${e.message}"
    }
}



