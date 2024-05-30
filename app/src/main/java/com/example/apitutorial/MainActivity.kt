package com.example.apitutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.apitutorial.HomeScreenUI.MainScreen
import com.example.apitutorial.ui.theme.ApiTutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        setContent {
            ApiTutorialTheme {
                MainScreen(viewModel = weatherViewModel)
            }
        }
    }
}
