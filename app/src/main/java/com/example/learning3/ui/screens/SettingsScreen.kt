package com.example.learning3.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learning3.R
import com.example.learning3.StoreThemePrefs
import com.example.learning3.composables.DeleteDialog
import com.example.learning3.composables.SettingsListItem
import com.example.learning3.data.DarkThemeConfig
import com.example.learning3.events.NoteEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    onEvent: (NoteEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreThemePrefs(context)
    var useDynamicThemeChecked by remember { mutableStateOf(true) }
    var themeModeExpanded by remember { mutableStateOf(false) }
    val themeModeOptions = listOf(
        "AUTO",
        "LIGHT",
        "DARK"
    )
    var selectedOptionText by remember { mutableStateOf(themeModeOptions[0]) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteDialog(
            titleText = "Confirm deletion?",
            bodyText = "All notes will be deleted permanently",
            onDismissRequest = { showDeleteDialog = false },
            onConfirmButtonClick = {
                onEvent(NoteEvent.DeleteAllNotes)
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
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.inverseSurface,
                    titleContentColor = MaterialTheme.colorScheme.inverseSurface
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 24.dp
                    )
                    .padding(top = 48.dp)
            )
            SettingsListItem(
                title = "Dynamic Theme",
                trailingContent = {
                    Switch(
                        checked = dataStore.isDynamicThemeEnabled.collectAsState(initial = true).value,
                        onCheckedChange = {
                            useDynamicThemeChecked = it
                            scope.launch {
                                dataStore.setDynamicThemeEnabled(it)
                            }
                        }
                    )
                }
            )
            if (!dataStore.isDynamicThemeEnabled.collectAsState(initial = true).value) {
                SettingsListItem(
                    title = "Theme Mode",
                    trailingContent = {
                        MaterialTheme(
                            shapes = MaterialTheme.shapes.copy(
                                extraSmall = RoundedCornerShape(16.dp)
                            ),
                        ) {
                            ExposedDropdownMenuBox(
                                expanded = themeModeExpanded,
                                onExpandedChange = {
                                    themeModeExpanded = !themeModeExpanded
                                }
                            ) {
                                TextField(
                                    readOnly = true,
                                    value = dataStore.getThemeMode.collectAsState(initial = DarkThemeConfig.AUTO.toString()).value,
                                    onValueChange = { },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = themeModeExpanded
                                        )
                                    },
                                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .width(140.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = themeModeExpanded,
                                    onDismissRequest = { themeModeExpanded = false }
                                ) {
                                    themeModeOptions.forEach {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    it,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            },
                                            onClick = {
                                                selectedOptionText = it
                                                scope.launch {
                                                    dataStore.setThemeMode(it)
                                                }
                                                themeModeExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
                SettingsListItem(
                    title = "Color scheme",
                    trailingContent = null
                )
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        )
                        .horizontalScroll(rememberScrollState())
                ) {
                    // Brown
                    Card(
                        modifier = Modifier
                            .height(200.dp)
                            .width(96.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFDDB0),
                        ),
                        onClick = {
                            scope.launch {
                                dataStore.setColor("BROWN")
                            }
                        },
                        border = CardDefaults.outlinedCardBorder(
                            dataStore.getColor.collectAsState(initial = "BROWN").value == "BROWN"
                        )
                    ) {

                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    // Green
                    Card(
                        modifier = Modifier
                            .height(200.dp)
                            .width(96.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF9EF981),
                        ),
                        onClick = {
                            scope.launch {
                                dataStore.setColor("GREEN")
                            }
                        },
                        border = CardDefaults.outlinedCardBorder(
                            dataStore.getColor.collectAsState(initial = "BROWN").value == "GREEN"
                        )
                    ) {

                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    // Blue
                    Card(
                        modifier = Modifier
                            .height(200.dp)
                            .width(96.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFC9E6FF),
                        ),
                        onClick = {
                            scope.launch {
                                dataStore.setColor("BLUE")
                            }
                        },
                        border = CardDefaults.outlinedCardBorder(
                            dataStore.getColor.collectAsState(initial = "BROWN").value == "BLUE"
                        )
                    ) {

                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    // Purple
                    Card(
                        modifier = Modifier
                            .height(200.dp)
                            .width(96.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF2DAFF),
                        ),
                        onClick = {
                            scope.launch {
                                dataStore.setColor("PURPLE")
                            }
                        },
                        border = CardDefaults.outlinedCardBorder(
                            dataStore.getColor.collectAsState(initial = "BROWN").value == "PURPLE"
                        )
                    ) {

                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    // Pink
                    Card(
                        modifier = Modifier
                            .height(200.dp)
                            .width(96.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFD9E2),
                        ),
                        onClick = {
                            scope.launch {
                                dataStore.setColor("PINK")
                            }
                        },
                        border = CardDefaults.outlinedCardBorder(
                            dataStore.getColor.collectAsState(initial = "BROWN").value == "PINK"
                        )
                    ) {

                    }
                    Spacer(modifier = Modifier.width(24.dp))
                }
            }
            SettingsListItem(
                title = "Clear All Notes",
                trailingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_delete_forever_40),
                        contentDescription = "Clear All Notes",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            showDeleteDialog = true
                        }
                    )
                }
            )
        }
    }
}