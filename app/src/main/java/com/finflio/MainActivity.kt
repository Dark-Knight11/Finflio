package com.finflio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.finflio.ui.theme.FinflioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinflioTheme {
                // A surface container using the 'background' color from the theme

            }
        }
    }
}