package com.example.learning3.events

import com.example.learning3.data.Note

sealed interface NoteEvent {
    object SaveNote: NoteEvent
    object ClearAllNotes: NoteEvent
    // data class SaveEditedNote(val note: Note): NoteEvent
    data class SetTitle(val title: String): NoteEvent
    data class SetContent(val content: String): NoteEvent
    data class SetSearchQuery(val query: String): NoteEvent
    // object SearchNotes: NoteEvent
    data class EnableIsSelected(val note: Note): NoteEvent
    data class DisableIsSelected(val note: Note): NoteEvent
    data class PinNote(val note: Note): NoteEvent
    data class UnpinNote(val note: Note): NoteEvent
    data class DeleteNote(val note: Note): NoteEvent
    data class StartEditing(val note: Note): NoteEvent
    // object StopEditing: NoteEvent
}