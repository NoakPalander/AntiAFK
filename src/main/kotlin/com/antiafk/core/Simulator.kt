package com.antiafk.core

import kotlinx.coroutines.*
import java.awt.Robot

class Simulator {
    private val robot = Robot()
    private var task: Job? = null

    suspend fun run(
        keys: Array<Pair<Int, String>>,
        shuffle: Boolean,
        postDelay: PostDelay,
        onPress: suspend (String) -> Unit = {},
        onRelease: suspend (String) -> Unit = {})
    {
        withContext(Dispatchers.IO) {
            task = launch {
                while (true) {
                    if (shuffle)
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
            }
        }
    }

    suspend fun stop() {
        task?.cancelAndJoin()
    }
}