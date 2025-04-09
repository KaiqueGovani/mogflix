package com.example.mogflix.ui.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mogflix.data.model.Movie
import com.example.mogflix.viewmodel.MovieViewModel

@Preview(showBackground = true)
@Composable
fun PreviewMovieScreen() {
    AddMovieScreen(onMovieAdded = {})
}

@Composable
fun AddMovieScreen(
    viewModel: MovieViewModel = viewModel(),
    onMovieAdded: () -> Unit,
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue(""))}
    var year by remember { mutableStateOf(TextFieldValue("")) }
    var rating by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

        Text(text = "Add a New Movie", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Release Year") },
            modifier = Modifier.fillMaxWidth()
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
                viewModel.addMovie(Movie(1, title.text, description.text, year.text.toInt(), rating) )
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add")
        }
    }


}