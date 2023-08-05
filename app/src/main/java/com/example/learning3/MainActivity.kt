package com.example.learning3

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.learning3.ui.theme.Learning3Theme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "notes.db"
        ).build()
    }

    private val viewModel by viewModels<NotesViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(db.noteDao) as T
                }
            }
        }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Learning3Theme {
                NotesApp(viewModel)
            }
        }
    }
}

class DemoNote(
    val title: String,
    val content: String,
)

val notes: List<DemoNote> = listOf(
    DemoNote("Title Title Title Title Title", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae purus. Pharetra magna ac placerat vestibulum lectus mauris ultrices. Enim sit amet venenatis urna cursus eget nunc scelerisque viverra. Auctor eu augue ut lectus arcu bibendum at varius. Viverra orci sagittis eu volutpat. A pellentesque sit amet porttitor eget dolor morbi non. Ipsum consequat nisl vel pretium lectus quam id leo in. Convallis a cras semper auctor neque vitae tempus quam. Orci a scelerisque purus semper eget duis at tellus at. Quam adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus. Porttitor lacus luctus accumsan tortor posuere. Ac ut consequat semper viverra nam libero justo laoreet. Accumsan tortor posuere ac ut consequat semper viverra."),
    DemoNote("Title Title Title Title Title", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae purus. Pharetra magna ac placerat vestibulum lectus mauris ultrices. Enim sit amet venenatis urna cursus eget nunc scelerisque viverra. Auctor eu augue ut lectus arcu bibendum at varius. Viverra orci sagittis eu volutpat. A pellentesque sit amet porttitor eget dolor morbi non. Ipsum consequat nisl vel pretium lectus quam id leo in. Convallis a cras semper auctor neque vitae tempus quam. Orci a scelerisque purus semper eget duis at tellus at. Quam adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus. Porttitor lacus luctus accumsan tortor posuere. Ac ut consequat semper viverra nam libero justo laoreet. Accumsan tortor posuere ac ut consequat semper viverra."),
    DemoNote("Title Title Title Title Title", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 4", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 1", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 2", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 3", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 4", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 1", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 2", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 3", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 4", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 1", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 2", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 3", "ih sdi uhsu dasuh xkcn kjn sdj"),
    DemoNote("Title 4", "ih sdi uhsu dasuh xkcn kjn sdj"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesGrid() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(8.dp)
            // contentPadding = PaddingValues(10.dp)
        ) {
            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    onClick = {}
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                        )
                        Text(
                            text = note.content,
                            maxLines = 8,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

data class MenuItem(
    val index: Int,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualNoteView() {
    val note: DemoNote = DemoNote("Title Title Title Title Title Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae purus. Pharetra magna ac placerat vestibulum lectus mauris ultrices.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae purus. Pharetra magna ac placerat vestibulum lectus mauris ultrices. Enim sit amet venenatis urna cursus eget nunc scelerisque viverra. Auctor eu augue ut lectus arcu bibendum at varius. Viverra orci sagittis eu volutpat. A pellentesque sit amet porttitor eget dolor morbi non. Ipsum consequat nisl vel pretium lectus quam id leo in. Convallis a cras semper auctor neque vitae tempus quam. Orci a scelerisque purus semper eget duis at tellus at. Quam adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus. Porttitor lacus luctus accumsan tortor posuere. Ac ut consequat semper viverra nam libero justo laoreet. Accumsan tortor posuere ac ut consequat semper viverra.")

    var expanded by remember { mutableStateOf(false) }
    val menuItems = listOf<String>(
        "Edit note",
        "Delete note"
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {}
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
                    horizontal = 16.dp
                )
        ) {
            Text(
                text = note.title,
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteView() {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(Icons.Default.ArrowBack,contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(
                    horizontal = 16.dp
                )
                .padding(
                    bottom = 16.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            TextField(
                value = title,
                onValueChange = { newTitle ->
                    title = newTitle
                },
                placeholder = { Text("Title", style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 22.sp
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            TextField(
                value = content,
                onValueChange = { newContent ->
                    content = newContent
                },
                placeholder = { Text("Content", style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(note: DemoNote) {
    Card(
        modifier = Modifier
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        onClick = {}
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesSearchBar() {
    Box(
        modifier = Modifier
            .padding(
                16.dp
            )
            .wrapContentHeight()
            .background(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(60.dp)
            ),
    ) {
        TextField(
            value = "",
            onValueChange = { },
            placeholder = { Text("Search", style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )) },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
            ),
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            },
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AppPreview() {
    Learning3Theme {
        CreateNoteView()
    }
}