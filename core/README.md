# core

![Maven Central](https://img.shields.io/maven-central/v/io.github.boswelja.ephemeris/core)

The core module of Ephemeris. This provides all generic implementations for platform-specific modules to use, such as page loaders, caching and data types.

## Usage

### Extending interfaces for custom layouts

#### [CalendarPageSource](https://github.com/boswelja/Ephemeris/blob/main/core/src/commonMain/kotlin/com/boswelja/ephemeris/core/data/CalendarPageSource.kt)

`CalendarPageSource` is an interface that defines how pages are loaded, and what data is loaded on them. `CalendarPageSource` is called to create instances of `CalendarPage`.

Currently, the only restrictions on a `CalendarPage` is that it contains at least one row and at least one date.

For convenience, we've created a CalendarPage DSL to help build your calendar pages. For examples of usages, check out some of the [default CalendarPageSource implementations](https://github.com/boswelja/Ephemeris/blob/main/core/src/commonMain/kotlin/com/boswelja/ephemeris/core/data)

### Platform UI development

There are currently two requirements for platform UI modules.

1. Implement `CalendarState` for standardised controls.
2. Use `CalendarPageLoader` to load data.

#### [CalendarState](https://github.com/boswelja/Ephemeris/blob/main/core/src/commonMain/kotlin/com/boswelja/ephemeris/core/ui/CalendarState.kt)

`CalendarState` defines a common interface for Ephemeris calendar views. It is expected all calendar views contain these functions *in some way*. For example, Android's `EphemerisCalendarView` implements `CalendarState` directly, whereas the `EphemerisCalendar` Composable takes a class that implements `CalendarState`.

It is expected these core functions work exactly the same across platforms. Any extra functions exposed from UI libraries are not guaranteed to be consistent.

#### [CalendarPageLoader](https://github.com/boswelja/Ephemeris/blob/main/core/src/commonMain/kotlin/com/boswelja/ephemeris/core/ui/CalendarPageLoader.kt)

`CalendarPageLoader` is a caching layer around `CalendarPageSource`. UI implementations should use an instance of `CalendarPageLoader` to handle loading data, rather than loading from `CalendarPageSource` directly.

When the `CalendarPageSource` changes, it is expected to create a new implementation of `CalendarPageLoader` and discard the old implementation.
