package com.example.learning3

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.learning3.ui.theme.Learning3Theme

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