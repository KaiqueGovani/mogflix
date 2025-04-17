package com.example.mogflix.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mogflix.data.local.MovieModule
import com.example.mogflix.data.local.entity.MovieEntity
import com.example.mogflix.data.model.Movie
import com.example.mogflix.data.remote.MovieDetailsDto
import com.example.mogflix.data.remote.MovieDto
import com.example.mogflix.data.remote.TmdbApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class MovieViewModel : ViewModel() {
    private val movieDao = MovieModule.database.movieDao()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    init {
        viewModelScope.launch {
            movieDao.getAll().collect { entities ->
                _movies.value = entities.map {
                    Movie(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        watchedDate = it.watchedDate,
                        rating = it.rating
                    )
                }
            }
        }
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            movieDao.insert(
                MovieEntity(
                    title = movie.title,
                    watchedDate = movie.watchedDate,
                    description = movie.description,
                    rating = movie.rating
                )
            )
        }
    }

    fun deleteMovieById(id: Int) {
        viewModelScope.launch {
            MovieModule.database.movieDao().deleteById(id)
        }
    }

    // TMDB Suggestions
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

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }

    private val _movieDetails = mutableStateOf<MovieDetailsDto?>(null)
    val movieDetails: State<MovieDetailsDto?> = _movieDetails

    fun fetchMovieDetails(title: String) {
        viewModelScope.launch {
            try {
                val searchResult = TmdbApiClient.api.searchMovies(title)
                val movie = searchResult.results.firstOrNull()

                if (movie != null) {
                    val details = TmdbApiClient.api.getMovieDetails(movie.id)
                    _movieDetails.value = details
                } else {
                    _movieDetails.value = null
                }
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Erro ao buscar detalhes do filme: ${e.message}")
                _movieDetails.value = null
            }
        }
    }
}