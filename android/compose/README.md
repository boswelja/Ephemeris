# android-compose

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
val calendarState = rememberCalendarState {
    CalendarMonthPageSource(
        DayOfWeek.SUNDAY
    )
}
EphemerisCalendar(
    calendarState = calendarState
) { dateCellState ->
    // Your date cell content here
}
```

### EphemerisCalendarState

`EphemerisCalendarState` is a state holder for the `EphemerisCalendar` Composable. You can use it to configure the calendar as you see fit.

When creating a `EphemerisCalendarState`, it is required you provide an initial page source.

```kotlin
val calendarState = rememberCalendarState {
    CalendarMonthPageSource(
        DayOfWeek.SUNDAY
    )
}
```
