plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.serialization)
    alias(libs.plugins.secrets)
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zhufucdev.motion_emulator"
        minSdk = 24
        targetSdk = 34
        versionCode = 24
        versionName = "1.2.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    splits {
        abi {
            isEnable = true
            isUniversalApk = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        viewBinding = true
        compose = true
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "server_uri", "\"${project.findProperty("server_uri") ?: "http://localhost:20230"}\"")
        buildConfigField("String", "product", "\"${project.findProperty("product") ?: "MotionEmulator"}\"")
    }
    namespace = "com.zhufucdev.motion_emulator"
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    packaging {
        resources {
            excludes += "META-INF/*"
            excludes += "META-INF/licenses/*"
            excludes += "**/attach_hotspot_windows.dll"
        }
    }
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
        force("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
        force("com.github.Redempt:Crunch:1.0")
    }
}

dependencies {
    // Internal
    implementation(libs.sdk)
    implementation(libs.update)
    // Stub
    implementation("com.zhufucdev.me:stub:1.0.0")
    implementation("com.zhufucdev.me:plugin:1.0.0")
    implementation("com.zhufucdev.me:xposed:1.0.0")
    // Ktor
    implementation(libs.ktor.client.jvm)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.serialization.jvm)
    implementation(libs.ktor.client.contentnegotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.serialization.protobuf)
    implementation(libs.ktor.server.jvm)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.contentnegotiation)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.websockets.jvm)
    implementation(libs.madgag.spongycastle)
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.work.runtime.ktx)
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    // KotlinX
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.serialization.json)
    // Compose
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.viewmodel.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.compose.mdi)
    implementation(libs.androidx.material3.window.size)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.kotlin.reflect)
    implementation(libs.redempt.crunch)
    implementation(libs.google.guava)
    implementation(libs.aventrix.jnanoid)
    implementation(libs.apache.commons.compress)

    // AMap SDK
    // Chart
    implementation(libs.mpandroidchart)

    // Preferences Serialization
    implementation(libs.kprefs) {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
    }

    // AMap SDK
    implementation(libs.amap.map)
    implementation(libs.amap.search)

    // Google Maps SDK
    implementation(libs.google.maps.ktx)
    implementation(libs.google.maps.utils)
    implementation(libs.google.gms.maps)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

