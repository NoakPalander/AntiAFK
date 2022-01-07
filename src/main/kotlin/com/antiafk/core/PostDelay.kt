package com.antiafk.core

import kotlinx.coroutines.delay
import kotlin.random.Random

sealed class PostDelay {
    abstract suspend fun sleep()

    class Default(private val duration: Long) : PostDelay() {
        override suspend fun sleep() {
            delay(duration)
        }
    }

    class Interval(private val start: Long, private val end: Long) : PostDelay() {
        override suspend fun sleep() {
            delay(Random.nextLong(start, end))
        }
    }
}