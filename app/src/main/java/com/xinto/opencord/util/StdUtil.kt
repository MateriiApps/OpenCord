package com.xinto.opencord.util

inline fun <A, B, C, D> Pair<A, B>.map(
    first: (A) -> C,
    second: (B) -> D
): Pair<C, D> {
    return first(this.first) to second(this.second)
}
