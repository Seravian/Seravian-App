plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.seravian.seravianapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.seravian.seravianapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }
        release {
            applicationIdSuffix = ".release"
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    androidResources {
        generateLocaleConfig = true
    }
    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xwhen-guards")
        }
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(project(":core-ui"))
    implementation(project(":tokens:core-tokens"))
    implementation(project(":tokens:feat-tokens"))
    implementation(project(":network:core-network"))
    implementation(project(":network:feat-network"))
    implementation(project(":local:core-local"))
    implementation(project(":local:feat-local"))
    implementation(project(":navigation:core-navigation"))
    implementation(project(":navigation:feat-navigation"))
    implementation(project(":auth:feat-auth"))
    implementation(project(":onboarding:feat-onboarding"))
    implementation(project(":home:feat-home"))
    implementation(project(":chat:feat-chat"))
    implementation(project(":profile:core-profile"))
    implementation(project(":profile:feat-profile"))

    implementation(project(":doctors:core-doctors"))
    implementation(project(":doctors:feat-doctors"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.appcompat.resources)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.main)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.dependency.injection)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.splashscreen)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}