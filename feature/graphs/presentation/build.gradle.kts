plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(libs.kotlin.stdlib)
                implementation(projects.feature.graphs.domain)
                implementation(projects.core.domain)
                implementation(projects.core.designsystem)
                implementation(projects.core.presentation)

                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.bundles.koin.common)
                implementation(libs.bundles.visco.common)
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