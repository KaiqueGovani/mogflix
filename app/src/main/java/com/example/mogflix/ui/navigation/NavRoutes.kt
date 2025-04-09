package com.example.mogflix.ui.navigation

sealed class NavRoutes(val route: String) {
    data object MovieList : NavRoutes("movie_list")
    data object AddMovie : NavRoutes("add_movie")
    data object MovieDetails : NavRoutes("movie_details/{movieId}") {
        fun createRoute(movieId: Int) = "movie_details/$movieId"
    }
}