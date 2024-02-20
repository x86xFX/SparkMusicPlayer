package me.theek.spark.core.content_reader

import android.content.Context
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import me.theek.spark.core.model.data.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioContentResolver @Inject constructor(@ApplicationContext private val context: Context) : ContentResolverHelper {

    override fun getAudioData(): SongFlow = flow {
        val songs: MutableList<Song> = mutableListOf()

        val songCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // External storage
            arrayOf(
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.TRACK
            ),
            "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?",
            arrayOf("1"),
            "${MediaStore.Audio.AudioColumns.TITLE} ASC"
        )

        songCursor?.use { cursor ->
            var progress = 0
            val size = cursor.count
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)
            val trackColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)

            while (currentCoroutineContext().isActive && cursor.moveToNext()) {
                val path = songCursor.getString(dataColumn)
                val song = Song(
                    id = 0,
                    path = path,
                    artistName = cursor.getString(artistColumn),
                    duration = cursor.getInt(durationColumn),
                    title = cursor.getString(titleColumn),
                    albumId = cursor.getInt(albumIdColumn),
                    trackNumber = cursor.getInt(trackColumn)
                )

                songs.add(song)
                progress++
                emit(
                    FlowEvent.Progress(
                        RetrieveProgress(
                            message = "${song.title} • ${song.artistName}",
                            progress = RetrieveProgress.Progress(
                                size = size,
                                currentProgress = progress
                            )
                        )
                    )
                )
            }
        }
        emit(FlowEvent.Success(songs))
    }
}