package com.example.noticiasofflinefirst.model

data class NewsFilters(
    val source: String? = null,
    val country: String? = null,
    val category: String? = null,
    val query: String? = null
)
