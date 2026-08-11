package ir.parvaz.calendar.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import ir.parvaz.calendar.adhan.AdhanScheduler
import ir.parvaz.calendar.notification.DateNotificationHelper
import ir.parvaz.calendar.ui.components.ParvazBottomNavBar
import ir.parvaz.calendar.ui.components.ParvazNavigationDrawer
import ir.parvaz.calendar.ui.screens.calendar.CalendarScreen
import ir.parvaz.calendar.ui.screens.home.HomeScreen
import ir.parvaz.calendar.ui.screens.permissions.PermissionsScreen
import ir.parvaz.calendar.ui.screens.settings.SettingsScreen
import ir.parvaz.calendar.ui.theme.ParvazTheme
import kotlinx.coroutines.launch

@Composable
fun ParvazApp() {
    ParvazTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            val context = LocalContext.current
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            var screen by remember { mutableStateOf(0) }

            LaunchedEffect(Unit) {
                DateNotificationHelper.refresh(context)
                AdhanScheduler.scheduleAll(context)
            }

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ParvazNavigationDrawer(
                        drawerState = drawerState,
                        currentScreen = screen,
                        onNavigate = {
                            screen = it
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            ) {
                Scaffold(
                    bottomBar = {
                        if (screen in 0..3) {
                            ParvazBottomNavBar(
                                currentScreen = screen,
                                onNavigate = { screen = it }
                            )
                        }
                    }
                ) { paddingValues ->
                    when (screen) {
                        0 -> HomeScreen(
                            modifier = Modifier.padding(paddingValues),
                            onMenuClick = { scope.launch { drawerState.open() } }
                        )
                        1 -> CalendarScreen(onBack = { screen = 0 })
                        2 -> HomeScreen(modifier = Modifier.padding(paddingValues))
                        3 -> SettingsScreen(
                            onBack = { screen = 0 },
                            onOpenPermissions = { screen = 4 }
                        )
                        4 -> PermissionsScreen(onBack = { screen = 3 })
                    }
                }
            }
        }
    }
}
