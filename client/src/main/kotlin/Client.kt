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
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier


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
        state = WindowState(width = 400.dp, height = 200.dp)
    ) {
        AppContent()
    }
}

@Composable
fun AppContent() {
    var status by remember { mutableStateOf("Oczekiwanie na odpowiedź...") }

    LaunchedEffect(Unit) {
        try {
            val response: String = client.get("http://localhost:8080/ping").body()
            status = response
        } catch (e: Exception) {
            println("błąd")
            status = "Błąd połączenia: ${e.message}"
            println(e.message)
            println(status)
        }
    }

    MaterialTheme {
        Column {
            Text("Status serwera:")
            Spacer(Modifier.height(8.dp))
            Text(status)
        }
    }
}
