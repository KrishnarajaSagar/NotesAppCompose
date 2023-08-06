package com.example.learning3.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 3,
    autoMigrations = [
        AutoMigration(1,2),
        AutoMigration(2,3)
    ]
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
}