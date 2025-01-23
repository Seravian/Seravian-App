package com.greenvenom.navigation.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.greenvenom.navigation.AppDestination
import com.greenvenom.navigation.R

@Composable
fun BottomNavigationBar(
    navigateToHome: () -> Unit,
    navigateToAIChat: () -> Unit,
    navigateToSessions: () -> Unit,
    navigateToDoctors: () -> Unit,
    currentDestination: AppDestination,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BarContent(
                navigateToHome = { navigateToHome() },
                navigateToAIChat = { navigateToAIChat() },
                navigateToSessions = { navigateToSessions() },
                navigateToDoctors = { navigateToDoctors() },
                currentDestination = currentDestination
            )
        }
    )
}

@Composable
private fun BarContent(
    navigateToHome: () -> Unit,
    navigateToAIChat: () -> Unit,
    navigateToSessions: () -> Unit,
    navigateToDoctors: () -> Unit,
    currentDestination: AppDestination
) {
    val colorScheme = MaterialTheme.colorScheme

    NavigationBar(
        containerColor = colorScheme.surface,
        contentColor = colorScheme.onSurface
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.home_ic),
                    contentDescription ="Home Nav Icon "
                )
            },
            label = { Text("Home", style = MaterialTheme.typography.labelLarge) },
            selected = currentDestination is AppDestination.Home,
            onClick = { navigateToHome() }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.chat_ic),
                    contentDescription ="AI Chat Nav Icon "
                )
            },
            label = { Text("AI Chat", style = MaterialTheme.typography.labelLarge) },
            selected = currentDestination is AppDestination.AIChat,
            onClick = { navigateToAIChat() }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.doctor_nav_ic),
                    contentDescription ="Doctors Nav Icon "
                )
            },
            label = { Text("Doctors", style = MaterialTheme.typography.labelLarge) },
            selected = currentDestination is AppDestination.Doctors,
            onClick = { navigateToDoctors() }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.sessions_ic),
                    contentDescription ="Sessions Nav Icon "
                )
            },
            label = { Text("Sessions", style = MaterialTheme.typography.labelLarge) },
            selected = currentDestination is AppDestination.Sessions,
            onClick = { navigateToSessions() }
        )
    }
}

@PreviewLightDark
@Composable
private fun BottomNavigationBarContent() {
    BarContent(
        navigateToHome = {  },
        navigateToAIChat = {  },
        navigateToSessions = {  },
        navigateToDoctors = {  },
        currentDestination = AppDestination.Home
    )
}
