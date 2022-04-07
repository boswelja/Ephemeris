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

## How to Use

Ephemeris is build around two core concepts, "page loaders" and "focus modes".
Page loaders control what data is displayed in a calendar page, while focus modes provide a simple way to determine whether a particular date is "in focus".
Check out the sections below for more details.

We're currently not publishing any builds, watch this space!

### UI

Ephemeris UI elements all have the same requirements - a page loader, a focus mode, and a day cell to populate the calendar with.
For platform-specific instructions, check out each module's own README.

### Page Sources

Ephemeris uses a `CalendarPageSource` to load data for each calendar page.
We provide default implementations for displaying one month per page (of varying weeks), and displaying one week per page.

If neither of the default implementations suit your requirements, you can implement your own`CalendarPageSource` and provide that to Ephemeris instead.
See [CalendarPageSource.kt](https://github.com/boswelja/Ephemeris/blob/main/core/src/commonMain/kotlin/com/boswelja/ephemeris/core/data/CalendarPageSource.kt) for examples.

### Focus Modes

A focus mode is a simple interface to help determine whether a particular date is considered "in focus".
Ephemeris performs no logic with this itself, but it is passed back to your day cell for you to use.
We provide implementations out-of-the-box for weekdays, displayed month days and all days.

You can implement your own focus mode by creating a class (or object) that implements the `FocusMode` interface.
See [FocusMode.kt](https://github.com/boswelja/Ephemeris/blob/main/core/src/commonMain/kotlin/com/boswelja/ephemeris/core/data/FocusMode.kt) for examples.
