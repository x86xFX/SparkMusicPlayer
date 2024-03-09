package me.theek.spark.core.content_reader

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.simplecityapps.ktaglib.KTagLib
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import me.theek.spark.core.model.data.FlowEvent
import me.theek.spark.core.model.data.Song
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaStoreReader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val kTagLib: KTagLib
) {

    fun getAudioData(): Flow<FlowEvent<List<Song>>> = flow<FlowEvent<List<Song>>> {

        var songs: MutableList<Song> = mutableListOf()

        val songCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.TRACK,
                MediaStore.Audio.AudioColumns.YEAR,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.SIZE,
                MediaStore.Audio.AudioColumns.DATE_MODIFIED,
                MediaStore.Audio.AudioColumns.MIME_TYPE
            ),
            "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?",
            arrayOf("1"),
            "${MediaStore.Audio.AudioColumns.TITLE} ASC"
        )
        songCursor?.use { cursor ->
            var progress = 0

            while (currentCoroutineContext().isActive && cursor.moveToNext()) {
                val song = Song(
                    id = 0,
                    songName = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)),
                    artistName = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)),
                    albumId = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)),
                    duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)),
                    trackNumber = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)),
                    releaseYear = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR)),
                    genres = emptyList(),
                    mimeType = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.MIME_TYPE)),
                    lastModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)),
                    size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)),
                    path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)),
                    externalId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)).toString()
                )
                songs.add(song)
                progress++

                emit(
                    FlowEvent.Progress(
                        size = cursor.count,
                        progress = progress,
                        message = "${song.songName} • ${song.artistName}"
                    )
                )
            }
        }

        // Extract song genres
        context.contentResolver.query(
            MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME),
            null,
            null,
            null
        )?.use { genreCursor ->
            while (currentCoroutineContext().isActive && genreCursor.moveToNext()) {
                val id = genreCursor.getLong(genreCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID))
                val genre = genreCursor.getString(genreCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME))

                context.contentResolver.query(
                    MediaStore.Audio.Genres.Members.getContentUri("external", id),
                    arrayOf(MediaStore.Audio.Media._ID),
                    null,
                    null,
                    null
                )?.use { songGenreCursor ->
                    while (currentCoroutineContext().isActive && songGenreCursor.moveToNext()) {
                        val songId = songGenreCursor.getLong(songGenreCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)).toString()
                        songs = songs.map { song ->
                            if (song.externalId == songId) {
                                song.copy(genres = song.genres + genre)
                            } else {
                                song
                            }
                        }.toMutableList()
                    }
                }
            }
        }
        emit(FlowEvent.Success(songs))
    }
    .catch {
        emit(FlowEvent.Failure(it.message ?: "Unknown"))
    }
    .flowOn(Dispatchers.IO)

    suspend fun getSongCover(songPath: String): ByteArray? = withContext(Dispatchers.IO) {
        val uri: Uri = if (songPath.startsWith("content://")) {
            Uri.parse(songPath)
        } else {
            Uri.fromFile(File(songPath))
        }

        try {
            context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                kTagLib.getArtwork(pfd.detachFd())
            }

        } catch (e: Exception) {
            null
        }
    }
}