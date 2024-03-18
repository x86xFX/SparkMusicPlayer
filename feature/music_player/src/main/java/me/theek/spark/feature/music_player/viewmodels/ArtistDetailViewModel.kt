package me.theek.spark.feature.music_player.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.theek.spark.core.data.repository.ArtistRepository
import me.theek.spark.core.model.data.ArtistRemoteData
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.model.util.Response
import me.theek.spark.core.player.AudioService
import me.theek.spark.core.player.PlayerEvent
import me.theek.spark.feature.music_player.util.UiState
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val audioService: AudioService
) : ViewModel() {

    private val _artistRemoteDetails = MutableStateFlow<UiState<ArtistRemoteData>>(UiState.Loading)
    val artistRemoteDetails = _artistRemoteDetails.asStateFlow()

    fun getArtistDetails(artistName: String) {
        viewModelScope.launch {
            when (val response = artistRepository.getAristDetails(artistName)) {
                is Response.Failure -> {
                    _artistRemoteDetails.value = UiState.Failure(response.message)
                }
                is Response.Loading -> {
                    _artistRemoteDetails.value = UiState.Loading
                }
                is Response.Success -> {
                    _artistRemoteDetails.value = UiState.Success(response.data)
                }
            }
        }
    }

    fun onSongClick(song: Song) {
        viewModelScope.launch {
            audioService.onPlayerEvent(PlayerEvent.SelectedSongChange(changedSongIndex = song.id.toInt() - 1))
        }
    }
}