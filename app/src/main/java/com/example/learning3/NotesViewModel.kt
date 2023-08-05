package com.example.learning3

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NotesViewModel(private val noteDao: NoteDao) : ViewModel() {

//    private val _notes = noteDao.getNotes()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _notes = noteDao.getNotes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    private val _state = MutableStateFlow(NoteState())

    val state = combine(_state, _notes) { state, notes ->
        val filteredNotes = if (state.searchQuery.isBlank()) {
            notes
        } else {
            notes.filter { it.doesMatchSearchQuery(state.searchQuery) }
        }
        state.copy(
            notes = filteredNotes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())


    fun onEvent(event: NoteEvent) {
        when(event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteDao.deleteNote(event.note)
                }
            }
            NoteEvent.SaveNote -> {
                val title = state.value.title
                val content = state.value.content

                if (title.isBlank() || content.isBlank())
                    return

                if (state.value.editingNote != null) {
                    val editedNote = Note(
                        id = state.value.editingNote!!.id,
                        title = title,
                        content = content,
                        lastModified = System.currentTimeMillis()
                    )
                    viewModelScope.launch {
                        noteDao.upsertNote(editedNote)
                    }
                } else {
                    val note = Note(
                        title = title,
                        content = content,
                        lastModified = System.currentTimeMillis()
                    )
                    viewModelScope.launch {
                        noteDao.upsertNote(note)
                    }
                }
                _state.update {
                    it.copy(
                        title = "",
                        content = ""
                    )
                }
            }
            is NoteEvent.SetContent -> {
                _state.update {
                    it.copy(
                        content = event.content
                    )
                }
            }
            is NoteEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            is NoteEvent.StartEditing -> {
                _state.update {
                    it.copy(
                        title = event.note.title,
                        content = event.note.content,
                        editingNote = event.note
                    )
                }
            }

            is NoteEvent.SetSearchQuery -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query,
                        isSearching = event.query.isNotBlank()
                    )
                }
            }

            is NoteEvent.EnableIsSelected -> {
                val selectedNote = event.note
                selectedNote.isSelected = true
                viewModelScope.launch {
                    noteDao.upsertNote(selectedNote)
                }
            }

            is NoteEvent.DisableIsSelected -> {
                val selectedNote = event.note
                selectedNote.isSelected = false
                viewModelScope.launch {
                    noteDao.upsertNote(selectedNote)
                }
            }
        }
    }
}