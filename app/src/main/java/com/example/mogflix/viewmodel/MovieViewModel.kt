package com.example.mogflix.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.mogflix.data.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieViewModel : ViewModel() {
    private val _movies = mutableStateListOf<Movie>()
    val movies: List<Movie> = _movies

    fun addMovie(movie: Movie) {
        _movies.add(movie)
    }

    fun deleteMovie(movie: Movie) {
        _movies.remove(movie)
    }
}