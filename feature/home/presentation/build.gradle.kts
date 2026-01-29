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
                implementation(projects.feature.home.domain)

                implementation(projects.sensors.core.domain)
//                implementation(projects.sensors.core.data)
                implementation(projects.sensors.core.presentation)

                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

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