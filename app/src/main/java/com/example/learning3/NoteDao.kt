package com.example.learning3

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>
}