package com.example.learning3.app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learning3.ui.screens.AddNoteScreen
import com.example.learning3.ui.screens.EditNoteScreen
import com.example.learning3.ui.screens.NotesScreen
import com.example.learning3.viewmodel.NotesViewModel
import com.example.learning3.navigation.Screen
import com.example.learning3.ui.screens.ViewNoteScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesApp(viewModel: NotesViewModel) {
    val navController = rememberNavController()

    val state by viewModel.state.collectAsState()

    NavHost(navController, startDestination = Screen.Notes.route) {
        composable(Screen.Notes.route) {
            NotesScreen(
                navController = navController,
                state = state,
                onEvent = viewModel::onEvent
            )
        }
        composable(Screen.AddNote.route) {
            AddNoteScreen(
                navController = navController,
                state = state,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = "${Screen.ViewNote.route}/{selectedId}",
            arguments = listOf(navArgument("selectedId") {type = NavType.LongType})
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("selectedId")
            noteId?.let {
                ViewNoteScreen(
                    navController = navController,
                    selectedNoteId = noteId,
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }
        }
        composable(
            route = "${Screen.EditNote.route}/{selectedId}",
            arguments = listOf(navArgument("selectedId") { type = NavType.LongType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("selectedId")
            noteId?.let {
                EditNoteScreen(
                    navController = navController,
                    selectedNoteId = noteId,
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun NotesScreenPreview() {
//    Learning3Theme {
//        val notes: List<Note> = mutableListOf(
//            Note(title = "Note 1", content = "Content"),
//            Note(title = "Note 2", content = "Content"),
//            Note(title = "Note 3", content = "Content"),
//            Note(title = "Note 4", content = "Content")
//        )
//        val onNoteClicked: (Note) -> Unit = { clickedNote ->
//
//        }
//        NotesScreen(notes = notes, onNoteClicked = onNoteClicked)
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun AddNoteScreenPreview() {
//    Learning3Theme {
//        val note: Note = Note("Note 1", "Content")
//        val onSaveNote: (Note) -> Unit = {}
//        AddNoteScreen(onSaveNote = onSaveNote)
//    }
//}