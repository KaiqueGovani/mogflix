package com.example.mogflix.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mogflix.data.model.Movie
import com.example.mogflix.data.remote.MovieDto
import com.example.mogflix.data.remote.TmdbApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MovieViewModel : ViewModel() {
    private val _movies = mutableStateListOf<Movie>()
    val movies: List<Movie> = _movies

    fun addMovie(movie: Movie) {
        _movies.add(movie)
    }

    fun deleteMovie(movie: Movie) {
        _movies.remove(movie)
    }

    private val _suggestions = MutableStateFlow<List<MovieDto>>(emptyList())
    val suggestions: StateFlow<List<MovieDto>> = _suggestions.asStateFlow()

    fun getMovieSuggestions(query: String) {
        viewModelScope.launch {
            try {
                if (query.length >= 3) {
                    Log.i("ModelViewModel", "SearchMovies called with query=$query")
                    val response = TmdbApiClient.api.searchMovies(query)
                    _suggestions.value = response.results
                    Log.i("ModelViewModel", "API call to fetch suggestions succeeded, response=$response")
                } else {
                    _suggestions.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("ModelViewModel", "API call to fetch suggestions failed", e)
                _suggestions.value = emptyList()
                if (e is HttpException) {
                    Log.e("MovieViewModel", "HTTP error: ${e.code()} - ${e.message()}")
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("MovieViewModel", "Error body: $errorBody")
                } else {
                    Log.e("MovieViewModel", "Other error", e)
                }
            }
        }
    }

}