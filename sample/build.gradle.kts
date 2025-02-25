import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()
    jvm("desktop")
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    iosX64("uikitX64")
    iosArm64("uikitArm64")
    iosSimulatorArm64("uikitSimArm64")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":compose-callable"))
                implementation(compose.material3)
                implementation(compose.uiTooling)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
            }
        }
    }
}

android {
    namespace = "com.moriatsushi.compose.callable.sample"
    compileSdk = 35

    defaultConfig {
        targetSdk = 35
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
