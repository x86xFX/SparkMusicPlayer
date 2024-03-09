package me.theek.spark.feature.music_player.util

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Progress(
        val progress: Float,
        val status: String?
    ) : UiState<Nothing>
    data class Success<T>(val songs: T) : UiState<T>
    data class Failure<T>(val message: String?) : UiState<T>
}