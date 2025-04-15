package com.example.mogflix.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey() val id: Int = 0,
    val title: String,
    val description: String,
    @ColumnInfo(name = "watched_date")
    val watchedDate: Date,
    val rating: Float
)
