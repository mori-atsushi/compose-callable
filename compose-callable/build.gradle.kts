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
  js(IR) {
    browser()
  }
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
  }
  macosX64()
  macosArm64()
  iosX64("uikitX64")
  iosArm64("uikitArm64")
  iosSimulatorArm64("uikitSimArm64")

  watchosArm64()
  watchosArm32()
  watchosX64()
  watchosSimulatorArm64()
  tvosArm64()
  tvosX64()
  tvosSimulatorArm64()
  mingwX64()
  linuxArm64()

  sourceSets {
    val commonMain by getting {
    }
    val commonTest by getting {
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
}
