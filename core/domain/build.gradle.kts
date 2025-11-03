plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.bundles.ktor.common)
                implementation(libs.koin.core)

                implementation(libs.multiplatform.settings)
                implementation(libs.multiplatform.settings.coroutines)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
            }
        }
        iosMain {
            dependencies {

            }
        }
    }
}