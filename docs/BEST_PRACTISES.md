# Velaris Mobile App - UI Guidelines

## 1. Design Principles

* Follow **Material 3** design system.
* Consistent use of colors from `ColorScheme.kt`.
* Typography defined in `Type.kt`.
* Prefer **spacing by multiples of 4dp** for padding/margins.
* Components should be **reusable and modular**.

## 2. Top Bar / Navigation

* `CompactTopBar` is the standard top bar for all screens.
* For screens with actions, use the `actions` lambda to add buttons.
* `BottomBarNavigation` handles primary navigation.
* Floating action buttons (FAB) should be used for primary actions.

## 3. Cards & Sections

* Wrap grouped content in `SectionCard` for visual separation.
* Cards should have a **title**, optional content, and consistent **vertical spacing**.
* Examples:

    * `TotalValueCard` - shows portfolio value.
    * `InvestmentCategoriesCard` - categories overview.
    * `RecentActivityCard` - recent changes with optional scrollable list.
    * `AssetCard` - for grid/list representation of a single asset.

## 4. Layouts

* **LazyColumn** for vertical scrolling lists.
* **LazyVerticalGrid** for grid layouts (Assets, images).
* Prefer **fillMaxWidth** for rows in cards.
* Use `weight(1f)` to make columns/rows expand proportionally.
* Vertical and horizontal spacing: `Arrangement.spacedBy(8.dp/16.dp)`.

## 5. Animations

* Use Compose **`animate*AsState`** for smooth number transitions:

    * Portfolio values (`TotalValueCard`) animated from 0 → current.
    * Asset/Item counters animate integer values.
* For simple appearance effects, use **`AnimatedVisibility`** with fade in/out.

## 6. Colors & Theming

* `MaterialTheme.colorScheme` must be used for all text, background, and icon colors.
* Avoid hardcoding colors.
* Dark/light theme supported automatically via `Theme.kt`.

## 7. Text & Typography

* Titles: `headlineMedium` or `titleMedium`.
* Labels: `labelSmall`.
* Body: `bodyMedium` or `bodySmall`.
* Use `FontWeight.Medium` for emphasis.

## 8. Icons & Images

* Prefer **Material Icons** or custom SVGs.
* Images for assets: `AssetImage` composable.
* Images should have aspect ratio maintained (1:1 for grids).

## 9. Error & Empty States

* Display `ErrorBanner` for API/network errors.
* Use `EmptyState` composable when there is no data.
* Loading states use `LoadingState` composable.
* Always provide visual feedback for pull-to-refresh.

## 10. Reusable Components

* **DropdownMenuBox** for selections.
* **LabeledProgressBar** for performance metrics.
* Charts: `LineChart`, `BarPoint`, `PieChart` under `common/charts`.

## 11. Pull-to-Refresh

* Use `PullToRefreshBox` with `rememberPullToRefreshState`.
* Indicator color: `primaryContainer` background, `onPrimaryContainer` for spinner.

## 12. Accessibility & Fonts

* Ensure all texts have readable sizes and contrasts.
* Compose provides semantic labels automatically; customize if needed.
* Support dynamic font sizes via `MaterialTheme.typography`.

---

*This document should be referred to by all developers creating UI components or screens in Velaris Mobile App.*