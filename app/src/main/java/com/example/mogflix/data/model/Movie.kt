package com.example.mogflix.data.model

data class Movie(
    val id: Int,
    val title: String,
    val description: String = "",
    val releaseYear: Int = 0,
    val rating: Float = 0f
)