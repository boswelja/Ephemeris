# android-views

A UI module enabling the use of Ephemeris on Android with XML Views.

## Usage

Add the library to your module's `build.gradle.kts`

```kotlin
implementation("io.github.boswelja.ephemeris:android-views:<latest_version>")
```

### EphemerisCalendarView

`EphemerisCalendarView` extends `RecyclerView` to provide a fast & efficient calendar view. Currently all configuration must be done from within your Activity or Fragment, however XML attributes are planned. You can follow their status via #36.

You can add `EphemerisCalendarView` to your layouts like so:

```xml
<com.boswelja.ephemeris.views.EphemerisCalendarView
    android:id="@+id/calendar_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

And to configure the calendar, you must call `initCalendar` on your `EphemerisCalendarView` **once**. Calling `initCalendar` more than once may result in unexpected behavior.

```kotlin
binding.calendarView.initCalendar(
    pageSource = CalendarMonthPageSource(
        firstDayOfWeek = DayOfWeek.SUNDAY,
        focusMode = CalendarMonthPageSource.FocusMode.MONTH
    ),
    dayBinder = CalendarDayBinder()
)
```

Since Ephemeris provides no date cells out-of-the-box, you need to tell it how to create a cell. This is done by providing a class that extends the `CalendarDateBinder` interface. `CalendarDateBinder` wraps a `RecyclerView.ViewHolder` and tells Ephemeris how to create and bind a cell. See below for a simple example that displays dates, with emphasis on "in focus" dates.

```kotlin
class CalendarDayBinder : CalendarDateBinder<CalendarDateViewHolder> {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): CalendarDateViewHolder {
        val view = DayBinding.inflate(inflater, parent, false)
        return CalendarDateViewHolder(view)
    }

    override fun onBindView(viewHolder: CalendarDateViewHolder, calendarDay: CalendarDay) {
        viewHolder.onBind(calendarDay)
    }
}

class CalendarDateViewHolder(
    private val binding: DayBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(calendarDay: CalendarDay) {
        binding.dayNum.apply {
            // Set the text for this date cell
            text = calendarDay.date.dayOfMonth.toString()
            // Update the text color to reflect the date focus state
            setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (calendarDay.isFocusedDate) {
                        R.color.purple_500
                    } else {
                        R.color.black
                    }
                )
            )
        }
    }
}
```

That's it for basic usage! Watch this space for more advanced use cases in the future.
