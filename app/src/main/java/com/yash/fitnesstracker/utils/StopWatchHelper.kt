package com.yash.fitnesstracker.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.*

class StopwatchHelper {

    private var job: Job? = null
    private var startTime = 0L
    private var _elapsedTime = 0L
    val elapsedTime: Long
        get() = _elapsedTime

    var isRunning = false
        private set

    fun start(scope: CoroutineScope, onTick: (Long) -> Unit) {
        if (isRunning) return

        isRunning = true
        startTime = System.currentTimeMillis()
        job = scope.launch {
            while (isRunning) {
                val currentTime = System.currentTimeMillis()
                val totalElapsed = _elapsedTime + (currentTime - startTime)
                onTick(totalElapsed)
                delay(10L) // tick every 10ms
            }
        }
    }
    fun setElapsedTime(elapsed: Long) {
        _elapsedTime = elapsed
    }

    fun stop() {
        isRunning = false
        job?.cancel()
        _elapsedTime += System.currentTimeMillis() - startTime
    }

    fun reset() {
        stop()
        _elapsedTime = 0L
    }
}
