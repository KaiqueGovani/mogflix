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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.text.font.FontWeight
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
    var overview by remember { mutableStateOf("") }
    var posterPath by remember { mutableStateOf<String?>("") }
    var showDateModal by remember { mutableStateOf(false) }
    var selectedWatchedDate by remember { mutableStateOf<Long?>(null) }
    var rating by remember { mutableFloatStateOf(0f) }
    var movieSelected by remember { mutableStateOf(false) }

    val isFormValid = title.text.isNotBlank() && description.text.isNotBlank() && selectedWatchedDate != null && rating > 0

    LaunchedEffect(title.text) {
        if (title.text.isNotBlank() && !movieSelected) {
            kotlinx.coroutines.delay(300)
            viewModel.getMovieSuggestions(title.text)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Filme") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                navigationIcon = {
                    IconButton(onClick = onMovieCancelled) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        }
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title field with suggestions
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    movieSelected = false
                                },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true
            )

            // Movie suggestions
            if (suggestions.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            "Sugestões",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                        )

                        suggestions.forEach { suggestion ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        title = TextFieldValue(suggestion.title)
                                        overview = suggestion.overview
                                        posterPath = suggestion.poster_path
                                        viewModel.clearSuggestions()
                                        movieSelected = true
                                    },
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Text(
                                    text = "${suggestion.title} (${suggestion.release_date ?: "?"})",
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            // Description field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                minLines = 3
            )

            // Date picker section
            Column {
                Text(
                    text = "Data assistida",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedButton(
                    onClick = { showDateModal = true },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                ) {
                    Icon(
                        Icons.Filled.CalendarMonth,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (selectedWatchedDate != null) {
                        val date = Date(selectedWatchedDate!!)
                        val formattedDate = DateFormat.getDateInstance().format(date)
                        Text(formattedDate)
                    } else {
                        Text("Selecionar data")
                    }
                }

                if (showDateModal) {
                    DatePickerModal(
                        onDateSelected = {
                            selectedWatchedDate = it
                            showDateModal = false
                        },
                        onDismiss = {
                            showDateModal = false
                        }
                    )
                }
            }

            // Rating section
            Column {
                Text(
                    text = "Avaliação",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                                tint = if (rating >= i) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    if (rating > 0) {
                        Text(
                            text = rating.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Add button
            Button(
                onClick = {
                    if (isFormValid) {
                        viewModel.addMovie(
                            Movie(
                                0,
                                title.text,
                                description.text,
                                Date(selectedWatchedDate!!),
                                rating,
                                overview,
                                posterPath
                            )
                        )
                        onMovieAdded()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            ) {
                Icon(
                    Icons.Filled.Save,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Salvar Filme")
            }
        }
    }


}