# Velaris Mobile - Architecture Overview

This document provides an overview of the architecture of the Velaris Mobile app, intended for new developers joining the project.

---

## 1. Architectural Pattern

The app uses **MVVM (Model-View-ViewModel)** combined with **Repository Pattern**:

* **View (UI)** – Composable screens using Jetpack Compose (e.g., `OverviewScreen`, `AssetsScreen`).
* **ViewModel** – Holds UI state via `StateFlow` and orchestrates data fetching.
* **Repository** – Handles data operations (network, local storage) and maps API responses to domain models.
* **Domain/Model** – Core data models (e.g., `AssetItem`, `OverviewStats`).

**Key Libraries/Technologies:**

* Kotlin + Jetpack Compose
* Hilt for dependency injection
* Retrofit + `SafeApiCaller` for network requests
* Kotlin coroutines + StateFlow

---

## 2. Project Folder Structure

```
com.velaris.mobile
├── core
│   ├── datastore      # PreferencesDataStore, keys
│   ├── di             # Hilt modules (Network, DataStore)
│   ├── security       # AuthInterceptor, TokenProvider, TokenAuthenticator
│   ├── serialization  # Custom serializers (BigDecimal, LocalDate, UUID)
│   └── util           # ApiResult, ErrorMapper, SafeApiCaller, helpers
│
├── data
│   └── repository     # AssetRepository, StatsRepository
│
├── domain
│   └── model          # AssetItem, OverviewStats, StatsModels
│
└── ui
    ├── common         # Reusable UI components (SectionCard, CompactTopBar, charts)
    ├── feature        # Screens by feature (assets, overview, insights, auth, settings)
    ├── navigation     # Routes, BottomBarScreen
    └── theme          # Color schemes, typography, app theme
```

---

## 3. Data Flow

1. **UI layer** observes `StateFlow` from ViewModel.
2. **ViewModel** triggers repository calls and updates the state.
3. **Repository** calls API and wraps responses in `ApiResult`.
4. **SafeApiCaller** ensures safe API calls with proper error handling.
5. **UI components** update reactively based on state.

Example:

```
OverviewScreen
   ↓ observes
OverviewViewModel.state
   ↓ calls
StatsRepository.getOverviewStats()
   ↓ fetches from API
api.getStatsOverview()
   ↓ maps to domain model
OverviewStats
   ↓ updates state
OverviewScreen recomposes
```

---

## 4. State Management

* Each screen has a dedicated state object stored in `ViewModel` using `MutableStateFlow`.
* UI observes state via `collectAsState()`.
* Loading and error states are included in the state object.
* Pull-to-refresh uses `isLoading` flag.

---

## 5. UI Component Guidelines

* All screens use **Jetpack Compose**.
* Common components live in `ui.common`; feature-specific components in `ui.feature.<feature>.components`.
* Cards, rows, and charts are implemented as composables.
* Animations should be lightweight and use Compose animation APIs (`animateFloatAsState`, `animateIntAsState`).

---

## 6. Dependency Injection

* **Hilt** provides repositories, network modules, and other dependencies.
* Modules are located in `core.di`.

Example:

```kotlin
@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModel()
```

---

## 7. Networking

* Uses **Retrofit** with `SafeApiCaller` for API requests.
* Responses wrapped in `ApiResult<T>` for error handling.
* Tokens managed via `TokenProvider` and `AuthInterceptor`.

---

## 8. Testing Strategy

* Unit tests for ViewModels and utility classes.
* Composable previews for UI components.
* Optional integration tests for repositories using mock API responses.

---

This document gives a high-level overview for new developers. For feature-specific details, explore `ui.feature.*` folders, corresponding ViewModels, and repositories.