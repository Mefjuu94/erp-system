package com.erp.client

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import classModels.User
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.unit.dp

@Composable
fun MainPanelView(user: User) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("👋 Witaj, ${user.username}!")
        Spacer(Modifier.height(16.dp))

        Text("To jest Twój panel główny.")
        // Tu możesz dodać listę produktów, przyciski, formularze itd.
    }
}
