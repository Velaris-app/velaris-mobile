# Velaris Mobile App

Velaris is a private mobile application for tracking alternative investments such as collectible cards, figurines, LEGO sets, and other rare assets. It is **not open-source** and is intended for internal use and beta testing.

---

## Overview

Velaris helps users manage, visualize, and analyze their alternative investment portfolios with features like:

* **Portfolio Overview**: Total portfolio value, performance trends, and category breakdown.
* **Asset Management**: Add, edit, and delete assets.
* **Insights & Stats**: Performance charts, top performers, and tag-based statistics.
* **Recent Activity**: Track recent changes and updates in your portfolio.
* **Settings**: Configure account, currency, and appearance.

---

## Architecture

Velaris follows a modular architecture using **MVVM** with **Jetpack Compose** for UI:

* `core`: Utilities, serialization, datastore, DI modules, and security.
* `data`: Repositories for accessing backend APIs.
* `domain`: Data models representing assets and stats.
* `ui`: All screens, features, and reusable components.

    * `common`: Shared UI components like charts and top bars.
    * `feature`: Screens and components grouped by feature.
    * `navigation`: Bottom bar and navigation routes.
    * `theme`: Color schemes and typography.

State management uses `StateFlow` and `ViewModel`s for reactive UI updates.

---

## Developer Setup

### Prerequisites

* Android Studio Arctic Fox or newer.
* Kotlin 1.9+
* JDK 17+
* Access to internal backend APIs (credentials required).

### Setup

1. Clone the private repository (credentials required).
2. Open in Android Studio.
3. Build the project and resolve dependencies.
4. Configure API keys and DataStore settings.
5. Run on an emulator or physical device.

### Project Structure

```
src/main/java/com/velaris/mobile/
├── core/       # Utilities, DI, security, serialization
├── data/       # Repositories
├── domain/     # Models
└── ui/         # Screens, components, theme, navigation
```

---

## Documentation

See the `docs/` folder for detailed developer documentation:

* `ARCHITECTURE.md` - App architecture overview
* `UI_GUIDELINES.md` - UI patterns, components, and theming
* `API.md` - Backend endpoints and usage
* `DEV_SETUP.md` - Local development and build instructions

---

## Notes

* This application is **internal and not open-source**.
* Do **not** share the repository or API credentials externally.
* For issues or support, contact the internal mobile dev team.

---

## License

Proprietary and confidential. Unauthorized use is prohibited.