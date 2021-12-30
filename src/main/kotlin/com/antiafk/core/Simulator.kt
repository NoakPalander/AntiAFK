package com.antiafk.core

import kotlinx.coroutines.*
import java.awt.Robot

class Simulator {
    private val robot = Robot()
    private var running: Boolean = false

    // TODO: Generate an actual random
    private fun postDelay(): Long {
        return 500
    }

    fun run(keys: Array<Pair<Int, String>>, randomOrder: Boolean, randomDelay: Boolean,
                    onPress: (String) -> Unit = {}, onRelease: (String) -> Unit = {}) {

        if (!running) {
            running = true
            CoroutineScope(Dispatchers.IO).launch {
                while (running) {
                    if (randomOrder)
                        keys.shuffle()

                    keys.forEach { (code, key) ->
                        robot.keyPress(code)
                        onPress(key)
                        delay(50)
                        robot.keyRelease(code)
                        onRelease(key)

                        delay(if (randomDelay) postDelay() else 1000)
                    }
                }
            }
        }
    }

    fun stop() {
        running = false
    }
}