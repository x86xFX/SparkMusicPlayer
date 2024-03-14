package me.theek.spark.core.model.util

sealed interface Response<out T> {
    data class Success<T>(val data: T) : Response<T>
    data class Failure<T>(val message: String?) : Response<T>
    data class Loading<T>(
        val size: Int,
        val progress: Float,
        val message: String?
    ): Response<T>
}