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
                implementation(projects.feature.home.domain)
                
                implementation(libs.bundles.ktor.common)
                implementation(libs.koin.core)


            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.lifecycle.process)
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.messaging)
                implementation(libs.koin.android)
            }
        }


        iosMain {
            dependencies {

            }
        }
    }

}