package me.theek.spark.core.content_reader

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.simplecityapps.ktaglib.KTagLib
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
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

        Log.d("MediaStoreReader", "Flow executed on ${Thread.currentThread().name}")

        val songs: MutableList<Song> = mutableListOf()

        val songCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
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

            while (currentCoroutineContext().isActive && cursor.moveToNext()) {
                val song = Song(
                    id = 0,
                    songName = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)),
                    artistName = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)),
                    albumId = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)),
                    duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)),
                    trackNumber = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)),
                    path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA))
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
        emit(FlowEvent.Success(songs))

    }.flowOn(Dispatchers.IO)

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
            e.printStackTrace()
            null
        }
    }
}