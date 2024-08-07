package com.ucne.geekmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.ucne.geekmarket.presentation.components.MainScaffold
import com.ucne.geekmarket.presentation.navigation.GeekMarketNavHost
import com.ucne.geekmarket.ui.theme.GeekMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeekMarketTheme {
                val navHost = rememberNavController()
                var showDeleteButton by remember { mutableStateOf(false) }
                MainScaffold(
                    navHostController = navHost,
                    showDeleteButton = { showDeleteButton = !showDeleteButton }
                ){ innerPadding->
                    GeekMarketNavHost(navHost, innerPadding, showDeleteButton)
                }
            }
        }
    }
}