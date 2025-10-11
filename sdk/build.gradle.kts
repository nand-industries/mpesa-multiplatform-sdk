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
    jvmToolchain(ProjectConfigurations.Compiler.jdkVersion)
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
            baseName = ProjectConfigurations.MpesaMultiplatformSdk.baseName
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
    namespace = ProjectConfigurations.MpesaMultiplatformSdk.namespace
    compileSdk = ProjectConfigurations.MpesaMultiplatformSdk.compileSdk

    defaultConfig {
        minSdk = ProjectConfigurations.MpesaMultiplatformSdk.minSdk
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
    packageOfResClass = ProjectConfigurations.MpesaMultiplatformSdk.packageOfResClass
    generateResClass = always
}

version = ProjectConfigurations.MpesaMultiplatformSdk.versionName
group = ProjectConfigurations.MpesaMultiplatformSdk.Publishing.group

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = group.toString(),
        artifactId = ProjectConfigurations.MpesaMultiplatformSdk.Publishing.artifactId,
        version = version.toString(),
    )

    val repositorySlug = System.getenv("GITHUB_REPOSITORY")
        ?: ProjectConfigurations.MpesaMultiplatformSdk.Publishing.repositorySlug

    pom {
        name = ProjectConfigurations.MpesaMultiplatformSdk.Publishing.name
        description = ProjectConfigurations.MpesaMultiplatformSdk.Publishing.description
        inceptionYear = ProjectConfigurations.MpesaMultiplatformSdk.Publishing.inceptionYear
        url = "https://github.com/$repositorySlug"
        licenses {
            license {
                name = ProjectConfigurations.MpesaMultiplatformSdk.Publishing.License.name
                url = ProjectConfigurations.MpesaMultiplatformSdk.Publishing.License.url
                distribution =
                    ProjectConfigurations.MpesaMultiplatformSdk.Publishing.License.distribution
            }
        }
        developers {
            developer {
                id = "callebdev"
                name = "Calebe Joel Miquissene"
                url = "https://calebemiquissene.com"
            }
            developer {
                id = "yazaldefilimone"
                name = "Yazalde Filimone"
                url = "https://yazaldefilimone.com"
            }
        }
        scm {
            url = "https://github.com/$repositorySlug"
            connection = "scm:git:git://https://github.com/$repositorySlug.git"
            developerConnection = "scm:git:ssh://git@github.com/$repositorySlug.git"
        }
    }
}
