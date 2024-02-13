package me.theek.spark.core.content_reader

import java.lang.Float.min
import kotlin.math.max

data class RetrieveProgress(
    val message: String,
    val progress: Progress
) {
    data class Progress(
        val size: Int,
        val currentProgress: Int
    ) {
        fun asFloat(): Float {
            val progressFraction = currentProgress.toFloat() / size
            return min(1.0f, max(0.0f, progressFraction))
        }

    }
}