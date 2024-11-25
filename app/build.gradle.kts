import com.android.build.api.variant.BuildConfigField

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.hilt.android)
    kotlin("plugin.serialization") version "1.9.23"
}

android {
    namespace = "ua.rikutou.studio"
    compileSdk = 34

    defaultConfig {
        applicationId = "ua.rikutou.studio"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    kapt {
        correctErrorTypes = true
        useBuildCache = true
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
//    buildConfigField("String", 'CFG_SERVER_URL', "\"http:192.168.50.79:8080\"")
//    buildConfigField("String", "CFG_SERVER_URL", "\"https://reachtags.app/api/\"")
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }
    kotlin {
        jvmToolchain(17)
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
androidComponents {
    onVariants {
        it.buildConfigFields.put(
            "CFG_SERVER_URL",
//            BuildConfigField(type  = "String",value = "\"http:192.168.50.79:8080\"", comment = "server config url")
            BuildConfigField(type  = "String",value = "\"http:192.168.50.124:8080\"", comment = "server config url")
        )
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.compose.material)

//    AsyncImage
    implementation(libs.coil.compose)
    implementation(libs.coil.network)

//    Network
    implementation(libs.retrofit)
    implementation(libs.retrofitConvertorGSon)
    implementation(libs.chucker)
//    implementation(libs.chuckerNoOp)

    // DI
    implementation(libs.android.hilt)
    kapt(libs.android.hilt.compiler)
//    kapt(libs.androidx.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    //Db
    implementation(libs.roomCommon)
    implementation(libs.roomRuntime)
    implementation(libs.roomKtx)
    implementation(libs.roomPaging)
    ksp(libs.roomCompiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}