package com.example.learning3.navigation

sealed class Screen(val route: String) {
    object Notes: Screen("notes")
    object AddNote: Screen("add-note")
    object ViewNote: Screen("viewNote/{noteId}")
    object EditNote: Screen("editNote/{noteId}")
    object Settings: Screen("settings")
    object About: Screen("about")
}
