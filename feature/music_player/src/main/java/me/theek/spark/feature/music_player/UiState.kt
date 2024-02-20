package me.theek.spark.feature.music_player

import me.theek.spark.core.model.data.Song

sealed interface UiState {
    data object Loading : UiState
    data class Progress(
        val progress: Float,
        val status: String?
    ) : UiState
    data class Success(val songs: List<Song>) : UiState
}