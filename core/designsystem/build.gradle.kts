plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.presentation)
                implementation(projects.core.domain)

                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                api(libs.kotlinx.datetime)
                implementation(libs.kmp.date.time.picker)
            }
        }
        androidMain {
            dependencies {

            }
        }
        iosMain {
            dependencies {

            }
        }
    }
}