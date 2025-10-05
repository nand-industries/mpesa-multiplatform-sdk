import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "io.github.nandindustries.demo"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.nandindustries.demo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        val productionApiKey: String =
            gradleLocalProperties(rootDir, providers).getProperty("MPESA_PRODUCTION_API_KEY")

        val developmentApiKey: String =
            gradleLocalProperties(rootDir, providers).getProperty("MPESA_DEVELOPMENT_API_KEY")

        val publicKey: String =
            gradleLocalProperties(rootDir, providers).getProperty("MPESA_PUBLIC_KEY")

        buildConfigField(
            "String",
            "MPESA_PRODUCTION_API_KEY",
            "\"$productionApiKey\""
        )

        buildConfigField(
            "String",
            "MPESA_DEVELOPMENT_API_KEY",
            "\"$developmentApiKey\""
        )

        buildConfigField(
            "String",
            "MPESA_PUBLIC_KEY",
            "\"$publicKey\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))

    // Modules
    implementation(project(":sdk"))

    // Test dependencies
    testImplementation(libs.junit)

    // Android test dependencies
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Debug dependencies
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)
}
