package com.example.learning3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add new note") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.Notes.route)
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.title,
                    onValueChange = { onEvent(NoteEvent.SetTitle(it)) },
                    label = { Text("Title") }
                )
                TextField(
                    singleLine = false,
                    value = state.content,
                    onValueChange = { onEvent(NoteEvent.SetContent(it)) },
                    label = { Text("Content") }
                )
            }
            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.BottomEnd)
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        onEvent(NoteEvent.SaveNote)
                        navController.navigate(Screen.Notes.route)
                    }
                ) {
                    Icon(Icons.Filled.Done, contentDescription = "Save Note")
                    Text("Save Note")
                }
            }
        }
    )
}