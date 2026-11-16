# Explore Sri Lanka – Android Catalogue App

An Android catalogue application that helps users browse notable destinations in Sri Lanka. The app provides a searchable, filterable catalogue, item detail pages, a dedicated favourites screen, and layouts that adapt to phone/tablet sizes and portrait/landscape orientations.

## Features

- Browse 10 Sri Lankan destinations with locally stored images and descriptions.
- Search live by destination title or description.
- Filter the catalogue by category (for example: UNESCO, History, Nature, Beach, Surfing and Hills).
- Open a destination detail screen with a larger image, description, external information link, and favourite control.
- Add or remove favourites using the heart control or a long press on a catalogue card.
- View saved destinations on a dedicated **Favourites** screen.
- Preserve the catalogue, favourite selections, search query, and category filter through configuration changes using a shared ViewModel.
- Adapt grid columns to the available screen configuration: 2 columns on portrait phones, 3 on landscape phones and tablets, and 4 on wide (720dp+) landscape tablets.
- Use compact list thumbnails and `ListAdapter`/`DiffUtil` to reduce image decoding and unnecessary row updates while scrolling. The original large photos are also resized for detail pages to keep memory usage reasonable.
- Support light and dark themes.

## Technology

- Kotlin
- Android XML layouts (no Jetpack Compose)
- Activities and Fragments
- RecyclerView and GridLayoutManager
- ViewModel and LiveData
- Material Components
- Gradle Kotlin DSL

> **Technology note:** The project uses AndroidX libraries for the View-based Fragment, RecyclerView, and lifecycle/ViewModel APIs. It does not use Jetpack Compose. The assignment brief contains both a broad reference to “Jetpack” and a later explicit restriction on “Jetpack Compose”; confirm the intended interpretation with the unit lecturer if required.

## Project structure

```text
app/src/main/
├── java/com/example/catalogapp/
│   ├── CatalogAdapter.kt
│   ├── CatalogViewModel.kt
│   ├── DetailFragment.kt
│   ├── FavouritesFragment.kt
│   ├── Item.kt
│   ├── ItemFragment.kt
│   ├── ItemList.kt
│   └── MainActivity.kt
└── res/
    ├── drawable/                 # Vector and selector resources
    ├── drawable-nodpi/           # Optimised destination images and list thumbnails
    ├── layout/                   # Default portrait layouts
    ├── layout-land/              # Landscape layouts
    ├── layout-sw600dp/           # Tablet layouts
    ├── layout-sw600dp-land/      # Landscape tablet layouts
    └── values*/                  # Strings, themes and adaptive grid values
```

## Requirements

- Android Studio with Android SDK Platform 36 installed.
- JDK 17 or a JDK supported by the installed Android Gradle Plugin.
- Internet access on the first build so Gradle can download dependencies.

## Build and run

1. Clone this repository.
2. Open the project root (the directory containing `settings.gradle.kts`) in Android Studio.
3. Allow Gradle to sync and install any missing Android SDK components.
4. Select the `app` run configuration and launch it on an emulator or Android device.

From a terminal, build a debug APK with:

```bash
# macOS / Linux
./gradlew assembleDebug

# Windows PowerShell
.\gradlew.bat assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Group members

| Name | Email |
|---|---|
| Damiru Gamage | damiru.399@gmail.com |
| Thishith Dahanayake | d.dahanayake@student.curtin.edu.au |
| Chamithka Gunasekara | chamithkagunasekera@gmail.com |
| Rishon Mendis | rishonmendis@gmail.com |

## Repository contents

This repository contains the Android Studio project source and Gradle wrapper. Local SDK configuration, IDE workspace files, build outputs, assignment PDFs, reports, and the Declaration of Originality are intentionally excluded.
