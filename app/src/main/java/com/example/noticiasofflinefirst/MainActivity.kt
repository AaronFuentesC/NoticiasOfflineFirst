package com.example.noticiasofflinefirst

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noticiasofflinefirst.ui.NewsViewModel
import com.example.noticiasofflinefirst.ui.theme.NewsScreen
import com.example.noticiasofflinefirst.ui.theme.NoticiasOfflineFirstTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// No hacer esto nunca en producción!
// Usa BuildConfig o variables de entorno
        val apiKey = BuildConfig.NEWS_API_KEY
        setContent {
            NoticiasOfflineFirstTheme {
                val viewModel: NewsViewModel = viewModel()
                NewsScreen(viewModel = viewModel, apiKey = apiKey)
            }
        }
    }
}
