@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlin.cocoapods)
    id("com.vanniktech.maven.publish") version "0.34.0"
}

kotlin {
    jvmToolchain(ProjectConfiguration.Compiler.jdkVersion)
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
            baseName = ProjectConfiguration.MpesaMultiplatformSdk.baseName
            isStatic = true
        }
    }

    cocoapods {
        version = ProjectConfiguration.MpesaMultiplatformSdk.versionName
        name = ProjectConfiguration.MpesaMultiplatformSdk.Publishing.cocoaPodName
        summary = ProjectConfiguration.MpesaMultiplatformSdk.Publishing.description
        homepage = ProjectConfiguration.MpesaMultiplatformSdk.documentation

        license = ProjectConfiguration.MpesaMultiplatformSdk.Publishing.License.licenseType
        source = "{ :git => 'git@github.com:nand-industries/mpesa-multiplatform-sdk.git', :tag => '$version' }"
        authors = "Calebe Miquissene, Yazalde Filimone"

        ios.deploymentTarget = ProjectConfiguration.MpesaMultiplatformSdk.iOS.deploymentTarget
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
    namespace = ProjectConfiguration.MpesaMultiplatformSdk.namespace
    compileSdk = ProjectConfiguration.MpesaMultiplatformSdk.compileSdk

    defaultConfig {
        minSdk = ProjectConfiguration.MpesaMultiplatformSdk.minSdk
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
    packageOfResClass = ProjectConfiguration.MpesaMultiplatformSdk.packageOfResClass
    generateResClass = always
}

version = ProjectConfiguration.MpesaMultiplatformSdk.versionName
group = ProjectConfiguration.MpesaMultiplatformSdk.Publishing.group

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = group.toString(),
        artifactId = ProjectConfiguration.MpesaMultiplatformSdk.Publishing.artifactId,
        version = version.toString(),
    )

    val repositorySlug = System.getenv("GITHUB_REPOSITORY")
        ?: ProjectConfiguration.MpesaMultiplatformSdk.Publishing.repositorySlug

    pom {
        name = ProjectConfiguration.MpesaMultiplatformSdk.Publishing.name
        description = ProjectConfiguration.MpesaMultiplatformSdk.Publishing.description
        inceptionYear = ProjectConfiguration.MpesaMultiplatformSdk.Publishing.inceptionYear
        url = "https://github.com/$repositorySlug"
        licenses {
            license {
                name = ProjectConfiguration.MpesaMultiplatformSdk.Publishing.License.name
                url = ProjectConfiguration.MpesaMultiplatformSdk.Publishing.License.url
                distribution =
                    ProjectConfiguration.MpesaMultiplatformSdk.Publishing.License.distribution
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
