package com.example.learning3.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learning3.composables.DeleteDialog
import com.example.learning3.data.Note
import com.example.learning3.events.NoteEvent
import com.example.learning3.ui.state.NoteState
import com.example.learning3.navigation.Screen
import com.example.learning3.composables.SearchBar
import com.example.learning3.utilities.UtilityFunctions.formatDateAndTime


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    navController: NavController,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit
) {
    val selectedNotes = remember { mutableStateListOf<Note>() }
    var showDeleteDialog by remember { mutableStateOf(false) }

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
    ) { padding ->
        Column {
            if (selectedNotes.isEmpty()) {
                SearchBar(
                    modifier = Modifier.padding(16.dp),
                    query = state.searchQuery,
                    onQueryChange = {
                        onEvent(NoteEvent.SetSearchQuery(it))
                        // Log.d(state.searchQuery,"")
                    },
                    onClearClick = {
                        onEvent(NoteEvent.SetSearchQuery(""))
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = {  }
                        )
                        {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    isQueryEmpty = state.searchQuery.isBlank()
                )
            } else {
                TopAppBar(
                    title = {
                        Text("${selectedNotes.size} items selected", style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ))
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
                                selectedNotes.forEach {
                                    onEvent(NoteEvent.DisableIsSelected(it))
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
                    }
                )
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.padding(
                    horizontal = 12.dp
                )
                // contentPadding = PaddingValues(10.dp)
            ) {
                items(state.notes) { note ->
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .combinedClickable(
                                onClick = {
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
                                onLongClick = {
                                    if (note.isSelected) {
                                        onEvent(NoteEvent.DisableIsSelected(note))
                                        selectedNotes.remove(note)
                                    } else {
                                        onEvent(NoteEvent.EnableIsSelected(note))
                                        selectedNotes.add(note)
                                    }
                                }
                            ),
//                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!note.isSelected)MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
                            contentColor = if (!note.isSelected)MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer
                        ),
//                        onClick = {
//                            navController.navigate("${Screen.ViewNote.route}/${note.id}")
//                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxHeight(),
                        ) {
                            Text(
                                text = note.title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 22.sp
                                ),
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(8.dp)
                            )
                            Text(
                                text = note.content,
                                maxLines = 8,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp,
                                )
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(8.dp)
                            )
                            Text(
                                text = formatDateAndTime(note.lastModified),
                                maxLines = 1,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                ),
                            )
                        }
                    }
                }
            }

        }
    }
}

