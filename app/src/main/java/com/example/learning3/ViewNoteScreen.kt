package com.example.learning3

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewNoteScreen(
    navController: NavController,
    selectedNoteId: Long,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit
) {
    val note: Note? = state.notes.find { note ->
        note.id == selectedNoteId
    }
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val menuItems = listOf<String>(
        "Edit note",
        "Delete note"
    )
    if (showDeleteDialog) {
        DeleteDialog(
            titleText = "Confirm deletion?",
            bodyText = "The note will be deleted permanently",
            onDismissRequest = { showDeleteDialog = false },
            onConfirmButtonClick = {
                onEvent(NoteEvent.DeleteNote(note!!))
                navController.navigate(Screen.Notes.route) {
                    popUpTo(Screen.Notes.route) {
                        inclusive = true
                    }
                }
            },
            onDismissButtonClick = {
                showDeleteDialog = false
            }
        )
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = "Last modified on ${UtilityFunctions.formatDateAndTime(note?.lastModified ?: 0)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                    )) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack,contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(Icons.Default.MoreVert,
                            contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                expanded = false
                                onEvent(NoteEvent.StartEditing(note!!))
                                navController.navigate("${Screen.EditNote.route}/${selectedNoteId}")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                expanded = false
                                showDeleteDialog = showDeleteDialog.not()
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(
                    horizontal = 16.dp,
                )
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = note?.title ?: "",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 22.sp
                ),
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Text(
                text = note?.content ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                )
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
        }
    }
}