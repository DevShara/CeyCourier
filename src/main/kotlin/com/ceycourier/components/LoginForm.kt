package com.ceycourier.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginForm(onLogin: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.h5)
        Spacer(Modifier.height(16.dp))
        BasicTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color.Gray),
            decorationBox = { innerTextField ->
                if (username.isEmpty()) Text("Username", fontSize = 16.sp, color = Color.Gray)
                innerTextField()
            }
        )
        Spacer(Modifier.height(16.dp))
        BasicTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color.Gray),
            decorationBox = { innerTextField ->
                if (password.isEmpty()) Text("Password", fontSize = 16.sp, color = Color.Gray)
                innerTextField()
            }
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { onLogin(username, password) },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Login", fontSize = 18.sp)
        }
    }
}