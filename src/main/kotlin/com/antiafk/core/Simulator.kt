package com.antiafk.core

import kotlinx.coroutines.*
import java.awt.Robot

class Simulator {
    private val robot = Robot()
    private var task: Job? = null

    // TODO: Generate an actual random
    private fun postDelay(): Long {
        return 500
    }

    suspend fun run(
        keys: Array<Pair<Int, String>>,
        randomOrder: Boolean,
        postDelay: PostDelay,
        onPress: suspend (String) -> Unit = {},
        onRelease: suspend (String) -> Unit = {})
    {
        withContext(Dispatchers.IO) {
            task = launch {
                while(true) {
                    if (randomOrder)
                        keys.shuffle()

                    keys.forEach { (code, key) ->
                        robot.keyPress(code)
                        onPress(key)
                        delay(50)
                        robot.keyRelease(code)
                        onRelease(key)

                        postDelay.sleep()
                    }
                }
            }.also(Job::start)
        }
    }

    suspend fun stop() {
        task?.cancelAndJoin()
    }
}