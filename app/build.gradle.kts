plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services) // Already included
    id("io.realm.kotlin") // Apply Realm plugin here
}

android {
    namespace = "com.example.expensetracker"
    compileSdk = 34

    buildFeatures {
        compose = false  // Disable Jetpack Compose if not needed
        viewBinding = true  // Enable ViewBinding
    }

    defaultConfig {
        applicationId = "com.example.expensetracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true  // Enable multidex
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.room.common)
    implementation(libs.identity.credential)

    // Add Multidex support
    implementation("androidx.multidex:multidex:2.0.1")

    implementation ("com.github.AnyChart:AnyChart-Android:1.1.5")

    // Add Realm Database dependencies
    implementation("io.realm.kotlin:library-base:1.11.0")
    implementation(libs.firebase.vertexai)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}