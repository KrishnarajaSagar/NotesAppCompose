package com.example.learning3.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning3.data.Note
import com.example.learning3.data.NoteDao
import com.example.learning3.events.NoteEvent
import com.example.learning3.ui.state.NoteState
import com.example.learning3.utilities.UtilityFunctions.exportNoteToPDF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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
        val sortedNotes = notes.sortedWith(compareByDescending<Note> { it.isPinned }.thenBy { it.id })
        val filteredNotes = if (state.searchQuery.isBlank()) {
            sortedNotes
        } else {
            sortedNotes.filter { it.doesMatchSearchQuery(state.searchQuery) }
        }
        state.copy(
            notes = filteredNotes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())


    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: NoteEvent) {
        when(event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteDao.deleteNote(event.note)
                }
            }
            is NoteEvent.ClearAllNotes -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        noteDao.clearAllNotes()
                    }
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
            is NoteEvent.ExportNote -> {
                exportNoteToPDF(event.note)
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

            is NoteEvent.PinNote -> {
                val toBePinnedNote = event.note
                toBePinnedNote.isPinned = true
                viewModelScope.launch {
                    noteDao.upsertNote(toBePinnedNote)
                }
            }
            is NoteEvent.UnpinNote -> {
                val toBeUnpinnedNote = event.note
                toBeUnpinnedNote.isPinned = false
                viewModelScope.launch {
                    noteDao.upsertNote(toBeUnpinnedNote)
                }
            }
        }
    }
}