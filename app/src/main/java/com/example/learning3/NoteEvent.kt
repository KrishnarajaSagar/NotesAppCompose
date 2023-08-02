package com.example.learning3

sealed interface NoteEvent {
    object SaveNote: NoteEvent
    // data class SaveEditedNote(val note: Note): NoteEvent
    data class SetTitle(val title: String): NoteEvent
    data class SetContent(val content: String): NoteEvent
    data class DeleteNote(val note: Note): NoteEvent
    data class StartEditing(val note: Note): NoteEvent
    // object StopEditing: NoteEvent
}