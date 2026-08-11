package ir.parvaz.calendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.parvaz.calendar.notification.DateNotificationHelper
import ir.parvaz.calendar.ui.screens.home.HomeScreen
import ir.parvaz.calendar.ui.screens.settings.SettingsScreen
import ir.parvaz.calendar.ui.theme.ParvazTheme

private val BarBlue = Color(0xFF0B5B8F)

@Composable
fun ParvazApp() {
    ParvazTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            val context = LocalContext.current
            var showSettings by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                DateNotificationHelper.refresh(context)
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialWhite()
            ) {
                if (showSettings) {
                    SettingsScreen(onBack = { showSettings = false })
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BarBlue)
                                .padding(horizontal = 8.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Text(
                                text = "پر پرواز",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            )

                            TextButton(onClick = { showSettings = true }) {
                                Text("تنظیمات", color = Color.White)
                            }
                        }

                        HomeScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun MaterialWhite(): Color = Color(0xFFFBFDFC)
