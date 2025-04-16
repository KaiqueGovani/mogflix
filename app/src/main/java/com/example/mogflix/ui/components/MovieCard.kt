package com.example.mogflix.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mogflix.data.model.Movie
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.remember

@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Assistido em: ${formatter.format(movie.watchedDate)}")
            Text(text = "Nota: ${movie.rating}")
        }
    }
}