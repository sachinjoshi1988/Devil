plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.devil.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.devil.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(
            org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17,
        )
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:runtime"))

    val composeBom = platform(
        "androidx.compose:compose-bom:2025.04.01",
    )

    implementation(composeBom)

    implementation(
        "androidx.activity:activity-compose:1.10.1",
    )
    implementation(
        "androidx.core:core-splashscreen:1.2.0",
    )
    implementation(
        "androidx.compose.material3:material3",
    )
    implementation(
        "androidx.compose.ui:ui",
    )

    testImplementation(kotlin("test"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
