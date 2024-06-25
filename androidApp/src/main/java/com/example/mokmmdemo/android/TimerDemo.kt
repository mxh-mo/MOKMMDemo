package com.tencent.tmm.qmt.common.com.example.mokmmdemo.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mokmmdemo.Greeting
import com.example.mokmmdemo.android.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class OnceCommonTimer(private val timeOutCallback: () -> Unit) {
    fun start(interval: Long) {
        if (stoped == false) {
            stop()
        }
        stoped = false
        job = CoroutineScope(Dispatchers.Default).launch {
            delay(interval)
            CoroutineScope(Dispatchers.Main).launch {
                timeOutCallback()
            }
            stop()
        }
        job?.start()
    }
    fun stop() {
        stoped = true
        job?.cancel()
        job = null
    }

    private var job: Job? = null
    private var stoped: Boolean = true
}


/**
 * 计时器
 * @property timeOutCallback 计时结束回调
 * @constructor
 */
open class CommonTimer(private val timeOutCallback: (repeatIdex: Int) -> Unit) {
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

class TimerDemo : ComponentActivity() {
    private val timer = OnceCommonTimer {
        println("momo: timer out")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Row(
                modifier = Modifier
                    .padding(all = 30.dp)
                    .fillMaxWidth()
                    .height(44.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(text = "开始计时", modifier = Modifier.clickable {
                    println("momo: click start")
                    timer.start(interval = 2000)
                })
                Text(text = "结束计时", modifier = Modifier.clickable {
                    println("momo: click stop")
                    timer.stop()
                })
            }
        }
    }
}