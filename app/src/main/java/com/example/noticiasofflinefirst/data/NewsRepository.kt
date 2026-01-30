package com.example.noticiasofflinefirst.data

import com.example.noticiasofflinefirst.model.NewsFilters
import com.example.noticiasofflinefirst.model.Noticia
import kotlinx.coroutines.flow.first

class NewsRepository(
    private val apiService: com.example.noticiasofflinefirst.network.NewsApiService,
    private val noticiaDao: NoticiaDao
) {
    suspend fun obtenerNoticias(apiKey: String, filters: NewsFilters): List<Noticia> {
        return try {
            // ❗ Seleccionamos qué filtros usar según si hay source
            val response = if (!filters.source.isNullOrBlank()) {
                // Si hay fuente seleccionada, solo enviamos sources
                apiService.getTopHeadlines(
                    sources = filters.source,
                    country = null,
                    category = null,
                    query = filters.query,
                    apiKey = apiKey
                )
            } else {
                // Si no hay fuente, usamos country, category y query
                apiService.getTopHeadlines(
                    sources = null,
                    country = filters.country,
                    category = filters.category,
                    query = filters.query,
                    apiKey = apiKey
                )
            }

            // Depuración opcional
            println("Llamada exitosa: ${response.status}, total=${response.totalResults}")
            println("Primer artículo: ${response.articles.firstOrNull()?.title}")

            if (response.status != "ok" || response.articles.isEmpty()) {
                throw Exception("Respuesta vacía: status=${response.status}, total=${response.totalResults}")
            }

            // Sanitizar URLs
            val noticiasSanitizadas = response.articles.map { noticia ->
                noticia.copy(
                    url = noticia.url.trim(),
                    urlToImage = noticia.urlToImage?.trim()
                )
            }

            // Guardar en Room
            noticiaDao.borrarTodas()
            noticiaDao.insertar(noticiasSanitizadas)

            noticiasSanitizadas

        } catch (e: Exception) {
            // Fallback a caché si falla
            val cached = noticiaDao.obtenerTodas().first()
            if (cached.isNotEmpty()) {
                println("Usando caché tras error: ${e.message}")
                return cached
            } else {
                throw e
            }
        }
    }
}
