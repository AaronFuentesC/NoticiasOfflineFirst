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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.noticiasofflinefirst.ui.theme.NewsScreen
import com.example.noticiasofflinefirst.ui.theme.NoticiasOfflineFirstTheme
import com.example.noticiasofflinefirst.ui.theme.ConfigScreen
import androidx.navigation.compose.composable
import com.example.noticiasofflinefirst.ui.NewsViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = BuildConfig.NEWS_API_KEY
        println("API Key: $apiKey")

        setContent {
            NoticiasOfflineFirstTheme {
                val navController = rememberNavController()
                val viewModel: NewsViewModel = viewModel(
                    factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                )


                NavHost(
                    navController = navController,
                    startDestination = "news"
                ) {
                    composable("news") {
                        NewsScreen(
                            viewModel = viewModel,
                            apiKey = apiKey,
                            onOpenConfig = {
                                navController.navigate("config")
                            }
                        )
                    }

                    composable("config") {
                        ConfigScreen(
                            filtrosActuales = viewModel.filters.collectAsState().value,
                            onApply = { filtros ->
                                viewModel.actualizarFiltros(filtros, apiKey)
                                navController.popBackStack()
                            }
                        )

                    }
                }
            }
        }
    }
}

