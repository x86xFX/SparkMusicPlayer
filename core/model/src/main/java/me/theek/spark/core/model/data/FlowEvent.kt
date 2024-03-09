package me.theek.spark.core.model.data

import kotlin.math.max
import kotlin.math.min

sealed interface FlowEvent<T> {
    data class Progress<T>(
        val size: Int,
        val progress: Int,
        val message: String?
    ) : FlowEvent<T> {
        fun asFloat() : Float {
            val progressFraction = progress.toFloat() / size
            return min(1.0f, max(0.0f, progressFraction))
        }
    }

    data class Success<T>(val data: T) : FlowEvent<T>
    data class Failure<T>(val message: String) : FlowEvent<T>
}