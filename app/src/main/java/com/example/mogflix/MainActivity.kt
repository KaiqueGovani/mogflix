package com.example.mogflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mogflix.data.model.Movie
import com.example.mogflix.ui.navigation.NavRoutes
import com.example.mogflix.ui.screens.AddMovieScreen
import com.example.mogflix.ui.screens.MovieListScreen
import com.example.mogflix.ui.theme.MogflixTheme
import com.example.mogflix.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MogflixTheme {
                val navController = rememberNavController()
                Box(
                    modifier = Modifier.padding(bottom = 50.dp, top=20.dp)
                ){
                    AppNavigation(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val fakeViewModel = MovieViewModel().apply {
        addMovie(Movie(1, "Filme X", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ornare gravida lacus.", 2020, 9.05f))
        addMovie(Movie(2, "Filme Y", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ornare gravida lacus.", 2020, 9.05f))
    }

    NavHost(navController = navController, startDestination = NavRoutes.MovieList.route) {
        composable(NavRoutes.MovieList.route) {

            MovieListScreen(
                viewModel = fakeViewModel,
                onAddMovieClick = {
                    navController.navigate(NavRoutes.AddMovie.route)
                }
            )
        }
        composable(NavRoutes.AddMovie.route) {
            AddMovieScreen (
                viewModel = fakeViewModel,
                onMovieAdded = {
                    navController.popBackStack() // Go back to movie list
                }
            )
        }
    }
}
