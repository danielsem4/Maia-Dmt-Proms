plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(projects.core.domain)
                implementation(projects.core.designsystem)
                implementation(projects.core.presentation)

                implementation(projects.cogTest.orientation.domain)

                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.compottie)
                implementation(libs.compottie.resources)

                implementation(libs.bundles.koin.common)
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