package com.example.mokmmdemo.android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class WellnessTask(
    val id: Int,
    val label: String,
    initialChecked: Boolean = false
) {
    var checked by mutableStateOf(initialChecked)
}
fun getWellnessTasks() = List(30) { i ->
    WellnessTask(i, "Task # $i")
}

class WellnessViewModel: ViewModel() {
    private val _tasks = getWellnessTasks().toMutableStateList()
    val tasks: List<WellnessTask>
        get() = _tasks

    fun remove(item: WellnessTask) {
        _tasks.remove(item)
    }

    fun changeTaskChecked(
        item: WellnessTask,
        checked: Boolean
    ) {
        _tasks.find {
            it.id == item.id
        }?.let { task ->
            task.checked = checked
        }
    }
}