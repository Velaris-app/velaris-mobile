# Velaris Mobile - Developer Setup Guide

This document explains how to set up the Velaris Mobile project for new developers.

---

## 1. Prerequisites

* **Operating System:** Windows 10+ / macOS / Linux
* **JDK:** Java 17 or later
* **Android Studio:** Arctic Fox or newer recommended
* **Gradle:** 8.x (bundled with Android Studio)
* **Kotlin:** 1.9.x (bundled with Gradle plugin)
* **Git:** latest stable version

Optional:

* **Postman / REST Client** for testing APIs
* **Emulator / Physical Device** for testing

---

## 2. Cloning the Repository

```bash
git clone https://github.com/Velaris-app/velaris-mobile.git
cd velaris-mobile
```

---

## 3. Opening in Android Studio

1. Open Android Studio.
2. Select **Open an existing project**.
3. Navigate to `velaris-mobile/app/src/main/java` and open the project.
4. Let Android Studio sync Gradle and download dependencies.

---

## 4. Environment Configuration

1. **API Base URL:**

    * Defined in `core/di/NetworkModule.kt`.
    * Update if you need to point to staging or local backend.

2. **Datastore / Preferences:**

    * Located in `core/datastore/PreferencesDataStore.kt` and `PreferencesKeys.kt`.

3. **Authentication:**

    * Managed by `TokenProvider.kt`, `AuthInterceptor.kt`, and `TokenAuthenticator.kt`.

---

## 5. Running the App

* Use **Run > Run 'app'** in Android Studio.
* Select an emulator or connected device.
* The app should build and start with initial mock data if backend is not connected.

---

## 6. Testing

* **Unit tests:** `src/test/java` for ViewModels and repositories.
* **Instrumented tests:** `src/androidTest/java` for UI flows.
* Run tests via **Run > Run All Tests** or **Gradle tasks**.

---

## 7. Folder Structure Overview

* `core/`: utilities, serialization, DI modules, security
* `data/repository`: APIs and repositories for assets & stats
* `domain/model`: data models for assets, stats, overview
* `ui/`: all Compose UI screens, components, navigation, theme

---

## 8. Recommended Workflow

1. Create a feature branch:

   ```bash
   ```

git checkout -b feature/your-feature-name

````
2. Implement changes.
3. Run the app and tests.
4. Commit with clear message:
   ```bash
git commit -m "feat: add XYZ feature"
````

5. Push to origin and create a PR.

---

## 9. Tips & Best Practices

* Follow **MVVM + Compose** architecture.
* Use `viewModelScope.launch` for coroutines.
* Use `ApiResult` for all API calls and handle errors gracefully.
* UI updates should rely on **StateFlow / Compose state**.
* Keep `@Composable` functions small and reusable.
* All shared UI components go into `ui/common`.

---

## 10. Useful Commands

* **Clean & rebuild:** `./gradlew clean assembleDebug`
* **Run tests:** `./gradlew test` or `./gradlew connectedAndroidTest`
* **Lint:** `./gradlew lint`

---

This guide is intended to get a new developer fully set up and familiar with Velaris Mobile quickly.
