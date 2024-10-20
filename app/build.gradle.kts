plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // Google Services plugin for Firebase
    alias(libs.plugins.kotlin.android) // Kotlin Android plugin
}

android {
    namespace = "com.example.workshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.workshop"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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
        compose = true // Enable Jetpack Compose
    }
    viewBinding { enable = true }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Specify Compose compiler version
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Exclude certain files to prevent duplicates
        }
    }
}

dependencies {
    implementation(libs.appcompat) // AndroidX AppCompat
    implementation(libs.material) // Material Components
    implementation(libs.activity) // AndroidX Activity
    implementation(libs.constraintlayout) // ConstraintLayout
    implementation(libs.preference) // AndroidX Preferences
    implementation(libs.lifecycle.runtime.ktx) // Lifecycle KTX
    implementation(libs.activity.compose) // Compose Activity
    implementation(platform(libs.compose.bom)) // Compose BOM
    implementation(libs.ui) // Jetpack Compose UI
    implementation(libs.ui.graphics) // Jetpack Compose UI Graphics
    implementation(libs.ui.tooling.preview) // Compose Preview Tooling
    implementation(libs.material3) // Material 3 components
    implementation(libs.firebase.auth) // Firebase Authentication
    implementation(libs.google.services) // Google Services
    implementation(libs.play.services.safetynet) // Play Services SafetyNet
    implementation (libs.google.recaptcha)

    // Firebase App Check with Play Integrity
    implementation(libs.firebase.appcheck.playintegrity)

    // Glide for image loading
    implementation(libs.glide)
    testImplementation(libs.junit.junit)
    annotationProcessor(libs.compiler)

    // Firebase Firestore for database
    implementation(libs.firebase.firestore)

    // Kotlin Coroutines for Android (works for Java too)
    implementation(libs.kotlinx.coroutines.android)
    implementation (libs.play.services.base)

    // Firebase Storage
    implementation(libs.firebase.storage)
}
