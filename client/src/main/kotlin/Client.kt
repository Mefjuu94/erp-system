package com.erp.client

import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import classModels.User


@Serializable
data class ServerStatus(val message: String)

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}

fun main() = application {
    Window(
        title = "ERP Client",
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 400.dp, height = 600.dp)
    ) {
        AppContent()
    }
}

@Composable
fun AppContent() {
    var pingStatus by remember { mutableStateOf("Oczekiwanie na odpowiedź...") }
    var isLoggedIn by remember { mutableStateOf(false) }
    var loggedUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response: String = client.get("http://localhost:8080/ping").body()
            pingStatus = response
        } catch (e: Exception) {
            pingStatus = "Błąd połączenia z serwerem."
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (loggedUser == null)
            if (pingStatus == "pong") {
                Text(
                    text = "Połączono z serwerem ✅",
                    style = MaterialTheme.typography.h6
                )
            } else {
                Text(
                    text = "Brak połączenia z serwerem ❌",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }

    MaterialTheme {

        if (isLoggedIn && loggedUser != null) {
            MainPanelView(loggedUser!!)
        } else {
            LoginView(
                onLoginSuccess = { user ->
                    loggedUser = user
                    isLoggedIn = true
                }
            )
        }
    }
}