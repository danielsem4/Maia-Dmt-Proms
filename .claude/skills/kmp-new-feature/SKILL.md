---
name: kmp-new-feature
description: Adding a complete new feature module with domain/data/presentation layers, DI, and navigation to the DMTProms KMP codebase
---

# Adding a New Feature Module to DMTProms

Use this skill when creating a new feature module with domain, data, and presentation layers.

## Step 1: Create Directory Structure

```
feature/<name>/
  domain/
    src/commonMain/kotlin/maia/dmt/<name>/domain/
      models/          # Domain model data classes
      <name>/          # Service interface(s)
      usecase/         # Use case classes (optional)
    build.gradle.kts
  data/
    src/commonMain/kotlin/maia/dmt/<name>/data/
      dto/             # @Serializable DTOs
      mapper/          # toDomain()/toSerial() extension functions
      <name>/          # Ktor service implementation(s)
      di/              # Koin data module
    build.gradle.kts
  presentation/
    src/commonMain/kotlin/maia/dmt/<name>/presentation/
      <screenName>/    # One directory per screen (Screen, ViewModel, State, Action, Event)
      components/      # Feature-specific UI components
      navigation/      # GraphRoutes + Graph builder
      di/              # Koin presentation module
    src/commonMain/composeResources/
      values/
        strings.xml    # Feature string resources
    build.gradle.kts
```

## Step 2: Build Files

**Domain** (`feature/<name>/domain/build.gradle.kts`) - uses `convention.cmp.library`:
```kotlin
plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.domain)
            }
        }
        androidMain { dependencies { } }
        iosMain { dependencies { } }
    }
}
```

**Data** (`feature/<name>/data/build.gradle.kts`) - uses `convention.cmp.feature`:
```kotlin
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
                implementation(projects.feature.<name>.domain)
                implementation(libs.bundles.ktor.common)
                implementation(libs.koin.core)
            }
        }
        androidMain { dependencies { } }
        iosMain { dependencies { } }
    }
}
```

**Presentation** (`feature/<name>/presentation/build.gradle.kts`) - uses `convention.cmp.feature`:
```kotlin
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
                implementation(projects.feature.<name>.domain)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.bundles.koin.common)
            }
        }
        androidMain { dependencies { } }
        iosMain { dependencies { } }
    }
}
```

## Step 3: Register in settings.gradle.kts

Add these lines to `/Users/klutz/Desktop/promsMultiplatform/settings.gradle.kts` alongside other feature includes:
```kotlin
include(":feature:<name>:domain")
include(":feature:<name>:data")
include(":feature:<name>:presentation")
```

## Step 4: Domain Layer

### Service Interface
```kotlin
package maia.dmt.<name>.domain.<name>

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result

interface <Name>Service {
    suspend fun getItems(clinicId: String, patientId: String): Result<List<ItemModel>, DataError.Remote>
    suspend fun submitItem(body: SubmitRequest): EmptyResult<DataError.Remote>
}
```

### Domain Model
```kotlin
package maia.dmt.<name>.domain.models

data class ItemModel(
    val id: String = "",
    val name: String = "",
    val value: Int = 0
)
```

### Use Case (optional, uses invoke operator or execute method)
```kotlin
package maia.dmt.<name>.domain.usecase

import maia.dmt.<name>.domain.<name>.<Name>Service

class Get<Name>UseCase(
    private val service: <Name>Service
) {
    suspend operator fun invoke(clinicId: String, patientId: String) =
        service.getItems(clinicId, patientId)
}
```

## Step 5: Data Layer

### DTO
```kotlin
package maia.dmt.<name>.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: String = "",
    val name: String = "",
    val value: Int = 0
)
```

### Mapper
```kotlin
package maia.dmt.<name>.data.mapper

import maia.dmt.<name>.data.dto.ItemDto
import maia.dmt.<name>.domain.models.ItemModel

fun ItemDto.toDomain(): ItemModel {
    return ItemModel(
        id = id,
        name = name,
        value = value
    )
}

fun ItemModel.toSerial(): ItemDto {
    return ItemDto(
        id = id,
        name = name,
        value = value
    )
}
```

