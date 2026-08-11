package ir.parvaz.calendar.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

private val BarBlue = Color(0xFF0E6BA8)

@Composable
fun ParvazBottomNavBar(
    currentScreen: Int,
    onNavigate: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = BarBlue
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "خانه") },
            label = { Text("خانه", fontSize = 11.sp) },
            selected = currentScreen == 0,
            onClick = { onNavigate(0) },
            selectedIcon = { Icon(Icons.Default.Home, contentDescription = "خانه") }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "تقویم") },
            label = { Text("تقویم", fontSize = 11.sp) },
            selected = currentScreen == 1,
            onClick = { onNavigate(1) },
            selectedIcon = { Icon(Icons.Default.DateRange, contentDescription = "تقویم") }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "اوقات شرعی") },
            label = { Text("اوقات شرعی", fontSize = 11.sp) },
            selected = currentScreen == 2,
            onClick = { onNavigate(2) },
            selectedIcon = { Icon(Icons.Default.Favorite, contentDescription = "اوقات شرعی") }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "تنظیمات") },
            label = { Text("تنظیمات", fontSize = 11.sp) },
            selected = currentScreen == 3,
            onClick = { onNavigate(3) },
            selectedIcon = { Icon(Icons.Default.Settings, contentDescription = "تنظیمات") }
        )
    }
}
