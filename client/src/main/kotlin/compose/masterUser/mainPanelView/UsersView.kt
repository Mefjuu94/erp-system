package com.erp.client.compose.masterUser.mainPanelView

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sun.security.ntlm.Client
import io.ktor.client.HttpClient

@Composable
fun usersView(client: HttpClient) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Zarządzanie użytkownikami", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))

        // Przykładowa zawartość – możesz tu dodać listę użytkowników, formularz dodawania itp.
        Text("Tutaj możesz dodawać, edytować i usuwać użytkowników.")
    }
}
