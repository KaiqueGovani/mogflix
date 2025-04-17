package com.example.mogflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mogflix.data.local.MovieDatabase
import com.example.mogflix.data.local.MovieModule
import com.example.mogflix.data.model.Movie
import com.example.mogflix.ui.navigation.NavRoutes
import com.example.mogflix.ui.screens.AddMovieScreen
import com.example.mogflix.ui.screens.MovieListScreen
import com.example.mogflix.ui.theme.MogflixTheme
import com.example.mogflix.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database
        MovieModule.init(this)

        setContent {
            MogflixTheme(
                dynamicColor = false
            ) {
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
    val viewModel = MovieViewModel();
//    val fakeViewModel = MovieViewModel().apply {
//        addMovie(Movie(1, "Filme X", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ornare gravida lacus.", 2020, 9.05f))
//        addMovie(Movie(2, "Filme Y", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ornare gravida lacus.", 2020, 9.05f))
//    }

    NavHost(navController = navController, startDestination = NavRoutes.MovieList.route) {
        composable(NavRoutes.MovieList.route) {
            MovieListScreen(
                viewModel = viewModel,
                onAddMovieClick = {
                    navController.navigate(NavRoutes.AddMovie.route)
                },
                onMovieClick = { movieId ->
                    navController.navigate(NavRoutes.MovieDetails.createRoute(movieId))
                }
            )
        }
        composable(NavRoutes.AddMovie.route) {
            AddMovieScreen(
                viewModel = viewModel,
                onMovieAdded = { navController.popBackStack() },
                onMovieCancelled = { navController.popBackStack() }
            )
        }
        composable(
            route = NavRoutes.MovieDetails.route,
            arguments = listOf(
                androidx.navigation.navArgument("movieId") { type = androidx.navigation.NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1
            com.example.mogflix.ui.screens.MovieDetailScreen(
                movieId = movieId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
