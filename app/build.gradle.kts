plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val devilReleaseKeystorePath =
    System.getenv("DEVIL_RELEASE_KEYSTORE_PATH")
        ?.trim()
        ?.takeIf { it.isNotEmpty() }

val devilReleaseKeystorePassword =
    System.getenv("DEVIL_RELEASE_KEYSTORE_PASSWORD")
        ?.takeIf { it.isNotEmpty() }

val devilReleaseKeyAlias =
    System.getenv("DEVIL_RELEASE_KEY_ALIAS")
        ?.trim()
        ?.takeIf { it.isNotEmpty() }

val devilReleaseKeyPassword =
    System.getenv("DEVIL_RELEASE_KEY_PASSWORD")
        ?.takeIf { it.isNotEmpty() }

val devilReleaseSigningValues =
    listOf(
        devilReleaseKeystorePath,
        devilReleaseKeystorePassword,
        devilReleaseKeyAlias,
        devilReleaseKeyPassword,
    )

val devilReleaseSigningConfigured =
    devilReleaseSigningValues.all { it != null }

android {
    namespace = "com.devil.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.devil.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 3
        versionName = "1.0.0"
    }

    signingConfigs {
        if (devilReleaseSigningConfigured) {
            create("release") {
                storeFile =
                    file(
                        requireNotNull(
                            devilReleaseKeystorePath,
                        ),
                    )
                storePassword =
                    requireNotNull(
                        devilReleaseKeystorePassword,
                    )
                keyAlias =
                    requireNotNull(
                        devilReleaseKeyAlias,
                    )
                keyPassword =
                    requireNotNull(
                        devilReleaseKeyPassword,
                    )
            }
        }
    }

    buildTypes {
        getByName("release") {
            if (devilReleaseSigningConfigured) {
                signingConfig =
                    signingConfigs.getByName("release")
            }
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

gradle.taskGraph.whenReady {
    val releaseBuildRequested =
        allTasks.any { task ->
            task.project == project &&
                (
                    task.name.equals(
                        "assembleRelease",
                        ignoreCase = true,
                    ) ||
                        task.name.equals(
                            "bundleRelease",
                            ignoreCase = true,
                        ) ||
                        task.name.startsWith(
                            "packageRelease",
                            ignoreCase = true,
                        )
                )
        }

    if (releaseBuildRequested) {
        check(devilReleaseSigningConfigured) {
            """
            Devil release signing credentials are unavailable.

            Release builds fail closed.

            Required environment variables:
            DEVIL_RELEASE_KEYSTORE_PATH
            DEVIL_RELEASE_KEYSTORE_PASSWORD
            DEVIL_RELEASE_KEY_ALIAS
            DEVIL_RELEASE_KEY_PASSWORD
            """.trimIndent()
        }

        val keystoreFile =
            file(
                requireNotNull(
                    devilReleaseKeystorePath,
                ),
            )

        check(keystoreFile.isFile) {
            "Devil release keystore does not exist at the configured path."
        }
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
