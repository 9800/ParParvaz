# پر پرواز

اپ اندرویدی پر پرواز.

این فاز شامل:
- ساختار اولیه Android
- Jetpack Compose
- نمایش تاریخ شمسی، میلادی و قمری
- GitHub Actions برای بیلد APK

## کامپایل با GitHub Actions

بعد از push شدن کد به GitHub، workflow زیر به‌صورت خودکار APK debug می‌سازد:

.github/workflows/android-ci.yml

خروجی APK در بخش Actions > Artifacts قابل دانلود است.
