package ir.parvaz.calendar.core.events

import ir.parvaz.calendar.core.date.HijriDate
import ir.parvaz.calendar.core.date.PersianDate
import java.time.LocalDate

data class Event(
    val title: String,
    val source: String,
    val holiday: Boolean
)

object EventsRepository {

    private data class E(val m: Int, val d: Int, val t: String, val h: Boolean = false)

    private val shamsi = listOf(
        E(1, 1, "عید نوروز", true),
        E(1, 2, "عید نوروز", true),
        E(1, 3, "عید نوروز", true),
        E(1, 4, "عید نوروز", true),
        E(1, 12, "روز جمهوری اسلامی", true),
        E(1, 13, "روز طبیعت", true),
        E(2, 12, "روز معلم", false),
        E(3, 14, "رحلت امام خمینی", true),
        E(3, 15, "قیام ۱۵ خرداد", true),
        E(8, 13, "روز دانش‌آموز", false),
        E(9, 16, "روز دانشجو", false),
        E(11, 22, "پیروزی انقلاب اسلامی", true),
        E(12, 29, "روز ملی شدن صنعت نفت", true)
    )

    private val qamari = listOf(
        E(1, 1, "سال جدید هجری قمری", false),
        E(1, 9, "تاسوعا", false),
        E(1, 10, "عاشورا", true),
        E(2, 20, "اربعین حسینی", true),
        E(2, 28, "رحلت رسول اکرم و شهادت امام حسن مجتبی", true),
        E(2, 30, "شهادت امام رضا", true),
        E(3, 17, "ولادت رسول اکرم و امام جعفر صادق", true),
        E(5, 5, "ولادت حضرت زینب", false),
        E(5, 13, "شهادت حضرت زهرا (روایت اول)", false),
        E(6, 3, "شهادت حضرت فاطمه زهرا", true),
        E(6, 20, "روز زن و مادر", false),
        E(7, 13, "ولادت امام علی", true),
        E(7, 27, "مبعث رسول اکرم", true),
        E(8, 15, "ولادت امام زمان", true),
        E(9, 1, "آغاز ماه رمضان", false),
        E(9, 19, "ضربت خوردن امام علی", false),
        E(9, 21, "شهادت امام علی", true),
        E(10, 1, "عید سعید فطر", true),
        E(10, 2, "تعطیل عید فطر", true),
        E(10, 25, "شهادت امام جعفر صادق", true),
        E(12, 9, "روز عرفه", false),
        E(12, 10, "عید سعید قربان", true),
        E(12, 18, "عید سعید غدیر خم", true)
    )

    private val miladi = listOf(
        E(1, 1, "سال نو میلادی", false),
        E(3, 8, "روز جهانی زن", false),
        E(4, 7, "روز جهانی سلامت", false),
        E(5, 1, "روز جهانی کارگر", false),
        E(6, 5, "روز جهانی محیط زیست", false),
        E(9, 21, "روز جهانی صلح", false),
        E(10, 16, "روز جهانی غذا", false),
        E(12, 25, "کریسمس", false)
    )

    fun todayEvents(p: PersianDate, h: HijriDate?, g: LocalDate): List<Event> {
        val list = mutableListOf<Event>()

        shamsi.filter { it.m == p.month && it.d == p.day }
            .forEach { list.add(Event(it.t, "شمسی", it.h)) }

        if (h != null) {
            qamari.filter { it.m == h.month && it.d == h.day }
                .forEach { list.add(Event(it.t, "قمری", it.h)) }
        }

        miladi.filter { it.m == g.monthValue && it.d == g.dayOfMonth }
            .forEach { list.add(Event(it.t, "میلادی", it.h)) }

        return list
    }

    fun allEvents(): List<Event> {
        val list = mutableListOf<Event>()
        shamsi.forEach { list.add(Event(it.t, "شمسی", it.h)) }
        qamari.forEach { list.add(Event(it.t, "قمری", it.h)) }
        miladi.forEach { list.add(Event(it.t, "میلادی", it.h)) }
        return list
    }
}
