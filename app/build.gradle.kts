@file:Suppress("UnstableApiUsage")
plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.finflio"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.finflio"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_15
        targetCompatibility = JavaVersion.VERSION_15
    }
    kotlinOptions {
        jvmTarget = "15"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packagingOptions {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

dependencies {
    // Compose BOM
    val composeBom = platform ("androidx.compose:compose-bom:2023.03.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.activity:activity-compose:1.7.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.1")

    // Lifecycle
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Cloudy
    implementation("com.github.skydoves:cloudy:0.1.2")

    // Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.28.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Compose Destinations
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.8.38-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.8.38-beta")

    // dagger hilt
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-compiler:2.45")
    // hilt navigation compose
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Room
    val roomVersion = "2.5.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    //material dialogs
    implementation("io.github.vanpra.compose-material-dialogs:core:0.9.0")
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    // For `compose`. Creates a `ChartStyle` based on an M2 Material Theme.
    implementation("com.patrykandpatrick.vico:compose-m2:1.6.4")

}