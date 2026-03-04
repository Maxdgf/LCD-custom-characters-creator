package com.example.lcdcustomcharactercreator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

import com.example.lcdcustomcharactercreator.ui.screens.MainAppScreen
import com.example.lcdcustomcharactercreator.ui.theme.LCDCustomCharacterCreatorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LCDCustomCharacterCreatorTheme {
                MainAppScreen()
            }
        }
    }
}