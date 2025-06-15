package com.seravian.feat_navigation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.seravian.core_navigation.data.NavigationType
import com.seravian.core_navigation.domain.Destination
import com.seravian.feat_navigation.R
import com.seravian.feat_navigation.routes.Screen

@Composable
fun BottomNavigationBar(
    defaultNavigationMethod: (NavigationType) -> Unit,
    currentAccountTypeIndex: Int,
    currentDestination: Destination,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomBarContent(
                defaultNavigationMethod = defaultNavigationMethod,
                currentAccountTypeIndex = currentAccountTypeIndex,
                currentDestination = currentDestination
            )
        }
    )
}

@Composable
private fun BottomBarContent(
    defaultNavigationMethod: (NavigationType) -> Unit,
    currentAccountTypeIndex: Int,
    currentDestination: Destination
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        if (currentAccountTypeIndex == 0) {
            PatientDestination.entries.forEach { destination ->
                NavigationBarItem(
                    onClick = {
                        if (currentDestination::class != destination.target::class) {
                            defaultNavigationMethod(
                                NavigationType.BottomNavigation(destination.target)
                            )
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = stringResource(
                                R.string.navigation_icon,
                                destination.label
                            ),
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destination.label),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    selected = destination.target == currentDestination,
                )
            }
        } else {
            DoctorDestination.entries.forEach { destination ->
                NavigationBarItem(
                    onClick = {
                        if (currentDestination::class != destination.target::class) {
                            defaultNavigationMethod(
                                NavigationType.BottomNavigation(destination.target)
                            )
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = stringResource(
                                R.string.navigation_icon,
                                destination.label
                            )
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destination.label),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    selected = destination.target == currentDestination,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BottomNavigationBarContent() {
    BottomBarContent(
        defaultNavigationMethod = {  },
        currentAccountTypeIndex = 0,
        currentDestination = Screen.Home
    )
}
