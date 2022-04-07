package com.xinto.opencord.util

class ListMap<K, V> {

    private val map = mutableMapOf<K, MutableList<V>>()

    operator fun get(key: K): MutableList<V> {
        if (map[key] == null) {
            map[key] = mutableListOf()
        }

        return map[key]!!
    }

}