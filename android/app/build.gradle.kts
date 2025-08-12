// android/app/build.gradle.kts
import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

val keystorePropertiesFile = rootProject.file("key.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        load(FileInputStream(keystorePropertiesFile))
    }
}

android {
    // معرف الحزمة النهائي (غيّره لو تريد uid آخر)
    namespace = "com.jeeey.shopin"

    // استخدم compileSdk ثابت — تأكد أنك ثبتت منصة android-35
    compileSdk = 35

    // استخدم NDK المطلوب للمكتبة webview
    ndkVersion = "27.0.12077973"

    defaultConfig {
        applicationId = "com.jeeey.shopin"

        // دعم Android 5.0 وأحدث
        minSdk = 21
        targetSdk = 35

        // رقم الإصدار واسم النسخة (ضبطتها حسب ما طلبت)
        versionCode = 3034
        versionName = "1.0.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        // Signing config للـ release — سيقرأ القيم من android/key.properties
        create("release") {
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            storeFile = file(keystoreProperties.getProperty("storeFile") ?: "key.jks")
            storePassword = keystoreProperties.getProperty("storePassword")
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            // لإصدار الريليز حالياً نترك minify/shrink مغلقين لتجنب مشاكل البناء.
            // لاحقاً إن أردت تقليل حجم الـ AAB فعّل minify و shrink مع إضافة proguard-rules.pro
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

flutter {
    source = "../.."
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}