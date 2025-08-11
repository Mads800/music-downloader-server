// ملف build.gradle.kts على مستوى المشروع (Project-level)

plugins {
    alias(libs.plugins.android.application) apply false
    // ضع هنا أي إضافات (Plugins) خاصة بالمشروع إذا كانت موجودة
    // مثال: kotlin("android") أو غيره حسب ما تحتاجه
}

tasks.register<Delete>(name = "clean") {
    delete(rootProject.layout.buildDirectory)
}

