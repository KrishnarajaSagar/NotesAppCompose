package com.example.learning3

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val content: String = "",
    val editingNote: Note? = null,
    val isEditing: Boolean = false
)
