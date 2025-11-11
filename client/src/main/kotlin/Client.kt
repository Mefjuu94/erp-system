package com.erp.client

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import classModels.User
import com.erp.client.compose.LoginView
import com.erp.client.compose.masterUser.MainPanelView


@Serializable
data class ServerStatus(val message: String)

val adressPrefix = "http://localhost:8080/"

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}

fun main() = application {
    var isLoggedIn by remember { mutableStateOf(false) }
    var loggedUser by remember { mutableStateOf<User?>(null) }
    var mode by remember { mutableStateOf("dashboard") } // 🔹 dodane

    if (!isLoggedIn) {
        Window(
            title = "ERP Client - Logowanie",
            onCloseRequest = ::exitApplication,
            state = WindowState(width = 400.dp, height = 600.dp)
        ) {
            var pingStatus by remember { mutableStateOf("Oczekiwanie na odpowiedź...") }

            LaunchedEffect(Unit) {
                try {
                    val response: String = client.get(adressPrefix + "ping").body()
                    pingStatus = response
                } catch (e: Exception) {
                    pingStatus = "Błąd połączenia z serwerem."
                }
            }

            MaterialTheme {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (pingStatus == "pong") "Połączono z serwerem ✅" else "Brak połączenia z serwerem ❌",
                        style = MaterialTheme.typography.h6,
                        color = if (pingStatus == "pong") MaterialTheme.colors.onSurface else MaterialTheme.colors.error
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    LoginView(
                        client,
                        onLoginSuccess = { user ->
                            loggedUser = user
                            isLoggedIn = true
                        }
                    )
                }
            }
        }
    }

    if (isLoggedIn && loggedUser != null) {
        Window(
            title = "ERP Client - Panel główny",
            onCloseRequest = ::exitApplication,
            state = WindowState(width = 1200.dp, height = 1000.dp)
        ) {
            MainPanelView(
                user = loggedUser!!,
                client = client,
                mode = mode,
                onModeChange = { mode = it } // 🔹 dodane
            )
        }
    }
}

