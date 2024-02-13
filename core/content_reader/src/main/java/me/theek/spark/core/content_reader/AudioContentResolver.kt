package me.theek.spark.core.content_reader

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
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
            "${MediaStore.Audio.AudioColumns.DATE_ADDED} ASC"
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
                val song = Song(
                    id = 0,
                    path = songCursor.getString(dataColumn),
                    artistName = cursor.getString(artistColumn),
                    duration = cursor.getInt(durationColumn),
                    title = cursor.getString(titleColumn),
                    albumId = cursor.getInt(albumIdColumn),
                    trackNumber = cursor.getInt(trackColumn),
                    albumArt = ContentUris.withAppendedId(
                        "content://media/external/audio/albumart".toUri(),
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID))
                    )
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
                delay(100)
            }
        }
        emit(FlowEvent.Success(songs))
    }
}