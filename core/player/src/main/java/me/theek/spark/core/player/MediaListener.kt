package me.theek.spark.core.player

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.theek.spark.core.model.data.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaListener @Inject constructor(private val exoPlayer: ExoPlayer) : AudioService, Player.Listener {

    /**
     * State of music player
     */
    private val _musicPlayStateStream: MutableStateFlow<MusicPlayerState> = MutableStateFlow(MusicPlayerState.Initial)
    override val musicPlayStateStream: StateFlow<MusicPlayerState> = _musicPlayStateStream.asStateFlow()

    private val progressJob: Job? = null

    /**
     * Initialize exoplayer and add listener
     */
    init {
        exoPlayer.addListener(this)
    }

    /**
     * Add songs to exoplayer's mediaItems and set exoplayer's state as **COMMAND_PREPARE**
     * @param mediaItems - Songs list that pass into exoplayer.
     */
    override fun addSongsToQueue(mediaItems: List<Song>) {
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
                            .setGenre(song.genres.joinToString())
                            .setSubtitle(song.songName)
                            .build()
                    )
                    .build()
            }
        )

        if (exoPlayer.playbackState == ExoPlayer.STATE_IDLE) {
            exoPlayer.prepare()
        }
    }

    override fun replaceCurrentQueue(mediaItems: List<Song>) {
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
                            .setGenre(song.genres.joinToString())
                            .setSubtitle(song.songName)
                            .build()
                    )
                    .build()
            },
            true
        )
    }

    override fun clearCurrentQueue() {
        exoPlayer.clearMediaItems()
    }

    override fun checkExoplayerStats(): String {
        return "MusicFiles: ${exoPlayer.mediaItemCount}\nCurrentPlaying: ${exoPlayer.currentMediaItemIndex}"
    }


    /**
     * Handle player events like pausing, playing, seeking songs.
     * @param playerEvent - Player event.
     * @see [PlayerEvent]
     */
    override suspend fun onPlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            PlayerEvent.Backward -> {
                exoPlayer.seekToPrevious()
            }

            PlayerEvent.Forward -> {
                exoPlayer.seekToNext()
            }

            PlayerEvent.PlayPause -> {
                playOrPause()
            }

            is PlayerEvent.SelectedSongChange -> {
                onSelectedSongChange(playerEvent.changedSongIndex)
            }

            is PlayerEvent.SeekTo -> {
                exoPlayer.seekTo(playerEvent.seekTo)
            }
        }
    }

    override fun setRepeatMode(@RepeatMode repeatMode: Int) {
        when (repeatMode) {
            RepeatMode.REPEAT_MODE_ALL -> {
                exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
            }

            RepeatMode.REPEAT_MODE_OFF -> {
                exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
            }

            RepeatMode.REPEAT_MODE_ONE -> {
                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            }
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        _musicPlayStateStream.update { MusicPlayerState.CurrentRepeatMode(repeatMode) }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                _musicPlayStateStream.update { MusicPlayerState.Buffering(progress = exoPlayer.currentPosition) }
            }

            Player.STATE_READY -> {
                _musicPlayStateStream.update { MusicPlayerState.Ready(duration = exoPlayer.duration) }
            }

            else -> Unit
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _musicPlayStateStream.update { MusicPlayerState.Playing(isPlaying = isPlaying) }
        _musicPlayStateStream.update { MusicPlayerState.CurrentPlaying(exoPlayer.currentMediaItemIndex) }
        if (isPlaying) {
            CoroutineScope(Dispatchers.Main).launch { startProgressUpdate() }
        } else {
            stopProgressUpdate()
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        when (reason) {
            Player.MEDIA_ITEM_TRANSITION_REASON_AUTO -> {
                _musicPlayStateStream.update { MusicPlayerState.CurrentPlaying(mediaItemIndex = exoPlayer.currentMediaItemIndex) }
            }

            else -> Unit
        }
    }

    override fun stopPlayer() {
        exoPlayer.release()
    }

    /**
     * Determine exoplayer is currently playing or not and update [musicPlayStateStream].
     */
    private suspend fun playOrPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            stopProgressUpdate()
        } else {
            exoPlayer.play()
            _musicPlayStateStream.update { MusicPlayerState.Playing(isPlaying = true) }
            startProgressUpdate()
        }
    }

    /**
     * Get current playing song's progress. Delay is automatically cancelled in new emit.
     */
    private suspend fun startProgressUpdate() = progressJob.run {
        while (true) {
            delay(500)
            _musicPlayStateStream.update { MusicPlayerState.Progress(progress = exoPlayer.currentPosition) }
            _musicPlayStateStream.update { MusicPlayerState.CurrentPlaying(mediaItemIndex = exoPlayer.currentMediaItemIndex) }
        }
    }

    private fun stopProgressUpdate() {
        progressJob?.cancel()
        _musicPlayStateStream.update { MusicPlayerState.Playing(isPlaying = false) }
    }

    /**
     * Exoplayer play user selected song from song list.
     */
    private suspend fun onSelectedSongChange(targetIndex: Int) {
        if (targetIndex == exoPlayer.currentMediaItemIndex) {
            playOrPause()
        } else {
            startPlaying(targetIndex)
        }
    }

    private fun startPlaying(targetIndex: Int) {
        exoPlayer.seekToDefaultPosition(targetIndex)
        exoPlayer.playWhenReady = true
        _musicPlayStateStream.update { MusicPlayerState.Playing(isPlaying = true) }
    }
}