### Ktor Service Implementation
```kotlin
package maia.dmt.<name>.data.<name>

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.data.networking.post
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.<name>.data.dto.ItemDto
import maia.dmt.<name>.data.mapper.toDomain
import maia.dmt.<name>.data.mapper.toSerial
import maia.dmt.<name>.domain.<name>.<Name>Service
import maia.dmt.<name>.domain.models.ItemModel

class Ktor<Name>Service(
    private val httpClient: HttpClient,
) : <Name>Service {

    override suspend fun getItems(
        clinicId: String,
        patientId: String
    ): Result<List<ItemModel>, DataError.Remote> {
        return httpClient.get<List<ItemDto>>(
            route = "<api_route>/",
            queryParams = mapOf(
                "clinic_id" to clinicId,
                "patient_id" to patientId
            )
        ).map { it.map { it.toDomain() } }
    }

    override suspend fun submitItem(body: SubmitRequest): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "<api_route>/",
            body = body.toSerial()
        )
    }
}
```

### Data DI Module
```kotlin
package maia.dmt.<name>.data.di

import maia.dmt.<name>.data.<name>.Ktor<Name>Service
import maia.dmt.<name>.domain.<name>.<Name>Service
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val <name>DataModule = module {
    singleOf(::Ktor<Name>Service) bind <Name>Service::class
}
```

## Step 6: Presentation Layer

Create the screen files following the `kmp-new-screen` skill pattern, then:

### Presentation DI Module
```kotlin
package maia.dmt.<name>.presentation.di

import maia.dmt.<name>.presentation.<screenName>.<ScreenName>ViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val <name>PresentationModule = module {
    viewModelOf(::<ScreenName>ViewModel)
}
```

### Navigation - GraphRoutes
```kotlin
package maia.dmt.<name>.presentation.navigation

import kotlinx.serialization.Serializable

interface <Name>GraphRoutes {
    @Serializable
    data object Graph : <Name>GraphRoutes

    @Serializable
    data object <ScreenName> : <Name>GraphRoutes
}
```

### Navigation - Graph Builder
```kotlin
package maia.dmt.<name>.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.<name>.presentation.<screenName>.<ScreenName>Root

fun NavGraphBuilder.<name>Graph(
    navController: NavController,
) {
    navigation<<Name>GraphRoutes.Graph>(
        startDestination = <Name>GraphRoutes.<ScreenName>
    ) {
        composable<<Name>GraphRoutes.<ScreenName>> {
            <ScreenName>Root(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
```

## Step 7: Register DI Modules

In `/Users/klutz/Desktop/promsMultiplatform/composeApp/src/commonMain/kotlin/maia/dmt/proms/di/initKoin.kt`:

1. Add imports:
```kotlin
import maia.dmt.<name>.data.di.<name>DataModule
import maia.dmt.<name>.presentation.di.<name>PresentationModule
```

2. Add to the `modules()` block:
```kotlin
<name>DataModule,
<name>PresentationModule,
```

## Step 8: Register Navigation

In `/Users/klutz/Desktop/promsMultiplatform/composeApp/src/commonMain/kotlin/maia/dmt/proms/navigation/NavigationRoot.kt`:

1. Add imports:
```kotlin
import maia.dmt.<name>.presentation.navigation.<Name>GraphRoutes
import maia.dmt.<name>.presentation.navigation.<name>Graph
```

2. Add the graph inside `NavHost`:
```kotlin
<name>Graph(
    navController = navController
)
```

3. If navigated from home, add the route mapping in the `onModuleClicked` when block:
```kotlin
"<module name>" -> navController.navigate(<Name>GraphRoutes.Graph)
```

## Key Conventions

- **Package naming**: `maia.dmt.<featurename>.<layer>` (e.g., `maia.dmt.medication.data`)
- **DI naming**: `<name>DataModule`, `<name>PresentationModule` (camelCase val names)
- **Service naming**: `<Name>Service` (domain interface), `Ktor<Name>Service` (data implementation)
- **Use `singleOf` + `bind`** for service implementations in data modules
- **Use `viewModelOf`** for ViewModels in presentation modules
- **Use `factoryOf`** for use cases (if needed, but use cases are not always present)
- **Domain layer has NO framework dependencies** - only Kotlin stdlib and core domain
- **Result type**: Always return `Result<T, DataError.Remote>` or `EmptyResult<DataError.Remote>` from service methods
- **Mappers**: Extension functions `DtoType.toDomain()` and `DomainType.toSerial()`
