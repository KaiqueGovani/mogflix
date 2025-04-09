package com.example.mogflix.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "pt-BR"
    ): MovieSearchResponse

}