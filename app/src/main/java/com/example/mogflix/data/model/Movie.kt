package com.example.mogflix.data.model

import java.util.Date

data class Movie(
    val id: Int,
    val title: String,
    val description: String = "",
    val watchedDate: Date = Date(),
    val rating: Float = 0f
)