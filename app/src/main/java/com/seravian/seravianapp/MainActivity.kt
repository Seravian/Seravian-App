package com.seravian.seravianapp

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_navigation.data.NavigationType
import com.seravian.feat_navigation.components.BottomNavigationBar
import com.greenvenom.core_navigation.data.repository.NavigationStateRepository
import com.seravian.feat_navigation.routes.Screen
import com.seravian.seravianapp.navigation.AppNavHost
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.core_local.domain.PrefsDataSource
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val coroutineScope = rememberCoroutineScope()

            val navigationRepository = koinInject<NavigationStateRepository>()
            val navigationState by navigationRepository.navigationState.collectAsStateWithLifecycle()

            val appPrefsDataSource = koinInject<PrefsDataSource>()
            val appPrefsState by appPrefsDataSource.appPrefsState.collectAsStateWithLifecycle()

            BackHandler {
                navigationRepository.navigate(NavigationType.Back)
            }

            DisposableEffect (Unit) {
                val themeJob = coroutineScope.launch {
                    appPrefsDataSource.getThemePreference().collect {
                        appPrefsDataSource.changeTheme(it)
                        WindowCompat.getInsetsController(window, window.decorView)
                            .isAppearanceLightStatusBars = !it
                    }
                }
                onDispose { themeJob.cancel() }
            }

            AppTheme(darkTheme = appPrefsState.isDarkTheme) {
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