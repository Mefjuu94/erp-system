package com.erp.client.compose.masterUser.mainPanelView

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sun.security.ntlm.Client
import io.ktor.client.HttpClient

@Composable
fun settingsView(client: HttpClient) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Ustawienia systemu", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))

        // Przykładowa zawartość – możesz tu dodać przełączniki, pola tekstowe, itp.
        Text("Tutaj możesz zmieniać konfigurację aplikacji.")
    }
}
