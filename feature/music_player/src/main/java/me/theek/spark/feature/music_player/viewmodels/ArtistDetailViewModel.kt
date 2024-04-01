package me.theek.spark.feature.music_player.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.theek.spark.core.data.repository.ArtistRepository
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.model.data.ArtistRemoteData
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.model.util.Response
import me.theek.spark.core.player.AudioService
import me.theek.spark.feature.music_player.util.UiState

@HiltViewModel(assistedFactory = ArtistDetailViewModel.ArtistDetailViewModelFactory::class)
class ArtistDetailViewModel @AssistedInject constructor(
    @Assisted val artistName: String,
    private val artistRepository: ArtistRepository,
    private val audioService: AudioService,
    private val songRepository: SongRepository
) : ViewModel() {

    @AssistedFactory
    interface ArtistDetailViewModelFactory {
        fun create(artistName: String) : ArtistDetailViewModel
    }

    private val _artistRemoteDetails = MutableStateFlow<UiState<ArtistRemoteData>>(UiState.Loading)
    val artistRemoteDetails = _artistRemoteDetails.asStateFlow()
    private val _artistSongState = MutableStateFlow<UiState<List<Song>>>(UiState.Loading)
    val artistSongState = _artistSongState.asStateFlow()

    init {
        getArtistRemoteDetails()
        getCurrentArtistSongs()
    }

    fun getArtistRemoteDetails() {
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

    fun checkExoplayerStats() {
        println(audioService.checkExoplayerStats())
    }

    private fun getCurrentArtistSongs() {
        viewModelScope.launch {
            when (val response = songRepository.getArtistSongs(artistName)) {
                is Response.Loading -> {
                    _artistRemoteDetails.value = UiState.Loading
                }
                is Response.Failure -> {
                    _artistSongState.value = UiState.Failure(response.message)
                }
                is Response.Success -> {
                    _artistSongState.value = UiState.Success(response.data)
                }
            }
        }
    }
}