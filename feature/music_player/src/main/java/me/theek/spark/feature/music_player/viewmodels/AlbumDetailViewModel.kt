package me.theek.spark.feature.music_player.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.util.UiState

@HiltViewModel(assistedFactory = AlbumDetailViewModel.AlbumDetailViewModelFactory::class)
class AlbumDetailViewModel @AssistedInject constructor(
    @Assisted albumId: Int,
    songRepository: SongRepository
) : ViewModel() {
    @AssistedFactory
    interface AlbumDetailViewModelFactory {
        fun create(albumId: Int) : AlbumDetailViewModel
    }

    val uiState: StateFlow<UiState<List<Song>>> = songRepository.getAlbumSongs(albumId)
        .map { UiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )
}