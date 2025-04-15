package com.example.mogflix.data.local

import android.content.Context
import androidx.room.Room

object MovieModule {
    lateinit var database: MovieDatabase
        private  set

    fun init(context: Context){
        database = Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movies_db"
        ).build()
    }
}