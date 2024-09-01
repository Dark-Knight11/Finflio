@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
    id("kotlin-parcelize")
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

android {
    namespace = "com.finflio"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.finflio"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "CLOUD_NAME", "\"${project.property("CLOUD_NAME")}\"")
            buildConfigField(
                "String",
                "CLOUDINARY_API_KEY",
                "\"${project.property("CLOUDINARY_API_KEY")}\""
            )
            buildConfigField(
                "String",
                "CLOUDINARY_API_SECRET",
                "\"${project.property("CLOUDINARY_API_SECRET")}\""
            )
            buildConfigField(
                "String",
                "CLOUDINARY_URL",
                "\"${project.property("CLOUDINARY_URL")}\""
            )
            buildConfigField(
                "String",
                "API_URL",
                "\"${project.property("API_URL")}\""
            )
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            buildConfigField("String", "CLOUD_NAME", "\"${project.property("CLOUD_NAME")}\"")
            buildConfigField(
                "String",
                "CLOUDINARY_API_KEY",
                "\"${project.property("CLOUDINARY_API_KEY")}\""
            )
            buildConfigField(
                "String",
                "CLOUDINARY_API_SECRET",
                "\"${project.property("CLOUDINARY_API_SECRET")}\""
            )
            buildConfigField(
                "String",
                "CLOUDINARY_URL",
                "\"${project.property("CLOUDINARY_URL")}\""
            )
            buildConfigField(
                "String",
                "API_URL",
                "\"${project.property("API_URL")}\""
            )
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

tasks.getByPath("preBuild").dependsOn("ktlintFormat")

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    debug.set(true)
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(true)
    disabledRules.set(
        setOf(
            "final-newline",
            "no-wildcard-imports",
            "max-line-length",
            "no-unused-imports",
            "package-name"
        )
    )
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.SARIF)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

dependencies {
    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // don't remove the specified version else android preview won't work
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.4")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.activity:activity:1.8.2")
    implementation("androidx.compose.ui:ui-util:1.5.4")

    // Lifecycle
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Cloudy
    implementation("com.github.skydoves:cloudy:0.1.2")

    // Accompanist
    val accompanistVersion = "0.30.1"
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")

    // Coil
    implementation("io.coil-kt:coil-compose:2.3.0")

    // Compose Destinations
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.9.62")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.9.62")

    // dagger hilt
    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-compiler:2.48")
    // hilt navigation compose
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-paging:$roomVersion")

    // material dialogs
    implementation("io.github.vanpra.compose-material-dialogs:core:0.9.0")
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    // Vico Charts
    implementation("com.patrykandpatrick.vico:compose-m2:1.6.4")

    // Integration
    implementation("com.cloudinary:cloudinary-android:2.2.0")
    implementation(kotlin("reflect"))

    // For zoomable images
    implementation("de.mr-pine.utils:zoomables:1.1.2")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("io.github.osipxd:security-crypto-datastore-preferences:1.0.0-beta01")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // Paging 3.0
    implementation("androidx.paging:paging-compose:3.2.1")
}
