package com.boswelja.ephemeris.views

fun awaitSettled(block: () -> Any) {
    var lastValue: Any? = null
    while (true) {
        val currentValue = block()
        if (lastValue != null && currentValue == lastValue) break
        lastValue = currentValue
        Thread.sleep(10)
    }
}
