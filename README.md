# Game Deals App

Shows video game deals from multiple stores using the CheapShark API.

## Run

1. Open in Android Studio
2. Sync Gradle
3. Run (API 35+)

## Features

- Search for games
- View deals with prices and discounts
- Click deal to see details
- Save games to favorites
- Manage saved games

## Tech Stack

- **Kotlin 2.0.21**
- **Retrofit 2.9.0** (API client)
- **Glide 4.16.0** (Images)
- **Coroutines** (Async)
- **Material Design 3** (UI)
- **Android 15-16** (SDK 35-36)

## Project Structure

```
app/src/main/java/com/example/gameapp/
├── MainActivity.kt (Main screen)
├── GameDetailActivity.kt (Game details)
├── FavoritesActivity.kt (Saved games)
├── GameDealAdapter.kt (List adapter)
├── FavoritesAdapter.kt (Favorites adapter)
├── FavoritesManager.kt (Save/load favorites)
└── data/
    ├── api/
    │   ├── CheapSharkApiService.kt
    │   └── RetrofitInstance.kt
    └── model/
        ├── GameDeal.kt
        └── StoreNames.kt
```

## How It Works

**Main Screen**: Search for games, see all deals

**Detail Screen**: View full info, add to favorites

**Favorites Screen**: View and manage saved games

## API

**CheapShark** - https://www.cheapshark.com/api/1.0/

- Free, no auth required
- Gets game deals from 70+ stores
- Endpoints: `/deals`, `/games`, `/stores`

## Dependencies

```gradle
retrofit = "2.9.0"
glide = "4.16.0"
coroutines = "1.7.1"
material = "1.13.0"
gson = "2.10.1"
```

## Build

```bash
./gradlew clean build
./gradlew installDebug
```

Or use Android Studio Run button.

## Key Files

- **MainActivity.kt** - Search deals, show list
- **GameDetailActivity.kt** - Show game info, add to favorites
- **FavoritesActivity.kt** - List saved games
- **FavoritesManager.kt** - Save/load preferences
- **GameDealAdapter.kt** - Bind deals to RecyclerView
- **CheapSharkApiService.kt** - Retrofit endpoints
- **RetrofitInstance.kt** - API client config

## What It Does

1. Loads top-rated game deals on startup
2. User searches for games
3. Shows all deals with prices and stores
4. Click deal → view full details
5. Add to favorites → saves locally
6. View favorites → manage saved games

#Final Notes

This project was collaboratively developed by both team members, who contributed to the design, development, and implementation of the application, including UI development, API integration, and overall functionality.
