package com.seravian.seravianapp

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_navigation.data.NavigationType
import com.seravian.feat_navigation.components.BottomNavigationBar
import com.greenvenom.core_navigation.data.repository.NavigationStateRepository
import com.seravian.feat_navigation.routes.Screen
import com.seravian.seravianapp.navigation.AppNavHost
import com.greenvenom.core_ui.theme.AppTheme
import org.koin.compose.koinInject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val navigationRepository = koinInject<NavigationStateRepository>()
            val navigationState by navigationRepository.navigationState.collectAsStateWithLifecycle()

            BackHandler {
                navigationRepository.navigate(NavigationType.Back)
            }

            AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        AppNavHost(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                        )

                        BottomNavigationBar(
                            defaultNavigationMethod = navigationRepository::navigate,
                            currentDestination = navigationState.currentDestination ?: Screen.Home,
                            isVisible = navigationState.bottomBarState
                        )
                    }
                }
            }
        }
    }
}