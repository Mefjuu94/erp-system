package com.erp.client.compose.masterUser

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button

@Composable
fun SidebarButton(label: String, targetMode: String, currentMode: String, onClick: (String) -> Unit) {
    Button(
        onClick = { onClick(targetMode) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (currentMode == targetMode) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label)
    }
}
