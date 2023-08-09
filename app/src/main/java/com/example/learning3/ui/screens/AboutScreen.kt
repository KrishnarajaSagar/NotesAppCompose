package com.example.learning3.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController
) {
    val uriHandler = LocalUriHandler.current
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
                        Icon(Icons.Default.ArrowBack,contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Card(
            modifier = Modifier
                .padding(padding)
                .padding(
                    horizontal = 16.dp,
                    vertical = 24.dp
                )
                .padding(top = 48.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    "Developed by Krishnaraja Sagar",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(16.dp)
                )
                Row {
                    TextButton(
                        onClick = {
                            uriHandler.openUri("https://github.com/KrishnarajaSagar")
                        },
                        modifier = Modifier
                            .padding(16.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                            contentColor = MaterialTheme.colorScheme.inverseSurface
                        )
                    ) {
                        Text("Github")
                    }
                    TextButton(
                        onClick = {
                            uriHandler.openUri("https://www.linkedin.com/in/krsagarts/")
                        },
                        modifier = Modifier
                            .padding(16.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                            contentColor = MaterialTheme.colorScheme.inverseSurface
                        )
                    ) {
                        Text("LinkedIn")
                    }
                }
            }
        }
    }
}