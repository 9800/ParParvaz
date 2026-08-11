package ir.parvaz.calendar.core.city

data class City(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double
)

object Cities {

    val all = listOf(
        City("tehran", "تهران", 35.6892, 51.3890),
        City("mashhad", "مشهد", 36.2605, 59.6168),
        City("isfahan", "اصفهان", 32.6546, 51.6680),
        City("shiraz", "شیراز", 29.5918, 52.5837),
        City("tabriz", "تبریز", 38.0800, 46.2919),
        City("karaj", "کرج", 35.8327, 50.9915),
        City("qom", "قم", 34.6416, 50.8746),
        City("ahvaz", "اهواز", 31.3183, 48.6706),
        City("kermanshah", "کرمانشاه", 34.3142, 47.0650),
        City("urmia", "ارومیه", 37.5527, 45.0761),
        City("rasht", "رشت", 37.2809, 49.5832),
        City("zahedan", "زاهدان", 29.4963, 60.8629),
        City("hamadan", "همدان", 34.7981, 48.5146),
        City("yazd", "یزد", 31.8974, 54.3569),
        City("ardabil", "اردبیل", 38.2498, 48.2933),
        City("bandarabbas", "بندرعباس", 27.1832, 56.2666),
        City("arak", "اراک", 34.0917, 49.6892),
        City("zanjan", "زنجان", 36.6686, 48.4963),
        City("sanandaj", "سنندج", 35.3150, 46.9988),
        City("qazvin", "قزوین", 36.2688, 50.0041),
        City("khorramabad", "خرم‌آباد", 33.4878, 48.3558),
        City("gorgan", "گرگان", 36.8456, 54.4393),
        City("sari", "ساری", 36.5633, 53.0601),
        City("semnan", "سمنان", 35.5729, 53.3971),
        City("birjand", "بیرجند", 32.8663, 59.2211),
        City("bojnurd", "بجنورد", 37.4747, 57.3256),
        City("bushehr", "بوشهر", 28.9684, 50.8385),
        City("ilam", "ایلام", 33.6374, 46.4227),
        City("shahrekord", "شهرکرد", 32.3256, 50.8644),
        City("yasuj", "یاسوج", 30.6682, 51.5876),
        City("kerman", "کرمان", 30.2839, 57.0834)
    )

    fun byId(id: String?): City? = all.firstOrNull { it.id == id }
}
