package com.xinto.opencord.gateway.event

interface Event<T> {
    val data: T
}