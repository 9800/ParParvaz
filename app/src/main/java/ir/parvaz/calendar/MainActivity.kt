package ir.parvaz.calendar

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ir.parvaz.calendar.core.icon.IconManager
import ir.parvaz.calendar.ui.ParvazApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParvazApp()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            try {
                IconManager.update(this)
            } catch (exception: Exception) {
            }
        }, 5000)
    }
}
