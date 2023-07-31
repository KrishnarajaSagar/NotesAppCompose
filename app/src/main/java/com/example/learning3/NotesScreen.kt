package com.example.learning3

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit
) {
    Scaffold(
    topBar = {
        TopAppBar(
            title = { Text(text = "Notes") },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    },
    content = { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LazyColumn {
                items(state.notes) { note ->
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.clickable {
                            navController.navigate("${Screen.ViewNote.route}/${note.id}")
                        }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize()
                .wrapContentSize(Alignment.BottomEnd)
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddNote.route)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
                Text("Add Note")
            }
        }
    }
    )
}