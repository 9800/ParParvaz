package ir.parvaz.calendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val DrawerBlue = Color(0xFF0E6BA8)

@Composable
fun ParvazNavigationDrawer(
    drawerState: DrawerState,
    currentScreen: Int,
    onNavigate: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(DrawerBlue)
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "پر پرواز",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "تقویم و اوقات شرعی",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
        }

        HorizontalDivider()

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            DrawerItem(Icons.Default.Home, "خانه", 0, currentScreen, onNavigate)
            DrawerItem(Icons.Default.DateRange, "تقویم", 1, currentScreen, onNavigate)
            DrawerItem(Icons.Default.Favorite, "اوقات شرعی", 2, currentScreen, onNavigate)
            DrawerItem(Icons.Default.Settings, "تنظیمات", 3, currentScreen, onNavigate)
            DrawerItem(Icons.Default.Info, "درباره ما", 4, currentScreen, onNavigate)
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "نسخه ۰.۱.۰",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    screenId: Int,
    currentScreen: Int,
    onNavigate: (Int) -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label, fontSize = 15.sp) },
        selected = currentScreen == screenId,
        onClick = { onNavigate(screenId) },
        modifier = Modifier.padding(vertical = 4.dp)
    )
}
