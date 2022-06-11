# android-compose

![Maven Central](https://img.shields.io/maven-central/v/io.github.boswelja.ephemeris/android-compose)

A UI module enabling the use of Ephemeris on Android with Jetpack Compose.

## Usage

Add the library to your module's `build.gradle.kts`

```kotlin
implementation("io.github.boswelja.ephemeris:android-compose:<latest_version>")
```

### EphemerisCalendar

The `EphemerisCalendar` Composable utilises a `LazyRow` under the hood to provide a fast & efficient calendar view.

Basic usage is as follows:

```kotlin
val calendarState = rememberCalendarState()
EphemerisCalendar(
    pageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY),
    calendarState = calendarState
) { dateCellState ->
    // Your date cell content here
}
```
