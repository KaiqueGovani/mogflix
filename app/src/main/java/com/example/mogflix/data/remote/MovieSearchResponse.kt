package com.example.mogflix.data.remote

data class MovieSearchResponse (
    val results: List<MovieDto>
)

data class MovieDto(
    val id: Int,
    val title: String,
    val release_date: String?
)

data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?
)