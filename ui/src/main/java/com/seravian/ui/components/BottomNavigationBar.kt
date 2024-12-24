package com.seravian.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seravian.ui.theme.SeravianTheme

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
    BarContent()
}

@Composable
private fun BarContent() {
    val colorScheme = MaterialTheme.colorScheme

    NavigationBar(
        containerColor = colorScheme.surface,
        contentColor = colorScheme.onSurface
    ) {
        NavigationBarItem(
            icon = { Box(Modifier.size(24.dp)) { /* Placeholder for Doctors Icon */ } },
            label = { Text("Doctors", style = MaterialTheme.typography.labelSmall) },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Box(Modifier.size(24.dp)) { /* Placeholder for AI Chat Icon */ } },
            label = { Text("AI Chat", style = MaterialTheme.typography.labelSmall) },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Box(Modifier.size(24.dp)) { /* Placeholder for Sessions Icon */ } },
            label = { Text("Sessions", style = MaterialTheme.typography.labelSmall) },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Box(Modifier.size(24.dp)) { /* Placeholder for Account Icon */ } },
            label = { Text("Account", style = MaterialTheme.typography.labelSmall) },
            selected = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun BottomNavigationBarContent() {
    SeravianTheme {
        BarContent()
    }
}
