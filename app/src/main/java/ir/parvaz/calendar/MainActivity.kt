package ir.parvaz.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ir.parvaz.calendar.core.icon.IconManager
import ir.parvaz.calendar.ui.ParvazApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IconManager.update(this)
        setContent {
            ParvazApp()
        }
    }
}
