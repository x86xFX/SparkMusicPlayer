package me.theek.spark.core.content_reader

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.model.util.FlowEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaStoreReader @Inject constructor(@ApplicationContext private val context: Context) {

    private val contentResolver = context.contentResolver

    fun getAudioData(): Flow<FlowEvent<List<Song>>> = flow<FlowEvent<List<Song>>> {

        if(!context.isAudioFileReadPermissionGranted()) {
            emit(FlowEvent.Failure(message = "Permission required for read audio files"))
            return@flow
        }

        var songs: MutableList<Song> = mutableListOf()

        val songCursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ALBUM,
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
                    albumName = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)),
                    duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)),
                    trackNumber = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)),
                    releaseYear = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR)),
                    genres = emptyList(),
                    mimeType = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.MIME_TYPE)),
                    lastModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)),
                    size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)),
                    path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)),
                    externalId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)).toString(),
                    isFavourite = false
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
        contentResolver.query(
            MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME),
            null,
            null,
            null
        )?.use { genreCursor ->
            while (currentCoroutineContext().isActive && genreCursor.moveToNext()) {
                val id = genreCursor.getLong(genreCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID))
                val genre = genreCursor.getString(genreCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME))

                contentResolver.query(
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
        emit(FlowEvent.Failure(it.message ?: "Something went wrong"))
    }
    .flowOn(Dispatchers.IO)

    @Suppress("DEPRECATION") //Suppress because createSource only support for API 28+
    fun getSongCover(songExternalId: String?):  Bitmap? {
        return if (songExternalId != null) {
            val coverUri = Uri.parse("content://media/external/audio/media/${songExternalId}/albumart")
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(contentResolver, coverUri)
                    ) { decoder, _, _ ->
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                        decoder.isMutableRequired = true
                    }
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, coverUri)
                }
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
}

private fun Context.isAudioFileReadPermissionGranted() : Boolean {
     return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
    } else {
        this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}