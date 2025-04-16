package com.example.mogflix.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mogflix.viewmodel.MovieViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieViewModel = viewModel(),
    onBack: () -> Unit
) {
    val movie = viewModel.movies.collectAsState().value.find { it.id == movieId }

    if (movie == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Filme não encontrado.")
        }
        return
    }

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Filme") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = movie.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Data assistido: ${dateFormatter.format(movie.watchedDate)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Nota: ${movie.rating}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Descrição", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))

            Text(text = movie.description.ifEmpty { "Sem descrição disponível." })
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.deleteMovieById(movie.id)
                    onBack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Excluir Filme")
            }
        }
    }
}