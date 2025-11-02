package org.example.classModels.operation

data class Operation(
    val id: Int,
    val name: String,
    val machineType: String,
    val durationMinutes: Int,
    val sequence: Int,
    val isCompleted: Boolean = false
)
