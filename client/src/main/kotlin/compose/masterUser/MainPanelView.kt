package com.erp.client.compose.masterUser


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import classModels.User
import com.erp.client.compose.masterUser.mainPanelView.*
import io.ktor.client.*


@Composable
fun MainPanelView(
    user: User,
    client: HttpClient,
    mode: String,
    onModeChange: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {

        // 🔹 Sidebar menu
        Column(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("👤 Witaj, ${user.username}", style = MaterialTheme.typography.h6)

            Divider()

            SidebarButton("📊 Dashboard", "dashboard", mode, onModeChange)
            SidebarButton("🛠️ Tworzenie", "createMode", mode, onModeChange)
            SidebarButton("👥 Użytkownicy", "users", mode, onModeChange)
            SidebarButton("⚙️ Ustawienia", "settings", mode, onModeChange)
        }

        // 🔸 Główna zawartość
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            when (mode) {
                "dashboard" -> dashboardView(client)
                "createMode" -> createModeView(client)
                "users" -> usersView(client)
                "settings" -> settingsView(client)
                else -> Text("Nieznany tryb: $mode")
            }
        }
    }
}



