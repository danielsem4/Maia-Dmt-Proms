plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.buildkonfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)

                implementation(libs.kotlin.stdlib)
                implementation(libs.bundles.ktor.common)
                implementation(libs.touchlab.kermit)


                implementation(libs.koin.core)

            }
        }
        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)

            }
        }
        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)

            }
        }
    }

}