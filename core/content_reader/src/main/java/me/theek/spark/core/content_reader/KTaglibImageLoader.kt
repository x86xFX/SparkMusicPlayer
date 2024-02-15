package me.theek.spark.core.content_reader

import android.content.Context
import android.net.Uri
import com.simplecityapps.ktaglib.KTagLib
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KTaglibImageLoader @Inject constructor(
    private val kTagLib: KTagLib,
    @ApplicationContext private val context: Context
) {

    fun getArt(songPath: String): ByteArray? {
        val uri: Uri = if (songPath.startsWith("content://")) {
            Uri.parse(songPath)
        } else {
            Uri.fromFile(File(songPath))
        }

        return try {
            context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                kTagLib.getArtwork(pfd.detachFd())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}