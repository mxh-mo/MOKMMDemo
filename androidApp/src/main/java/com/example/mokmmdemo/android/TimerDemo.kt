package com.tencent.tmm.qmt.common.com.example.mokmmdemo.android

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 计时器
 * @property timeOutCallback 计时结束回调
 * @constructor
 */
open class KMTCommonTimer(private val timeOutCallback: (repeatIdex: Int) -> Unit) {
    /**
     * 停止计时
     */
    fun stop() {
        stoped = true
        job?.cancel()
        job = null
    }

    /**
     * 开始计时
     * @param interval Long 计时间隔
     * @param repeatCount Int 重复次数
     */
    fun start(interval: Long, repeatCount: Int = 1) {
        if (stoped == false) { stop() }
        stoped = false
        job = CoroutineScope(Dispatchers.Default).launch {
            repeat(repeatCount) {
                delay(interval)
                CoroutineScope(Dispatchers.Main).launch {
                    timeOutCallback(it)
                }
                if (stoped) return@repeat
                if (repeatCount - 1 == it) stop()
            }
        }
        job?.start()
    }

    private var job: Job? = null
    private var stoped: Boolean = true
}