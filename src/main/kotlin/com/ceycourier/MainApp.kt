// src/main/kotlin/com/ceycourier/MainApp.kt
package com.ceycourier

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ceycourier.components.Dashboard
import com.ceycourier.components.LoginForm
import com.ceycourier.components.MainLayout
import com.ceycourier.services.AuthService

@Composable
fun App() {
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        MainLayout()
    } else {
        LoginForm { username, password ->
            if (AuthService.authenticate(username, password)) {
                isLoggedIn = true
            } else {
                // Handle login error
            }
        }
    }
}
fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "CeyCourier") {
        App()
    }
}