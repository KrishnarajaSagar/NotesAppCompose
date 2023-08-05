package com.example.learning3

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 2,
    autoMigrations = [
        AutoMigration(1,2)
    ]
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
}