package com.timers4medi.utils

import java.time.LocalTime

/**
 * Format given time in sec to min:sec output fe. 1800 -> // 30:00
 */
fun countdownFormatter(timeSec: Long) = with(LocalTime.ofSecondOfDay(timeSec)) {
    String.format("%02d:%02d", minute, second)
}