plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    alias(libs.plugins.dagger.hilt.android)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.20"
}

android {
    namespace = "com.velaris.mobile"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.velaris.mobile"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }

    packaging {
        resources {
            excludes += "META-INF/**"
        }
    }
}

dependencies {
    // -----------------------
    // AndroidX Core & Lifecycle
    // -----------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)

    // -----------------------
    // Jetpack Compose
    // -----------------------
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ycharts)

    // Icons
    implementation(libs.androidx.material.icons.extended)

    // Compose Debug / Test tools
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Accompanist
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.permissions)

    // -----------------------
    // Coroutines
    // -----------------------
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // -----------------------
    // Dependency Injection (Hilt)
    // -----------------------
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // -----------------------
    // Networking
    // -----------------------
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    // -----------------------
    // Communication / Nearby
    // -----------------------
    implementation(libs.play.services.nearby)

    // -----------------------
    // Storage
    // -----------------------
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // -----------------------
    // Animation / UI
    // -----------------------
    implementation(libs.android.lottie.compose)
    implementation(libs.coil.compose) // Image loading

    // -----------------------
    // Velaris API client
    // -----------------------
    implementation(libs.velaris.api.client)

    // -----------------------
    // Testing
    // -----------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}