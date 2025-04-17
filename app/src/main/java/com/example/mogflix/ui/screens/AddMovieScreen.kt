package com.example.mogflix.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mogflix.data.model.Movie
import com.example.mogflix.ui.components.DatePickerModal
import com.example.mogflix.viewmodel.MovieViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun PreviewMovieScreen() {
    AddMovieScreen(onMovieAdded = {}, onMovieCancelled = {}, viewModel = MovieViewModel())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovieScreen(
    onMovieAdded: () -> Unit,
    onMovieCancelled: () -> Unit,
    viewModel: MovieViewModel
) {
    val suggestions by viewModel.suggestions.collectAsState()

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var showDateModal by remember { mutableStateOf(false) }
    var selectedWatchedDate by remember { mutableStateOf<Long?>(null) }
    var rating by remember { mutableFloatStateOf(0f) }
    var movieSelected by remember { mutableStateOf(false) }

    LaunchedEffect(title.text) {
        if (title.text.isNotBlank() && !movieSelected) {
            kotlinx.coroutines.delay(300)
            viewModel.getMovieSuggestions(title.text)
        }
    }

    val dateText = selectedWatchedDate?.let {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
    } ?: "Nenhuma data selecionada"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Adicionar Filme",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMovieCancelled) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Preencha as informações abaixo",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    movieSelected = false
                },
                label = { Text("Título do filme") },
                modifier = Modifier.fillMaxWidth()
            )

            suggestions.forEach { suggestion ->
                Text(
                    text = "${suggestion.title} (${suggestion.release_date ?: "?"})",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            title = TextFieldValue(suggestion.title)
                            viewModel.clearSuggestions()
                            movieSelected = true
                        }
                        .padding(vertical = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição pessoal") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Column {
                Text("Data assistida", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dateText,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { showDateModal = true },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text("Selecionar")
                    }
                }
            }

            if (showDateModal) {
                DatePickerModal(
                    onDateSelected = {
                        selectedWatchedDate = it
                        showDateModal = false
                    },
                    onDismiss = { showDateModal = false }
                )
            }

            Text("Avaliação", style = MaterialTheme.typography.titleMedium)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                (1..5).forEach { i ->
                    IconToggleButton(
                        checked = rating >= i,
                        onCheckedChange = { rating = i.toFloat() }
                    ) {
                        Icon(
                            imageVector = if (rating >= i) Icons.Default.Star else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = if (rating >= i)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (selectedWatchedDate != null) {
                        viewModel.addMovie(
                            Movie(
                                id = 0,
                                title = title.text,
                                description = description.text,
                                watchedDate = Date(selectedWatchedDate!!),
                                rating = rating
                            )
                        )
                        onMovieAdded()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = title.text.isNotBlank() && selectedWatchedDate != null,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Adicionar")
            }
        }
    }
}