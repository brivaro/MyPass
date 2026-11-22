package com.brivaro.mypass.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brivaro.mypass.viewmodel.MainViewModel

@Composable
fun AuthScreen(
    isFirstLaunch: Boolean,
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isFirstLaunch) "✨ Create Account" else "🔐 Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    error = "🚫 Please fill in all fields 🚫"
                    return@Button
                }
                // Si no estamos en el primer lanzamiento, intentamos autenticar
                if (isFirstLaunch) {
                    viewModel.createUser(username, password)
                } else {
                    // Autenticación: verificamos si las credenciales son correctas
                    viewModel.authenticate(username, password)

                    // Comprobamos si la autenticación fue exitosa
                    if (!viewModel.isAuthenticated.value) {
                        error = "❌ Invalid username or password ❌"
                    } else {
                        // Si la autenticación es exitosa, procedemos a la siguiente pantalla
                        onAuthSuccess()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isFirstLaunch) "🌟 Create" else "🚀 Enter")
        }
    }
}