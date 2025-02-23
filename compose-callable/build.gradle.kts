import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.multiplatform)
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
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(libs.kotlinx.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(compose.foundation)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.androidx.compose.ui.test.junit4)
                implementation(libs.androidx.compose.ui.test.manifest)
                implementation(libs.junit)
                implementation(libs.robolectric)
            }
        }
        val desktopTest by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "com.moriatsushi.copmose.callable"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    testOptions.unitTests {
        isIncludeAndroidResources = true
    }
}
