import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.seravian.seravianapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.seravian.seravianapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"${localProperties.getProperty("DEVELOPMENT_URL")}\"")
            buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("API_KEY")}\"")
        }
        release {
            applicationIdSuffix = ".release"
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"${localProperties.getProperty("PRODUCTION_URL")}\"")
            buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("API_KEY")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xwhen-guards")
        }
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.networking)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.image.loading)
    implementation(libs.bundles.dependency.injection)
    implementation(libs.bundles.data.persistence)
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(libs.androidx.compose.navigation)
    implementation(libs.kotlinx.serialization.json)

    //splash api
    implementation(libs.androidx.core.splashscreen)
    implementation (libs.material.v1120) // Use the latest version


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.bundles.compose.debug)
}