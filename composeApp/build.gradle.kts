plugins {
    alias(libs.plugins.convention.cmp.application)
    alias(libs.plugins.compose.hot.reload)
    alias(libs.plugins.google.services)
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

            implementation(projects.feature.evaluation.presentation)
            implementation(projects.feature.evaluation.domain)
            implementation(projects.feature.evaluation.data)

            implementation(projects.feature.activities.presentation)
            implementation(projects.feature.activities.domain)
            implementation(projects.feature.activities.data)

            implementation(projects.feature.statistics.presentation)
            implementation(projects.feature.statistics.domain)
            implementation(projects.feature.statistics.data)

            implementation(projects.cogTest.market.presentation)
            implementation(projects.cogTest.market.domain)
            implementation(projects.cogTest.market.data)

            implementation(projects.cogTest.cdt.presentation)
            implementation(projects.cogTest.cdt.domain)
            implementation(projects.cogTest.cdt.data)

            implementation(projects.feature.settings.presentation)

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
