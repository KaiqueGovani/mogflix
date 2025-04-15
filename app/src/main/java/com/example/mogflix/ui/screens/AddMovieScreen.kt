package com.example.mogflix.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
    AddMovieScreen(onMovieAdded = {}, viewModel = MovieViewModel())
}

@Composable
fun AddMovieScreen(
    onMovieAdded: () -> Unit,
    viewModel: MovieViewModel
) {
    val suggestions by viewModel.suggestions.collectAsState()

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var showDateModal by remember { mutableStateOf<Boolean>(false) }
    var selectedWatchedDate by remember { mutableStateOf<Long?>(null) }
    var rating by remember { mutableFloatStateOf(0f) }



    LaunchedEffect(title) {
        kotlinx.coroutines.delay(300)
        viewModel.getMovieSuggestions(title.text)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(text = "Add a New Movie", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
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
                    }
                    .padding(vertical = 4.dp)
            )
        }

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { showDateModal = true },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Pick a date")
        }
        if (selectedWatchedDate != null) {
            val date = Date(selectedWatchedDate!!)
            val formatedDate = DateFormat.getDateInstance().format(date)
            Text("Selected date: $formatedDate")
        } else {
            Text("No date selected")
        }
        if (showDateModal)
            DatePickerModal(
                onDateSelected = {
                    selectedWatchedDate = it
                    showDateModal = false
                },
                onDismiss = {
                    showDateModal = false
                }
            )



        Text(text = "Rating")
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
                        contentDescription = null
                    )
                }
            }
        }

        Button(
            onClick = {
                onMovieAdded();
                viewModel.addMovie(
                    Movie(
                        1,
                        title.text,
                        description.text,
                        Date(selectedWatchedDate!!),
                        rating
                    )
                )
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add")
        }
    }


}