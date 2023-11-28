package com.timers4medi.extensions

import kotlin.math.round

fun Float.round(decimals: Int): Float {
    var multiplier = 1f
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}