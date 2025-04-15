# 📱 MogFlix - Android App Architecture Overview

MogFlix is a modular Android application designed to allow users to catalog, rate, and manage their watched movies. It adopts a **clean MVVM architecture**, utilizes **Jetpack Compose** for UI, and integrates both **local persistence** and **remote API** support.

---

## 🧱 Project Structure

```
app/src/main/java/com/example/mogflix
├── MainActivity.kt                // App entry point
├── data                          // Model layer (local & remote)
│   ├── local                     // Room DB setup
│   │   ├── Converters.kt
│   │   ├── MovieDatabase.kt
│   │   ├── MovieModule.kt        // DI for local data
│   │   ├── dao
│   │   │   └── MovieDAO.kt
│   │   └── entity
│   │       └── MovieEntity.kt
│   ├── model
│   │   └── Movie.kt              // Core domain model
│   ├── remote                    // Retrofit API integration
│   │   ├── MovieSearchResponse.kt
│   │   ├── TmdbApiClient.kt
│   │   └── TmdbApiService.kt
├── ui                            // View layer (Jetpack Compose)
│   ├── components                // Reusable UI components
│   │   ├── DatePickerModal.kt
│   │   └── MovieCard.kt
│   ├── navigation                // Nav routes and destinations
│   │   └── NavRoutes.kt
│   ├── screens                   // Feature screens
│   │   ├── AddMovieScreen.kt
│   │   └── MovieListScreen.kt
│   └── theme                     // Colors, typography, themes
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── viewmodel                    // ViewModel layer
    └── MovieViewModel.kt
```

---

## 🎯 Architecture Pattern: MVVM

- **Model** (`data/`)
  - Handles data logic and business models.
  - Abstracts away persistence (Room) and networking (Retrofit).
- **View** (`ui/`)
  - Jetpack Compose-based UI that reacts to ViewModel state.
- **ViewModel** (`viewmodel/`)
  - Acts as a bridge between View and Model.
  - Holds UI state and exposes flows/state for observation.

---

## 🧩 Key Design Patterns

| Pattern | Role |
|--------|------|
| **MVVM** | Ensures separation of concerns and testability |
| **Repository-style access** *(optional)* | ViewModel calls into data sources |
| **Dependency Injection** | Managed via `MovieModule.kt` (consider Hilt) |
| **Unidirectional Data Flow** | ViewModel -> State -> UI (via `StateFlow` or `LiveData`) |
| **Single Source of Truth** | Room database persists core data |
| **Composable UI** | Declarative UI with Jetpack Compose |

---

## 🔌 Technologies Used

- **Kotlin** with Coroutines + Flow
- **Jetpack Compose** for declarative UI
- **Room** for local database
- **Retrofit** for TMDb API integration
- **Modular file structure** for scalability

---

## 💡 Future Suggestions

- Create a `repository/` layer for clearer separation between `ViewModel` and data sources.
- Add `domain/` layer for use cases and business logic encapsulation.
- Explicit dependency injection using **Hilt** (if not already).

---

