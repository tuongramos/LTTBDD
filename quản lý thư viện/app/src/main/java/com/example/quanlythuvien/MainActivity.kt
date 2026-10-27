package com.example.quanlythuvien

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quanlythuvien.ui.LibraryApp
import com.example.quanlythuvien.ui.LibraryTheme
import com.example.quanlythuvien.ui.LibraryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibraryTheme {
                val model: LibraryViewModel = viewModel()
                LibraryApp(model)
            }
        }
    }
}
