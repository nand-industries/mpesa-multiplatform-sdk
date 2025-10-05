@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    id("com.vanniktech.maven.publish") version "0.34.0"
}

kotlin {
    jvmToolchain(21)
    androidTarget {
        publishLibraryVariants("release")
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "MpesaMultiplatformSdk"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.bundles.ktor)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.compottie)
            implementation(libs.compottie.dot)
            implementation(libs.compottie.network)
            implementation(libs.compottie.resources)
            implementation(libs.navigation.compose)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.material.icons.extended)
            implementation(libs.ui.backhandler)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.android)
        }

        iosMain.dependencies {
            implementation(libs.cryptography.provider.apple)
            implementation(libs.ktor.ios)
        }
    }
}

android {
    namespace = "io.github.nandindustries"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.nandindustries.sdk.resources"
    generateResClass = always
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(
        group.toString(),
        "mpesa-multiplatform-sdk",
        version.toString(),
    )

    val repositorySlug = System.getenv("GITHUB_REPOSITORY") ?: "nand-industries/mpesa-multiplatform-sdk"

    pom {
        name = "M-Pesa Multiplatform SDK"
        description = "Compose Multiplatform SDK for interacting with Vodacom M-Pesa APIs."
        inceptionYear = "2025"
        url = "https://github.com/$repositorySlug"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "callebdev"
                name = "Calebe Joel Miquissene"
                url = "https://calebemiquissene.com"
            }
        }
        scm {
            url = "https://github.com/$repositorySlug"
            connection = "scm:git:git://https://github.com/$repositorySlug.git"
            developerConnection = "scm:git:ssh://git@github.com/$repositorySlug.git"
        }
    }
}
