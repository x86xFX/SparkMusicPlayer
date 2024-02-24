package me.theek.spark.core.player

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.theek.spark.core.model.data.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaListener @Inject constructor(private val exoPlayer: ExoPlayer) : AudioService, Player.Listener {

    /**
     * State for music player
     */
    private val _musicPlayStateStream: MutableStateFlow<MusicPlayerState> = MutableStateFlow(MusicPlayerState.Initial)
    override val musicPlayStateStream: StateFlow<MusicPlayerState> = _musicPlayStateStream.asStateFlow()

    /**
     * Initialize exoplayer and add listener
     */
    init { exoPlayer.addListener(this) }

    /**
     * Add songs to exoplayer's mediaItems and set exoplayer's state as **COMMAND_PREPARE**
     * @param mediaItems - Songs list that pass into exoplayer.
     */
    override fun setMediaItemList(mediaItems: List<Song>) {
        exoPlayer.setMediaItems(
            mediaItems.map { song ->
                MediaItem.Builder()
                    .setUri(song.path)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTrackNumber(song.trackNumber)
                            .setReleaseYear(song.releaseYear)
                            .setDisplayTitle(song.songName)
                            .setAlbumArtist(song.artistName)
                            .setSubtitle(song.songName)
                            .build()
                    )
                    .build()
            }
        )
        exoPlayer.prepare()
    }

    /**
     * Handle player events like pausing, playing, seeking songs.
     * @param playerEvent - Player event.
     * @see [PlayerEvent]
     */
    override suspend fun onPlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            PlayerEvent.Backward -> exoPlayer.seekToPrevious()
            PlayerEvent.Forward -> exoPlayer.seekToNext()
            PlayerEvent.PlayPause -> playOrPause()
            is PlayerEvent.SelectedSongChange -> onSelectedSongChange(playerEvent.changedSongIndex)
        }
    }

    override fun stopPlayer() {
        exoPlayer.release()
    }

    /**
     * Determine exoplayer is currently playing or not and update [musicPlayStateStream].
     */
    private fun playOrPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            _musicPlayStateStream.value = MusicPlayerState.Pause
        } else {
            exoPlayer.play()
            _musicPlayStateStream.value = MusicPlayerState.Playing
        }
    }

    /**
     * Exoplayer play user selected song from song list.
     */
    private fun onSelectedSongChange(targetIndex: Int) {
        if (targetIndex == exoPlayer.currentMediaItemIndex) {
                playOrPause()
        } else {
            exoPlayer.seekToDefaultPosition(targetIndex)
            exoPlayer.playWhenReady = true
            _musicPlayStateStream.value = MusicPlayerState.Playing
        }
    }

}