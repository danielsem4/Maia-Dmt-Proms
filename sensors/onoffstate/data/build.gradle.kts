plugins {
    alias(libs.plugins.convention.cmp.feature)
    alias(libs.plugins.convention.room)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(projects.core.domain)
                implementation(projects.core.data)

                implementation(projects.sensors.onoffstate.domain)
                implementation(projects.sensors.core.domain)

                implementation(libs.bundles.ktor.common)
                implementation(libs.koin.core)
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
