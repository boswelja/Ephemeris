# Ephemeris

The flexible, multiplatform calendar library!

## Supported Platforms

### Core

Ephemeris Core currently runs on the following platforms

| Platform | Supported |
| -- | -- |
| Android | ✔️ |
| iOS | ✔️ |
| MacOS | ✔️ |
| JVM | ✔️ |
| Windows | ✔️ |
| Linux | ✔️ |
| JS (Node) | ✔️ |
| JS (Browser) | ✔️ |

### UI Frameworks

| Platform | Supported |
| -- | -- |
| Android (Jetpack Compose) | ✔️ |
| Android (XML Views) | ✔️ |
| iOS (SwiftUI) | Coming soon |

## How to Use

### Importing

See module READMEs for more details on importing and using Ephemeris.

#### [Core](https://github.com/boswelja/Ephemeris/tree/main/core/README.md)

#### [Android (Views)](https://github.com/boswelja/Ephemeris/tree/main/android/views/README.md)

### Page Sources

Ephemeris uses a `CalendarPageSource` to load data for each calendar page.
We provide default implementations for displaying one month per page (of varying weeks), and displaying one week per page.

If neither of the default implementations suit your requirements, you can implement your own`CalendarPageSource` and provide that to Ephemeris instead.
See [CalendarPageSource.kt](https://github.com/boswelja/Ephemeris/blob/main/core/src/commonMain/kotlin/com/boswelja/ephemeris/core/data/CalendarPageSource.kt) for examples.
