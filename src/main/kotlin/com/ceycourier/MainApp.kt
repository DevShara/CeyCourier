// src/main/kotlin/com/ceycourier/MainApp.kt
package com.ceycourier

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ceycourier.components.Dashboard
import com.ceycourier.components.LoginForm
import com.ceycourier.services.AuthService

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(AppScreens.Login) }
    var loginError by remember { mutableStateOf("") }

    when (currentScreen) {
        AppScreens.Login -> LoginForm { username, password ->
            if (AuthService.authenticate(username, password)) {
                currentScreen = AppScreens.Dashboard
            } else {
                loginError = "Invalid username or password."
            }
        }
        AppScreens.Dashboard -> Dashboard()
    }

    if (currentScreen == AppScreens.Login && loginError.isNotEmpty()) {
        Text(loginError, color = androidx.compose.ui.graphics.Color.Red)
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "CeyCourier") {
        App()
    }
}