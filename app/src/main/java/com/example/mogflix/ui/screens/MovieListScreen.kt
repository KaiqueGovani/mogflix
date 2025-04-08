package com.example.mogflix.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun MoviesPreview(){
    MovieListScreen(onAddMovieClick = {})
}


@Composable
fun MovieListScreen(
    onAddMovieClick: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                textAlign = TextAlign.Center,
                text = "Parece que você ainda não adicionou nenhum filme...",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height((8.dp)))
            Text(text = "Clique no botão abaixo para adicionar!")

            Spacer(modifier = Modifier.height((16.dp)))

            Button(onClick = onAddMovieClick) {
                Text(
                    text = "+ Novo filme"
                )
            }

        }
    }
}