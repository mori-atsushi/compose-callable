import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.spotless) apply false
}

allprojects {
    apply<SpotlessPlugin>()

    extensions.configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt", "**/*.kts")
            val ktlintVersion = libs.versions.ktlint.get()
            targetExclude("**/build/**/*.kt")
            ktlint(ktlintVersion)
        }
    }
}
