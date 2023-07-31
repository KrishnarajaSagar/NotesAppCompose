package com.example.learning3

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var database: NoteDatabase ?= null

    fun getDatabase(context: Context): NoteDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "notes_database"
            ).build()
            database = instance
            instance
        }
    }
}