package com.erp.client.Compose.masterUser


import androidx.compose.runtime.*
import classModels.User

@Composable
fun MainPanelView(user: User) {
    var currentScreen by remember { mutableStateOf("main") }



}
