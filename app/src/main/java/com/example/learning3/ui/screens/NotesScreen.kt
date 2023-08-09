package com.example.learning3.ui.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learning3.composables.DeleteDialog
import com.example.learning3.composables.NotesGrid
import com.example.learning3.data.Note
import com.example.learning3.events.NoteEvent
import com.example.learning3.ui.state.NoteState
import com.example.learning3.navigation.Screen
import com.example.learning3.composables.SearchBar


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val selectedNotes = remember { mutableStateListOf<Note>() }
    val pinnedNotes = remember { mutableStateListOf<Note>() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    if (showDeleteDialog) {
        DeleteDialog(
            titleText = "Confirm deletion?",
            bodyText = "The selected notes will be deleted permanently",
            onDismissRequest = { showDeleteDialog = false },
            onConfirmButtonClick = {
                selectedNotes.forEach {
                    onEvent(NoteEvent.DeleteNote(it))
                }
                selectedNotes.clear()
                showDeleteDialog = false
            },
            onDismissButtonClick = {
                showDeleteDialog = false
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.inverseSurface,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddNote.route)
                },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
                Text("Add Note")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusManager.clearFocus()
            }) {
            if (selectedNotes.isEmpty()) {
                SearchBar(
                    modifier = Modifier.padding(16.dp),
                    query = state.searchQuery,
                    onQueryChange = { str ->
                        onEvent(NoteEvent.SetSearchQuery(str))
                        // Log.d(state.searchQuery,"")
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Feature under development",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                        {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    trailingIcon = {
                        if (state.searchQuery.isBlank()) {
                            IconButton(
                                onClick = {
                                    expanded = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options"
                                )
                            }
                            MaterialTheme(
                                shapes = MaterialTheme.shapes.copy(
                                    extraSmall = RoundedCornerShape(16.dp)
                                ),
                            ) {
                                DropdownMenu(
                                    expanded = expanded,
                                    offset = DpOffset(10.dp, 0.dp),
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Settings") },
                                        onClick = {
                                            expanded = false
                                            navController.navigate(Screen.Settings.route)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("About") },
                                        onClick = {
                                            expanded = false
                                            navController.navigate(Screen.About.route)
                                        }
                                    )
                                }
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    onEvent(NoteEvent.SetSearchQuery(""))
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search query"
                                )
                            }
                        }
                    }
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            "${selectedNotes.size} items selected",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.inverseSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.inverseSurface,
                        titleContentColor = MaterialTheme.colorScheme.inverseSurface
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                selectedNotes.forEach { note ->
                                    onEvent(NoteEvent.DisableIsSelected(note))
                                }
                                selectedNotes.clear()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Unselect all"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                showDeleteDialog = showDeleteDialog.not()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete notes"
                            )
                        }
                        if (selectedNotes.all { note ->
                                note.isPinned
                            }) {
                            // Unpin note
                            IconButton(
                                onClick = {
                                    selectedNotes.forEach { note ->
                                        onEvent(NoteEvent.UnpinNote(note))
                                        pinnedNotes.remove(note)
                                        onEvent(NoteEvent.DisableIsSelected(note))
                                    }
                                    Log.d("pin", pinnedNotes.size.toString())
                                    selectedNotes.clear()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PushPin,
                                    contentDescription = "Unpin notes"
                                )
                            }
                        } else if (selectedNotes.all { note ->
                                !note.isPinned
                            }) {
                            // Pin note
                            IconButton(
                                onClick = {
                                    selectedNotes.forEach { note ->
                                        onEvent(NoteEvent.PinNote(note))
                                        pinnedNotes.add(note)
                                        onEvent(NoteEvent.DisableIsSelected(note))
                                    }
                                    selectedNotes.clear()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PushPin,
                                    contentDescription = "Pin notes"
                                )
                            }
                        }
                    }
                )
            }
            NotesGrid(
                notes = state.notes,
                onNoteClick = { note ->
                    if (selectedNotes.size >= 1) {
                        if (note.isSelected) {
                            onEvent(NoteEvent.DisableIsSelected(note))
                            selectedNotes.remove(note)
                        } else {
                            onEvent(NoteEvent.EnableIsSelected(note))
                            selectedNotes.add(note)
                        }
                    } else {
                        navController.navigate("${Screen.ViewNote.route}/${note.id}")
                    }
                },
                onNoteLongClick = { note ->
                    if (note.isSelected) {
                        onEvent(NoteEvent.DisableIsSelected(note))
                        selectedNotes.remove(note)
                    } else {
                        onEvent(NoteEvent.EnableIsSelected(note))
                        selectedNotes.add(note)
                    }
                }
            )
        }
    }
}