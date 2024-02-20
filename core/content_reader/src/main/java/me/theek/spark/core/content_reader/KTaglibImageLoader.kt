package me.theek.spark.core.content_reader

import android.content.Context
import android.net.Uri
import com.simplecityapps.ktaglib.KTagLib
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KTaglibImageLoader @Inject constructor(
    private val kTagLib: KTagLib,
    @ApplicationContext private val context: Context
) {

    suspend fun getArt(songPath: String): ByteArray? = withContext(Dispatchers.IO) {
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