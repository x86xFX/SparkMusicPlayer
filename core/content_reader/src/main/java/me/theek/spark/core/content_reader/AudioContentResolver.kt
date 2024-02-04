package me.theek.spark.core.content_reader

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.theek.spark.core.model.data.Audio
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioContentResolver @Inject constructor(@ApplicationContext private val context: Context) : ContentResolverHelper {

    private var mCursor: Cursor? = null

    private val projection: Array<String> = arrayOf(
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.AudioColumns.TITLE,
        MediaStore.Audio.AudioColumns.ALBUM_ID,
        MediaStore.Audio.AudioColumns.TRACK
    )

    private val selectionClause: String = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?"

    private val selectArgs: Array<String> = arrayOf("1")

    private val sortOrder: String = "${MediaStore.Audio.AudioColumns.DATE_ADDED} ASC"

    override fun getAudioData(): Flow<List<Audio>> = flow {
        val audioList: MutableList<Audio> = mutableListOf()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // External storage
            projection,
            selectionClause,
            selectArgs,
            sortOrder
        )

        mCursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)
            val trackColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)

            cursor.apply {
                if (count == 0) {
                    Log.d("Cursor", "get cursor data: Cursor is empty")
                } else {
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)

                        audioList += Audio(
                            id = id,
                            uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id),
                            displayName = cursor.getString(displayNameColumn),
                            artistName = cursor.getString(artistColumn),
                            duration = cursor.getInt(durationColumn),
                            title = cursor.getString(titleColumn),
                            albumId = cursor.getInt(albumIdColumn),
                            trackNumber = cursor.getInt(trackColumn)
                        )
                    }
                }
            }
        }

        emit(audioList)
    }
}