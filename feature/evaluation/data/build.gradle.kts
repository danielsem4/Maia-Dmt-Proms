plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(projects.core.domain)
                implementation(projects.core.data)
                implementation(projects.feature.evaluation.domain)

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