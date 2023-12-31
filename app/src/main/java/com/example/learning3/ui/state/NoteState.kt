package com.example.learning3.ui.state

import com.example.learning3.data.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val content: String = "",
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val editingNote: Note? = null,
)
