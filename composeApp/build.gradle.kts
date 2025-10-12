plugins {
    alias(libs.plugins.convention.cmp.application)
    alias(libs.plugins.compose.hot.reload)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.core.splashscreen)

            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(projects.core.presentation)
            implementation(projects.core.data)
            implementation(projects.core.domain)
            implementation(projects.core.designsystem)

            implementation(projects.feature.auth.presentation)
            implementation(projects.feature.auth.domain)

            implementation(projects.feature.home.presentation)
            implementation(projects.feature.home.data)
            implementation(projects.feature.home.domain)

            implementation(projects.feature.medication.presentation)
            implementation(projects.feature.medication.domain)
            implementation(projects.feature.medication.data)

            implementation(projects.feature.graphs.presentation)
            implementation(projects.feature.graphs.domain)

            implementation(projects.feature.evaluation.presentation)
            implementation(projects.feature.evaluation.domain)
            implementation(projects.feature.evaluation.data)

            implementation(projects.feature.hitber.presentation)
            implementation(projects.feature.hitber.domain)
            implementation(projects.feature.hitber.data)

            implementation(projects.feature.cdt.presentation)
            implementation(projects.feature.cdt.domain)
            implementation(projects.feature.cdt.data)

            implementation(projects.feature.pass.presentation)
            implementation(projects.feature.pass.domain)
            implementation(projects.feature.pass.data)

            implementation(projects.feature.orientation.presentation)
            implementation(projects.feature.orientation.domain)
            implementation(projects.feature.orientation.data)

            implementation(projects.feature.memory.presentation)
            implementation(projects.feature.memory.domain)
            implementation(projects.feature.memory.data)

            implementation(libs.jetbrains.compose.navigation)
            implementation(libs.bundles.koin.common)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jetbrains.compose.viewmodel)
            implementation(libs.jetbrains.lifecycle.compose)


        }
    }
}
