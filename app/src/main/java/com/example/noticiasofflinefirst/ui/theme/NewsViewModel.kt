package com.example.noticiasofflinefirst.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noticiasofflinefirst.data.NewsDatabase
import com.example.noticiasofflinefirst.data.NewsRepository
import com.example.noticiasofflinefirst.model.NewsFilters
import com.example.noticiasofflinefirst.model.Noticia
import com.example.noticiasofflinefirst.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 1️⃣ Estado de la UI
sealed interface EstadoNoticias {
    object Cargando : EstadoNoticias
    data class Exito(
        val noticias: List<Noticia>,
        val filtros: NewsFilters
    ) : EstadoNoticias
    data class Error(val mensaje: String) : EstadoNoticias
}

// 2️⃣ ViewModel
class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = NewsDatabase.getDatabase(application)
    private val repository = NewsRepository(RetrofitClient.apiService, db.noticiaDao())

    // 3️⃣ Filtros activos
    private val _filters = MutableStateFlow(
        NewsFilters(source = "techcrunch") // valor por defecto
    )
    val filters: StateFlow<NewsFilters> = _filters

    // 4️⃣ Estado observable por la UI
    private val _estado = MutableStateFlow<EstadoNoticias>(EstadoNoticias.Cargando)
    val estado: StateFlow<EstadoNoticias> = _estado

    // 5️⃣ Cargar noticias usando filtros
    fun cargarNoticias(apiKey: String) {
        viewModelScope.launch {
            _estado.value = EstadoNoticias.Cargando
            try {
                // PASO 4: filtros seguros
                val filtrosActuales = _filters.value.copy(
                    source = _filters.value.source ?: "techcrunch",
                    country = _filters.value.country ?: "us", // valor por defecto
                    category = _filters.value.category,
                    query = _filters.value.query
                )

                val noticias = repository.obtenerNoticias(
                    apiKey = apiKey,
                    filters = filtrosActuales
                )

                _estado.value = EstadoNoticias.Exito(
                    noticias = noticias,
                    filtros = filtrosActuales
                )

            } catch (e: Exception) {
                _estado.value = EstadoNoticias.Error(
                    "Error al cargar noticias: ${e.message ?: "Desconocido"}"
                )
            }
        }
    }

    // 6️⃣ Cambiar filtros desde ConfigScreen
    fun actualizarFiltros(
        nuevosFiltros: NewsFilters,
        apiKey: String
    ) {
        _filters.value = nuevosFiltros
        cargarNoticias(apiKey)
    }
}
