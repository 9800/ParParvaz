package ir.parvaz.calendar.core.icon

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import ir.parvaz.calendar.core.date.CalendarProvider

object IconManager {

    fun update(context: Context) {
        val day = CalendarProvider.today().persian.day
        val pm = context.packageManager
        val pkg = context.packageName

        for (d in 1..31) {
            val alias = ComponentName(pkg, "$pkg.DayAlias$d")
            val state = if (d == day) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }

            try {
                if (pm.getComponentEnabledSetting(alias) != state) {
                    pm.setComponentEnabledSetting(
                        alias,
                        state,
                        PackageManager.DONT_KILL_APP
                    )
                }
            } catch (exception: Exception) {
            }
        }
    }
}
