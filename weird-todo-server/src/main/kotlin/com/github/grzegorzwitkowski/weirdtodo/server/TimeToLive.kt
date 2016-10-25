package com.github.grzegorzwitkowski.weirdtodo.server

fun timeToLive(milliseconds: Long): Long {
    return milliseconds
}

fun Int.days(): Long {
    return this * 86400000L
}