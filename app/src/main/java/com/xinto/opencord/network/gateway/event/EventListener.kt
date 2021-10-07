package com.xinto.opencord.network.gateway.event

interface EventListener {

    fun onEvent(event: Event)

}