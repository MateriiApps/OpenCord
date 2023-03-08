package com.xinto.opencord.util

data class Quad<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
) {
    override fun toString() = "($first, $second, $third, $fourth)"
}
