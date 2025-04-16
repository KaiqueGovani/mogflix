package com.example.mogflix.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mogflix.data.model.Movie
import com.example.mogflix.ui.components.MovieCard
import com.example.mogflix.viewmodel.MovieViewModel
import java.util.Date

@Preview(showBackground = true)
@Composable
fun MoviesPreview() {
    val fakeViewModel = MovieViewModel().apply {
        addMovie(
            Movie(
                1,
                "Filme X",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                Date(2020),
                9.05f
            )
        )
        addMovie(
            Movie(
                2,
                "Filme Y",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                Date(2020),
                8.7f
            )
        )
    }

    MovieListScreen(
        viewModel = fakeViewModel,
        onAddMovieClick = {},
        onMovieClick = {}
    )
}

@Composable
fun MovieListScreen(
    onAddMovieClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    viewModel: MovieViewModel = viewModel(),
) {
    val movies = viewModel.movies.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (movies.value.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Parece que você ainda não adicionou nenhum filme…")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Clique no botão abaixo para adicionar!")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onAddMovieClick) {
                    Text(text = "+ Novo filme")
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(movies.value) { movie ->
                        MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onAddMovieClick
                ) {
                    Text(text = "+ Novo filme")
                }
            }
        }
    }
}