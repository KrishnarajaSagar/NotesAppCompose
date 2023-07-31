package com.example.learning3

sealed class Screen(val route: String) {
    object Notes: Screen("notes")
    object AddNote: Screen("add-note")
    object ViewNote: Screen("viewNote/{noteId}")
}
