package com.example.noticiasofflinefirst.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.noticiasofflinefirst.model.NewsFilters

@Composable
fun ConfigScreen(
    filtrosActuales: NewsFilters,
    onApply: (NewsFilters) -> Unit
) {
    // Estados locales
    var selectedSource by remember { mutableStateOf(filtrosActuales.source ?: "") }
    var selectedCountry by remember { mutableStateOf(filtrosActuales.country ?: "") }
    var selectedCategory by remember { mutableStateOf(filtrosActuales.category ?: "") }

    // Datos de ejemplo
    val fuentes = listOf("techcrunch", "bbc-news", "cnn", "reuters")
    val countries = listOf("us", "gb", "es", "fr", "de")
    val categories = listOf("business", "entertainment", "general", "health", "science", "sports", "technology")

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())  // Scroll si hay muchos elementos
    ) {

        // 🔹 Sección Fuentes
        Text("Selecciona la fuente (desactiva otros filtros si usas fuente)", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        fuentes.forEach { fuente ->
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
            ) {
                RadioButton(
                    selected = selectedSource == fuente,
                    onClick = {
                        selectedSource = fuente
                        selectedCountry = ""
                        selectedCategory = ""
                    }
                )
                Spacer(Modifier.width(8.dp))
                Text(fuente)
            }
        }

        Spacer(Modifier.height(16.dp))

        // 🔹 Sección Country y Category
        Text("O elige Country y Category", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        // Country
        Text("Country", style = MaterialTheme.typography.titleMedium)
        countries.forEach { country ->
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
            ) {
                RadioButton(
                    selected = selectedCountry == country,
                    onClick = {
                        selectedCountry = country
                        selectedSource = "" // desactiva fuente
                    }
                )
                Spacer(Modifier.width(8.dp))
                Text(country)
            }
        }

        Spacer(Modifier.height(8.dp))
        // Category
        Text("Category", style = MaterialTheme.typography.titleMedium)
        categories.forEach { category ->
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
            ) {
                RadioButton(
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = category
                        selectedSource = "" // desactiva fuente
                    }
                )
                Spacer(Modifier.width(8.dp))
                Text(category)
            }
        }

        Spacer(Modifier.height(24.dp))

        // 🔹 Botón visible para aplicar filtros
        Button(
            onClick = {
                onApply(
                    NewsFilters(
                        source = if (selectedSource.isBlank()) null else selectedSource,
                        country = if (selectedCountry.isBlank()) null else selectedCountry,
                        category = if (selectedCategory.isBlank()) null else selectedCategory
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Aplicar filtros")
        }
    }
}
