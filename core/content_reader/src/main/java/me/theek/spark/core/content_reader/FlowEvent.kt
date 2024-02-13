package me.theek.spark.core.content_reader

sealed interface FlowEvent<out T, out U> {
    data class Progress<T, U>(val data: U) : FlowEvent<T, U>
    data class Success<T>(val result: T) : FlowEvent<T, Nothing>
    data class Failure(val message: String?) : FlowEvent<Nothing, Nothing>
}