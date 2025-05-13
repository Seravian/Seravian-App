import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.greenvenom.core_network"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"${localProperties.getProperty("DEV_URL")}\"")
            buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("DEV_KEY")}\"")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"${localProperties.getProperty("PROD_URL")}\"")
            buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("PROD_KEY")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    val ktor = "3.0.3"
    val koin = "4.0.0"

    implementation(project(":tokens:core-tokens"))
    implementation(project(":auth:core-auth"))
    implementation(project(":profile:core-profile"))
    implementation(project(":home:core-home"))
    implementation(project(":chat:core-chat"))
    implementation(project(":onboarding:core-onboarding"))

    implementation(libs.androidx.core.ktx)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    implementation("io.ktor:ktor-client-cio:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-okhttp:$ktor")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("io.ktor:ktor-client-logging:$ktor")
    implementation("io.ktor:ktor-client-auth:$ktor")
    implementation("io.ktor:ktor-client-resources:$ktor")

    implementation(platform("io.insert-koin:koin-bom:$koin"))
    implementation("io.insert-koin:koin-androidx-compose")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}