package com.brivaro.mypass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brivaro.mypass.ui.theme.MyPassTheme
import com.brivaro.mypass.ui.screens.AuthScreen
import com.brivaro.mypass.ui.screens.PasswordListScreen
import com.brivaro.mypass.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPassTheme {
                val viewModel: MainViewModel = viewModel()
                val isAuthenticated by viewModel.isAuthenticated.collectAsState()
                val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (!isAuthenticated) {
                            AuthScreen(
                                isFirstLaunch = isFirstLaunch,
                                onAuthSuccess = viewModel::onAuthenticationSuccess
                            )
                        } else {
                            PasswordListScreen()
                        }
                    }
                }
            }
        }
    }
}