package com.example.learning3

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
    val menuItems = listOf<String>(
        "Edit note",
        "Delete note"
    )
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            TopAppBar(
                title = {},
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
                        menuItems.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    expanded = false
                                }
                            )
                        }
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
                text = note!!.title,
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
                text = note.content,
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